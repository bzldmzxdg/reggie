package com.dds.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.Category;

import java.util.List;

public interface CategoryService {

    //新增菜品/套餐分类
    public Integer save(Category category);

    //分页查询
    public Page<Category> page(Page<Category> pageInfo, QueryWrapper<Category> qw);

    //删除指定菜品/套餐分类
    public Integer delete(Long id);

    //修改指定菜品/套餐分类
    public Integer update(Category category);

    //根据type获取全部菜品或者套餐分类
    public List<Category> getByType(QueryWrapper<Category> qw);

    //根据分类id获取指定菜品/套餐分类
    public Category getById(Long id);
}
