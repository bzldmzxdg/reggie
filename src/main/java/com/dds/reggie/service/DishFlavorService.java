package com.dds.reggie.service;

import com.dds.reggie.entity.DishFlavor;

import java.util.List;

public interface DishFlavorService {

    //根据菜品id查询菜品口味信息
    public List<DishFlavor> getByDishId(Long dishId);

    //根据菜品id删除口味信息
    public Integer deleteByDishId(Long dishId);

    //保存口味
    public Integer save(DishFlavor dishFlavor);
}
