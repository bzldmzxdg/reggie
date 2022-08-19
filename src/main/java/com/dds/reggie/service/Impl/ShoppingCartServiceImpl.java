package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.config.BaseContext;
import com.dds.reggie.entity.ShoppingCart;
import com.dds.reggie.mapper.ShoppingCartMapper;
import com.dds.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCart getOne(QueryWrapper<ShoppingCart> qw) {
        return shoppingCartMapper.selectOne(qw);
    }

    @Override
    public Integer updateById(ShoppingCart shoppingCart) {
        return shoppingCartMapper.updateById(shoppingCart);
    }

    @Override
    public Integer save(ShoppingCart shoppingCart) {
        return shoppingCartMapper.insert(shoppingCart);
    }

    @Override
    public List<ShoppingCart> getAll(QueryWrapper<ShoppingCart> qw) {
        return shoppingCartMapper.selectList(qw);
    }

    @Override
    public Integer clean() {
        QueryWrapper<ShoppingCart> qw = new QueryWrapper<>();
        qw.eq("user_id", BaseContext.get());
        return shoppingCartMapper.delete(qw);
    }

    @Override
    public Integer delete(ShoppingCart shoppingCart) {
        return shoppingCartMapper.deleteById(shoppingCart.getId());
    }
}
