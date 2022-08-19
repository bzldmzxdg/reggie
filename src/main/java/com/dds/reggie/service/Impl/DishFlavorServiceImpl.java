package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.entity.DishFlavor;
import com.dds.reggie.mapper.DishFlavorMapper;
import com.dds.reggie.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishFlavorServiceImpl implements DishFlavorService {

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Override
    public List<DishFlavor> getByDishId(Long dishId) {

        QueryWrapper<DishFlavor> qw = new QueryWrapper<>();
        qw.eq("dish_id",dishId);
        return dishFlavorMapper.selectList(qw);
    }

    @Override
    public Integer deleteByDishId(Long dishId) {
        QueryWrapper<DishFlavor> qw = new QueryWrapper<>();
        qw.eq("dish_id",dishId);
        return dishFlavorMapper.delete(qw);
    }

    @Override
    public Integer save(DishFlavor dishFlavor) {
        return dishFlavorMapper.insert(dishFlavor);
    }
}
