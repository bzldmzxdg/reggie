package com.dds.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.config.MyRuntimeException;
import com.dds.reggie.entity.Category;
import com.dds.reggie.entity.R;
import com.dds.reggie.service.CategoryService;
import com.dds.reggie.service.DishService;
import com.dds.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    //新增菜品/套餐分类
    @PostMapping
    public R<String> save(@RequestBody Category category){

        //调用Service层执行插入方法
        categoryService.save(category);

        return R.success("新增菜品/套餐分类成功！");
    }
    //分页展示所有菜品/套餐分类
    @GetMapping("/page")
    public R<Page<Category>> page(int page,int pageSize){

        Page<Category> pageInfo = new Page<>();

        QueryWrapper<Category> qw = new QueryWrapper<>();
        qw.orderByDesc("sort");
        //调用Service层执行查询并分页方法
        categoryService.page(pageInfo,qw);

        return R.success(pageInfo);
    }

    //删除指定菜品/套餐分类
    @DeleteMapping
    public R<String> delete(Long ids){


        //调用Service层执行根据id删除指定菜品/套餐分类的方法
        Long count1 = dishService.selectCountByCategoryId(ids);
        if(count1 > 0){
            throw new MyRuntimeException("当前分类已有菜品，不能删除！");
        }

        Long count2 = setmealService.selectCountByCategoryId(ids);


        if(count2 > 0){
            throw new MyRuntimeException("当前分类已有套餐，不能删除！");
        }

        categoryService.delete(ids);
        return R.success("删除成功！");
    }

    //修改指定菜品/套餐分类
    @PutMapping
    public R<String> update(@RequestBody Category category){

        //调用Service层执行根据修改指定菜品/套餐分类的方法
        categoryService.update(category);
        return R.success("修改成功！");

    }

    //根据type获取全部的菜品分类或者套餐分类
    @GetMapping("/list")
    public R<List<Category>> getByType(Category category){

        QueryWrapper<Category> qw = new QueryWrapper<>();
        qw.eq(category.getType()!=null,"type",category.getType());

        qw.orderByDesc("sort").orderByDesc("update_time");

        //调用Service层执行获取全部的菜品分类或者套餐分类方法
        List<Category> byType = categoryService.getByType(qw);
        return R.success(byType);

    }


}
