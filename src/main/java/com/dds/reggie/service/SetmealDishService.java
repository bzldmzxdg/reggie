package com.dds.reggie.service;

import com.dds.reggie.entity.SetmealDish;

public interface SetmealDishService {

    //保存套餐内菜品信息
    public Integer save(SetmealDish setmealDish);

    //根据套餐id删除套餐内菜品
    public void deleteBySetmealId(Long setmealId);
}
