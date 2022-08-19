package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.config.MyRuntimeException;
import com.dds.reggie.entity.Dish;
import com.dds.reggie.entity.Setmeal;
import com.dds.reggie.entity.SetmealDish;
import com.dds.reggie.entity.SetmealDto;
import com.dds.reggie.mapper.DishMapper;
import com.dds.reggie.mapper.SetmealMapper;
import com.dds.reggie.service.SetmealDishService;
import com.dds.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishService setmealDishService;
    @Override
    public Long selectCountByCategoryId(Long category_id) {

        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        qw.select("count(*) as count");
        qw.eq("category_id",category_id);

        List<Map<String, Object>> maps = setmealMapper.selectMaps(qw);
        Long count = (Long) maps.get(0).get("count");
        return count;
    }



    @Override
    @Transactional
    public void save(SetmealDto setmealDto) {
        //保存套餐信息
        setmealMapper.insert(setmealDto);

        Long id = setmealDto.getId();
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();

        for(SetmealDish dish : dishes){
            dish.setSetmealId(id);
            //保存套餐内菜品信息
            setmealDishService.save(dish);
        }
    }

    @Override
    public void page(Page<Setmeal> pageInfo, QueryWrapper<Setmeal> qw) {
        setmealMapper.selectPage(pageInfo,qw);
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {

        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        qw.in("id",ids);
        qw.eq("status",1);
        qw.select("count(*) as count");

        List<Map<String, Object>> maps = setmealMapper.selectMaps(qw);
        Long count = (Long) maps.get(0).get("count");
        //判断选中的套餐中是否有正在售卖的
        if(count > 0){
            throw new MyRuntimeException("存在套餐正在售卖，不可删除！");
        }

        //删除套餐信息
        qw = new QueryWrapper<>();
        qw.in("id",ids);
        setmealMapper.delete(qw);

        //删除套餐内菜品信息
        for(Long id : ids){
            setmealDishService.deleteBySetmealId(id);
        }
    }


    @Override
    public List<Setmeal> getAllByCategoryId(Setmeal setmeal) {
        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        qw.eq("category_id",setmeal.getCategoryId());
        qw.eq("status",setmeal.getStatus());
        return setmealMapper.selectList(qw);
    }
}
