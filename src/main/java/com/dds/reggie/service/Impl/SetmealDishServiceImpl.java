package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.entity.SetmealDish;
import com.dds.reggie.mapper.SetmealDishMapper;
import com.dds.reggie.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl implements SetmealDishService {
    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Override
    public Integer save(SetmealDish setmealDish) {
        return setmealDishMapper.insert(setmealDish);
    }

    @Override
    public void deleteBySetmealId(Long setmealId) {

        QueryWrapper<SetmealDish> qw = new QueryWrapper<>();
        qw.eq("setmeal_id",setmealId);
        setmealDishMapper.delete(qw);

    }

    @Override
    public void deleteDishByDishId(List<Long> dishIds){
        //循环删除
        QueryWrapper<SetmealDish> qw;
        for(Long dishId:dishIds){
            qw = new QueryWrapper<>();
            qw.eq("dish_id",dishId);
            setmealDishMapper.delete(qw);
        }

    }

    @Override
    public List<SetmealDish> getBySetmealId(Long setmealId) {
        QueryWrapper<SetmealDish> qw = new QueryWrapper<>();
        qw.eq("setmeal_id",setmealId);
        return setmealDishMapper.selectList(qw);
    }
}
