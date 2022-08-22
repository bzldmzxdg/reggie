package com.dds.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.config.BaseContext;
import com.dds.reggie.entity.*;

import com.dds.reggie.service.OrdersDetailService;
import com.dds.reggie.service.OrdersService;
import com.dds.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("order")
public class OrdersController {

    @Autowired
    OrdersService ordersService;
    @Autowired
    OrdersDetailService ordersDetailService;
    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order){
        ordersService.save(order);
        return R.success("下单成功~");
    }
    //管理端获取所有订单
    @GetMapping("/page")
    public R<Page<Orders>> pageM(int page, int pageSize, String number,String beginTime,String endTime){

        Page<Orders> pageInfo = ordersService.page(page, pageSize, number,beginTime,endTime);
        return R.success(pageInfo);
    }
    //移动端获取当前用户订单
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> pageC(int page, int pageSize){

        Page<OrdersDto> pageInfo = ordersService.page(page,pageSize);
        return R.success(pageInfo);
    }

    //修改订单状态
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders order){
        ordersService.updateById(order);
        return R.success("修改订单状态成功！");
    }

    /**
     * 前端点击再来一单是直接跳转到购物车的，所以为了避免数据有问题，再跳转之前我们需要把购物车的数据给清除
     * ①通过orderId获取订单明细
     * ②把订单明细的数据的数据塞到购物车表中，不过在此之前要先把购物车表中的数据给清除(清除的是当前登录用户的购物车表中的数据)，
     * 不然就会导致再来一单的数据有问题；
     * (这样可能会影响用户体验，但是对于外卖来说，用户体验的影响不是很大，电商项目就不能这么干了)
     */
    @PostMapping("/again")
    @Transactional
    public R<String> again(@RequestBody Orders orders){
        //通过orderId获取订单明细
        Long orderId = orders.getId();
        List<OrderDetail> orderDetails = ordersDetailService.getByOrderId(orderId);
        //清除购物车数据
        shoppingCartService.clean();
        //把订单明细的数据的数据塞到购物车表中
        ShoppingCart shoppingCart = null;
        for(OrderDetail orderDetail:orderDetails){
            shoppingCart = new ShoppingCart();
            shoppingCart.setName(orderDetail.getName());
            shoppingCart.setUserId(BaseContext.get());
            shoppingCart.setDishId(orderDetail.getDishId());
            shoppingCart.setSetmealId(orderDetail.getSetmealId());
            shoppingCart.setNumber(orderDetail.getNumber());
            shoppingCart.setAmount(orderDetail.getAmount());
            shoppingCart.setDishFlavor(orderDetail.getDishFlavor());
            shoppingCart.setImage(orderDetail.getImage());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }
        return R.success("在来一单成功！");
    }

}
