package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.Category;
import com.dds.reggie.mapper.CategoryMapper;
import com.dds.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public Integer save(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    public Page<Category> page(Page<Category> pageInfo, QueryWrapper<Category> qw) {
        categoryMapper.selectPage(pageInfo,qw);
        return pageInfo;
    }

    @Override
    public Integer delete(Long id) {
        return categoryMapper.deleteById(id);
    }

    @Override
    public Integer update(Category category) {
        return categoryMapper.updateById(category);
    }

    @Override
    public List<Category> getByType(QueryWrapper<Category> qw) {
        return categoryMapper.selectList(qw);
    }

    @Override
    public Category getById(Long id) {
        return categoryMapper.selectById(id);
    }
}
