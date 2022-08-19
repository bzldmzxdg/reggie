package com.dds.reggie.service;

import com.dds.reggie.entity.OrderDetail;

public interface OrdersDetailService {

    //新增订单明细
    public Integer save(OrderDetail orderDetail);
}
