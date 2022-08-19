package com.dds.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dds.reggie.config.BaseContext;
import com.dds.reggie.entity.AddressBook;
import com.dds.reggie.entity.R;
import com.dds.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    //保存地址信息
    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        //设置当前登录用户的用户id
        addressBook.setUserId(BaseContext.get());
        addressBookService.save(addressBook);
        return R.success("保存成功！");
    }

    //根据当前用户id获取所有地址信息
    @GetMapping("/list")
    public R<List<AddressBook>> page(AddressBook addressBook){
        return R.success(addressBookService.page(BaseContext.get()));
    }

    //根据地址信息id获取地址信息
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id){
        return R.success(addressBookService.get(id));

    }

    //修改地址信息
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return R.success("修改成功！");
    }

    //删除地址信息
    @DeleteMapping
    public R<String> delete(Long ids){
        addressBookService.delete(ids);
        return R.success("删除成功！");
    }

    //根据地址id设置地址信息默认值为1
    @PutMapping("/default")
    public R<String> updateToDefault(@RequestBody AddressBook addressBook){

        //先把所有地址信息的默认值设置为0
        addressBookService.update_default(addressBook);
        return R.success("修改成功！");

    }
    //根据用户id查询默认地址信息
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        AddressBook aDefault = addressBookService.getDefault();
        return R.success(aDefault);
    }
}
