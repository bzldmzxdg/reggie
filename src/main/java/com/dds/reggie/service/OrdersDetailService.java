package com.dds.reggie.service;

import com.dds.reggie.entity.OrderDetail;

import java.util.List;

public interface OrdersDetailService {

    //新增订单明细
    public Integer save(OrderDetail orderDetail);

    //根据订单号查询订单明细
    public List<OrderDetail> getByOrderId(Long orderId);
}
