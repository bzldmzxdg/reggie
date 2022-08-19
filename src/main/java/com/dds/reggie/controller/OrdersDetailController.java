package com.dds.reggie.controller;

import com.dds.reggie.service.OrdersDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/ordersDetail")
public class OrdersDetailController {

    @Autowired
    OrdersDetailService ordersDetailService;
}
