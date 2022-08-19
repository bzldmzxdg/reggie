package com.dds.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.config.BaseContext;
import com.dds.reggie.entity.AddressBook;
import com.dds.reggie.mapper.AddressBookMapper;
import com.dds.reggie.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    AddressBookMapper addressBookMapper;

    @Override
    public Integer save(AddressBook addressBook) {
        return addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> page(Long userId) {

        QueryWrapper<AddressBook> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        qw.orderByDesc("update_time");

        return addressBookMapper.selectList(qw);
    }

    @Override
    public AddressBook get(Long id) {
        return addressBookMapper.selectById(id);
    }

    @Override
    public Integer update(AddressBook addressBook) {
        return addressBookMapper.updateById(addressBook);
    }

    @Override
    public Integer delete(Long ids) {
        return addressBookMapper.deleteById(ids);
    }

    @Override
    @Transactional//多表操作需要设置事务
    public Integer update_default(AddressBook addressBook) {
        //先将所有的当前用户id下的所有地址的默认值设置为0
        UpdateWrapper<AddressBook> uw = new UpdateWrapper<>();
        uw.eq("user_id", BaseContext.get());
        AddressBook addressBook1 = new AddressBook();
        addressBook1.setIsDefault(0);
        addressBookMapper.update(addressBook1,uw);

        //再设置指定地址id的地址信息的默认值为1
        addressBook.setIsDefault(1);
        return addressBookMapper.updateById(addressBook);
    }


    @Override
    public AddressBook getDefault() {
        QueryWrapper<AddressBook> qw = new QueryWrapper<>();
        qw.eq("user_id",BaseContext.get());
        qw.eq("is_default",1);
        return addressBookMapper.selectOne(qw);
    }
}
