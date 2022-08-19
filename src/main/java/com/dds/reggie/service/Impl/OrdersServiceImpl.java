package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dds.reggie.config.BaseContext;
import com.dds.reggie.entity.*;
import com.dds.reggie.mapper.OrdersDetailMapper;
import com.dds.reggie.mapper.OrdersMapper;
import com.dds.reggie.mapper.ShoppingCartMapper;
import com.dds.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    UserService userService;

    @Autowired
    AddressBookService addressBookService;

    @Autowired
    OrdersDetailService ordersDetailService;

    //保存订单
    @Override
    @Transactional
    public Integer save(Orders orders) {
        QueryWrapper<ShoppingCart> qw = new QueryWrapper<>();
        //获得当前用户id
        Long userId = BaseContext.get();
        //查询当前用户的购物车数据
        qw.eq("user_id",userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.getAll(qw);
        //查询用户数据
        User curUser = userService.getById(userId);
        //查询地址数据
        AddressBook curAddressBook = addressBookService.get(orders.getAddressBookId());

        //这里需要手动生成订单id，因为是先插入的订单明细，再插入订单，而订单明细需要订单id
        Long orderId = IdWorker.getId();//订单号

        //将购物车数据转化为订单明细，并计算总金额
        BigDecimal moneySum = new BigDecimal(0);

        for(ShoppingCart shoppingCart :shoppingCarts){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setImage(shoppingCart.getImage());
            //累加总金额
            moneySum = moneySum.add(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())));
            //循环插入订单明细
            ordersDetailService.save(orderDetail);
        }

        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));//订单号直接由订单id转化为string
        orders.setStatus(1);//设置为待付款
        orders.setUserId(userId);
        //由于没有开发支付功能，假设支付时间跟下单时间一样
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(moneySum);//设置订单总金额
        orders.setUserName(curUser.getName());//设置用户名
        orders.setPhone(curAddressBook.getPhone());//设置当前地址信息的收货人手机号，而不是所属用户的手机号，注意区分
        orders.setConsignee(curAddressBook.getConsignee());//设置收货人
        //设置地址信息
        orders.setAddress((curAddressBook.getProvinceName() == null ? "" : curAddressBook.getProvinceName())
                + (curAddressBook.getCityName() == null ? "" : curAddressBook.getCityName())
                + (curAddressBook.getDistrictName() == null ? "" : curAddressBook.getDistrictName())
                + (curAddressBook.getDetail() == null ? "" : curAddressBook.getDetail()));

        //向订单表插入数据，一条数据
        ordersMapper.insert(orders);
        //清空购物车数据
        shoppingCartService.clean();
        return 1;
    }
}
