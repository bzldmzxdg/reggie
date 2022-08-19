package com.dds.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.*;
import com.dds.reggie.service.CategoryService;
import com.dds.reggie.service.DishFlavorService;
import com.dds.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;

    //保存菜品及其菜品口味信息
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        log.info("dishDto : {}",dishDto);


        dishService.save(dishDto);

        return R.success("新增菜品成功！");

    }

    //查询全部菜品及其菜品分类名称并分页
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page,int pageSize,String name){

        Page<Dish> pageInfo = new Page<>(page,pageSize);

        QueryWrapper<Dish> qw = new QueryWrapper<>();
        qw.orderByDesc("update_time");
        qw.like(StringUtils.isNotEmpty(name),"name",name);

        dishService.page(qw,pageInfo);

        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtos = new ArrayList<>();
        for(Dish dish : records){
            DishDto dishDto = new DishDto();
            Category category = categoryService.getById(dish.getCategoryId());
            dishDto.setCategoryName(category.getName());

            BeanUtils.copyProperties(dish,dishDto);
            dishDtos.add(dishDto);

        }
        dishDtoPage.setRecords(dishDtos);

        return R.success(dishDtoPage);


    }

    //根据菜品id获取菜品及其口味信息
    @GetMapping("/{id}")
    public R<DishDto> getDishAndFlavorsById(@PathVariable Long id){
        DishDto dishDto = dishService.getDishAndFlavorsById(id);
        return R.success(dishDto);
    }

    //修改菜品信息及其口味信息
    @PutMapping
    public R<String> updateDishAndFlavors(@RequestBody DishDto dishDto){

        dishService.updateDishAndFlavors(dishDto);
        return R.success("修改菜品成功！");

    }
    //根据菜品分类id获取全部菜品
    @GetMapping("/list")
    public R<List<DishDto>> getByCategoryId(Dish dish){
        QueryWrapper<Dish> qw = new QueryWrapper<>();
        qw.eq("category_id",dish.getCategoryId());
        qw.orderByDesc("update_time").orderByAsc("sort");
        qw.eq("status",1);

        List<Dish> dishes = dishService.getDishByCategoryId(qw);
        //重新封装为有额外信息的对象（比如口味，菜品分类名称）返还给客户端
        List<DishDto> dishDtos = new ArrayList<>();

        for(Dish tmpDish:dishes){
            DishDto tmpDishDto = dishService.getDishAndFlavorsById(tmpDish.getId());
            dishDtos.add(tmpDishDto);
        }

        return R.success(dishDtos);
    }

}
