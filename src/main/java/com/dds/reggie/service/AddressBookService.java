package com.dds.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    //保存地址信息
    public Integer save(AddressBook addressBook);

    //根据当前登录用户id获取所有地址信息并分页
    public List<AddressBook> page(Long userId);

    //根据地址id获取地址信息
    public AddressBook get(Long id);

    //修改地址信息
    public Integer update(AddressBook addressBook);

    //根据地址id删除地址信息
    public Integer delete(Long ids);

    //重新设置默认地址
    public Integer update_default(AddressBook addressBook);

    //根据用户id获取默认地址
    public AddressBook getDefault();

}
