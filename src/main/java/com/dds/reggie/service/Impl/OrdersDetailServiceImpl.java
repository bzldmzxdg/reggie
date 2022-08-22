package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dds.reggie.entity.OrderDetail;
import com.dds.reggie.mapper.OrdersDetailMapper;
import com.dds.reggie.service.OrdersDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersDetailServiceImpl implements OrdersDetailService {

    @Autowired
    OrdersDetailMapper orderDetailsMapper;

    @Override
    public Integer save(OrderDetail orderDetail) {
        return orderDetailsMapper.insert(orderDetail);
    }

    @Override
    public List<OrderDetail> getByOrderId(Long orderId) {
        QueryWrapper<OrderDetail> qw = new QueryWrapper<>();
        qw.eq("order_id",orderId);
        return orderDetailsMapper.selectList(qw);
    }
}
