package com.dds.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.Orders;
import com.dds.reggie.entity.OrdersDto;

public interface OrdersService {

    //保存订单
    public Integer save(Orders orders);

    //查询订单并分页展示
    public Page<Orders> page(int page, int pageSize, String number,String beginTime,String endTime);

    //查询当前用户所有订单
    public Page<OrdersDto> page(int page, int pageSize);

    //根据订单号修改订单状态
    public void updateById(Orders orders);
}
