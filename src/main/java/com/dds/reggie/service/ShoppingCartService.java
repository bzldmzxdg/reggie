package com.dds.reggie.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    //根据用户id和菜品/套餐id查询菜品/套餐信息
    public ShoppingCart getOne(QueryWrapper<ShoppingCart> qw);

    //根据购物车id修改信息
    public Integer updateById(ShoppingCart shoppingCart);

    //新增购物车信息
    public Integer save(ShoppingCart shoppingCart);

    //根据用户id获取购物车所有信息
    public List<ShoppingCart> getAll(QueryWrapper<ShoppingCart> qw);

    //根据用户id清空购物车
    public Integer clean();

    //根据用户id和菜品/套餐id删除购物车商品信息
    public Integer delete(ShoppingCart shoppingCart);
}
