package com.dds.reggie.service;

import com.dds.reggie.entity.Orders;

public interface OrdersService {

    //保存订单
    public Integer save(Orders orders);
}
