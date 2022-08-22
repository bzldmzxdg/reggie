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
import org.springframework.transaction.annotation.Transactional;
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

    //新增套餐及其套餐内菜品信息
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

    //根据套餐id删除套餐及其套餐内菜品信息（不会影响购物车和订单信息）
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

    //根据套餐id修改套餐售卖状态（不会影响购物车和订单信息）
    @PostMapping("/status/{code}")
    @Transactional
    public R<String> updateStatus(@PathVariable("code") Integer statusCode,@RequestParam List<Long> ids){

        setmealService.updateStatus(statusCode,ids);

        return R.success("修改成功！");
    }

    //根据套餐id返回套餐及其套餐内菜品信息
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable("id") Long id){
        SetmealDto setmealDto = setmealService.getById(id);
        return R.success(setmealDto);
    }

    //修改套餐及其套餐内菜品信息
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.update(setmealDto);
        return R.success("修改成功！");
    }

    //客户端根据套餐id回显套餐信息
    @GetMapping("/dish/{id}")
    public R<List<SetmealDish>> getDishes(@PathVariable("id") Long id){
        SetmealDto dishes = setmealService.getById(id);
        List<SetmealDish> list = dishes.getSetmealDishes();
        return R.success(list);

    }
}
