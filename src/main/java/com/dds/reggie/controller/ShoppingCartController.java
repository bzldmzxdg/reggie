package com.dds.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.config.BaseContext;
import com.dds.reggie.entity.Dish;
import com.dds.reggie.entity.R;
import com.dds.reggie.entity.ShoppingCart;
import com.dds.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    //添加商品到购物车
    @PostMapping("/add")
    @Transactional
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置当前要添加的菜品/套餐所属用户的id
        shoppingCart.setUserId(BaseContext.get());
        //设置添加时间

        //判断添加的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        QueryWrapper<ShoppingCart> qw = new QueryWrapper<>();
        qw.eq("user_id",shoppingCart.getUserId());
        if(dishId!=null){
            //如果是菜品
            qw.eq("dish_id",shoppingCart.getDishId());
        }else{
            //如果是套餐
            qw.eq("setmeal_id",shoppingCart.getSetmealId());
        }

        //判断购物车中是否有该菜品/套餐
        ShoppingCart one = shoppingCartService.getOne(qw);
        if(one!=null){
            //如果有，则在原数量基础上加一
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
        }else{
            //如果没有，则直接插入数据库
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);

            one = shoppingCart;
        }

        return R.success(one);
    }

    //获取当前用户购物车所有信息
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        QueryWrapper<ShoppingCart> qw = new QueryWrapper<>();
        qw.eq("user_id",BaseContext.get());
        List<ShoppingCart> all = shoppingCartService.getAll(qw);
        return R.success(all);
    }

    //清空当前用户购物车所有信息
    @DeleteMapping("/clean")
    public R<String> clean(){
        shoppingCartService.clean();
        return R.success("清空购物车成功！");
    }

    //减少当前用户某个菜品/套餐数量
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        QueryWrapper<ShoppingCart> qw = new QueryWrapper<>();
        qw.eq("user_id",BaseContext.get());
        Long dishId = shoppingCart.getDishId();
        if(dishId!=null){
            qw.eq("dish_id",dishId);
        }else{
            qw.eq("setmeal_id",shoppingCart.getSetmealId());
        }

        ShoppingCart one = shoppingCartService.getOne(qw);

        //如果当前商品的数量等于1的时候，删除该购物车商品信息，否则修改数量减1
        if(one.getNumber() > 1){
            one.setNumber(one.getNumber()-1);
            shoppingCartService.updateById(one);
        }else{
            one.setNumber(one.getNumber()-1);
            shoppingCartService.delete(one);

        }

        return R.success(one);
    }
}
