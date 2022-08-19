package com.dds.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.Dish;
import com.dds.reggie.entity.DishDto;
import com.dds.reggie.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DishService {

    //根据category_id查询条目总数
    public Long selectCountByCategoryId(Long category_id);

    //保存菜品及其口味信息（涉及多张表，需要保证数据一致性）
    public void save(DishDto dishDto);

    //查询并分页菜品
    public void page(QueryWrapper<Dish> qw, Page<Dish> pageInfo);

    //根据菜品id查询菜品及其口味信息
    public DishDto getDishAndFlavorsById(Long id);

    //修改菜品信息及其欧维信息
    public void updateDishAndFlavors(DishDto dishDto);

    //根据菜品分类id查询所有菜品信息
    public List<Dish> getDishByCategoryId(QueryWrapper<Dish> qw);
}
