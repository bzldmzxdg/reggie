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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    RedisTemplate redisTemplate;


    //新增菜品及其菜品口味信息
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        //优化：菜品及其菜品口味信息后需要清理当前缓存数据，保证数据一致性
        dishService.save(dishDto);

        //这里选择清理指定key的数据，也可以清理所有缓存数据，看需求编写
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

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

        //优化：修改菜品及其菜品口味信息后需要清理当前缓存数据，保证数据一致性
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("修改菜品成功！");

    }
    //根据菜品分类id获取全部菜品
    @GetMapping("/list")
    public R<List<DishDto>> getByCategoryId(Dish dish){

        dish.setStatus(1);//这里必定是查询正在起售的菜品，防止前端请求地址漏掉

        //优化：确认是进行缓存
        //确认要查询的数据在redis中的key值
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        List<DishDto> dishDtos = (List<DishDto>)redisTemplate.opsForValue().get(key);
        //如果缓存存在数据直接返回缓存数据
        if(dishDtos != null){
            return R.success(dishDtos);
        }


        //如果缓存中无数据则查询mysql数据库并将数据存入redis中
        List<Dish> dishes = dishService.getDishByCategoryId(dish);
        //重新封装为有额外信息的对象（比如口味，菜品分类名称）返还给客户端
        dishDtos = new ArrayList<>();
        for(Dish tmpDish:dishes){
            DishDto tmpDishDto = dishService.getDishAndFlavorsById(tmpDish.getId());
            dishDtos.add(tmpDishDto);
        }
        redisTemplate.opsForValue().set(key,dishDtos,60, TimeUnit.MINUTES);


        return R.success(dishDtos);
    }

    //修改菜品售卖状态
    @PostMapping("/status/{code}")
    public R<String> updateStatus(@PathVariable("code") Integer statusCode,Long ids){
        dishService.updateStatus(statusCode,ids);
        return R.success("修改成功！");
    }










}
