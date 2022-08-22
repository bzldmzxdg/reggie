package com.dds.reggie.service;

import com.dds.reggie.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService {

    //保存套餐内菜品信息
    public Integer save(SetmealDish setmealDish);

    //根据套餐id删除该套餐内菜品信息
    public void deleteBySetmealId(Long setmealId);

    //根据菜品id在所有套餐内去除该菜品
    public void deleteDishByDishId(List<Long> dishIds);

    //根据套餐id返回套餐内菜品信息
    public List<SetmealDish> getBySetmealId(Long setmealId);
}
