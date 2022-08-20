package com.dds.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.R;
import com.dds.reggie.entity.Setmeal;
import com.dds.reggie.entity.SetmealDish;
import com.dds.reggie.entity.SetmealDto;
import com.dds.reggie.service.CategoryService;
import com.dds.reggie.service.SetmealDishService;
import com.dds.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    CategoryService categoryService;

    //保存套餐及其套餐内菜品信息
    @PostMapping
    //spring cache : @CacheEvict注解作用删除缓存中的数据
    @CacheEvict(value = "setmeal",allEntries = true)//删除类别为setmeal下的所有key
    public R<String> save(@RequestBody SetmealDto setmealDto){

        //保存套餐信息
        setmealService.save(setmealDto);

        return R.success("新增套餐成功！");

    }

    //查询套餐信息及其套餐分类名称并分页
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> page1 = new Page<>();


        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(name),"name",name);
        qw.orderByDesc("update_time");
        //查询套餐分页信息
        setmealService.page(pageInfo,qw);

        BeanUtils.copyProperties(pageInfo,page1,"records");

        List<SetmealDto> list = new ArrayList<>();
        List<Setmeal> records = pageInfo.getRecords();

        for(Setmeal setmeal : records){
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            //查询每个套餐信息的分类名称并赋值给setmealDto
            setmealDto.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
            list.add(setmealDto);
        }

        page1.setRecords(list);

        return R.success(page1);
    }

    //根据套餐id删除套餐信息
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.delete(ids);
        return R.success("删除成功！");
    }

    //根据套餐分类id获取返回所有启用的套餐信息
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        List<Setmeal> setMealList = setmealService.getAllByCategoryId(setmeal);
        return R.success(setMealList);
    }
}
