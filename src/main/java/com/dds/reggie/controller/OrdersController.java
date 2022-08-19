package com.dds.reggie.controller;


import com.dds.reggie.entity.Orders;
import com.dds.reggie.entity.R;

import com.dds.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("order")
public class OrdersController {

    @Autowired
    OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order){
        ordersService.save(order);
        return R.success("下单成功~");
    }
}
