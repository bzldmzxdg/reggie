package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.Dish;
import com.dds.reggie.entity.DishDto;
import com.dds.reggie.entity.DishFlavor;
import com.dds.reggie.mapper.DishFlavorMapper;
import com.dds.reggie.mapper.DishMapper;
import com.dds.reggie.service.CategoryService;
import com.dds.reggie.service.DishFlavorService;
import com.dds.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishFlavorService dishFlavorService;

    @Override
    public Long selectCountByCategoryId(Long category_id) {

        QueryWrapper<Dish> qw = new QueryWrapper<>();
        qw.select("count(*) as count");
        qw.eq("category_id",category_id);

        List<Map<String, Object>> maps = dishMapper.selectMaps(qw);
        Long count = (Long) maps.get(0).get("count");
        return count;
    }


    @Override
    @Transactional
    public void save(DishDto dishDto) {

        dishMapper.insert(dishDto);

        List<DishFlavor> flavors = dishDto.getFlavors();

        Long dishId = dishDto.getId();

        for(DishFlavor flavor : flavors){
            flavor.setDishId(dishId);
            dishFlavorMapper.insert(flavor);
        }
    }

    @Override
    public void page(QueryWrapper<Dish> qw, Page<Dish> pageInfo) {
        dishMapper.selectPage(pageInfo,qw);
    }

    @Override
    public DishDto getDishAndFlavorsById(Long id) {
        //根据菜品id查询菜品信息
        Dish dish = dishMapper.selectById(id);
        String category_name = categoryService.getById(dish.getCategoryId()).getName();

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setCategoryName(category_name);

        //根据菜品id查询菜品口味信息
        List<DishFlavor> flavors = dishFlavorService.getByDishId(id);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateDishAndFlavors(DishDto dishDto) {
        //修改菜品信息
        dishMapper.updateById(dishDto);


        //1.根据菜品id删除口味信息
        dishFlavorService.deleteByDishId(dishDto.getId());
        //2.插入新的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor flavor : flavors){
            dishFlavorService.save(flavor);
        }
    }

    @Override
    public List<Dish> getDishByCategoryId(QueryWrapper<Dish> qw) {
        return dishMapper.selectList(qw);
    }
}
