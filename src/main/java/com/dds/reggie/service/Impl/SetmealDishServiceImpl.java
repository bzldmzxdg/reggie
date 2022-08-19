package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.entity.SetmealDish;
import com.dds.reggie.mapper.SetmealDishMapper;
import com.dds.reggie.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
