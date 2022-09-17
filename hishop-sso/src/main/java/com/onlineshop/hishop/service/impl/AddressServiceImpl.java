package com.onlineshop.hishop.service.impl;


import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.mapper.TbAddressMapper;
import com.onlineshop.hishop.pojo.TbAddress;
import com.onlineshop.hishop.pojo.TbAddressExample;
import com.onlineshop.hishop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private TbAddressMapper tbAddressMapper;

    @Value(value = "classpath:conf/cities.json")
    private Resource data;

    @Override
    public List<TbAddress> getAddressList(Long userId) {
        List<TbAddress> list = new ArrayList<>();
        TbAddressExample example = new TbAddressExample();
        TbAddressExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        list = tbAddressMapper.selectByExample(example);
        if (list == null) {
            throw new HiShopException("获取默认地址列表失败");
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsDefault()) {
                Collections.swap(list, 0, i);
                break;
            }
        }

        return list;
    }

    @Override
    public TbAddress getAddress(Long addressId) {
        TbAddress tbAddress = tbAddressMapper.selectByPrimaryKey(addressId);
        if (tbAddress == null) {
            throw new HiShopException("通过id获取地址失败");
        }
        return tbAddress;
    }

    @Override
    public int addAddress(TbAddress tbAddress) {

        //设置唯一默认
        setOneDefault(tbAddress);
        if (tbAddressMapper.insert(tbAddress) != 1) {
            throw new HiShopException("添加地址失败");
        }
        return 1;
    }

    @Override
    public int updateAddress(TbAddress tbAddress) {

        //设置唯一默认
        setOneDefault(tbAddress);
        if (tbAddressMapper.updateByPrimaryKey(tbAddress) != 1) {
            throw new HiShopException("更新地址失败");
        }
        return 1;
    }

    @Override
    public int delAddress(TbAddress tbAddress) {

        if (tbAddressMapper.deleteByPrimaryKey(tbAddress.getAddressId()) != 1) {
            throw new HiShopException("删除地址失败");
        }
        return 1;
    }

    public void setOneDefault(TbAddress tbAddress) {
        //设置唯一默认
        if (tbAddress.getIsDefault()) {
            TbAddressExample example = new TbAddressExample();
            TbAddressExample.Criteria criteria = example.createCriteria();
            criteria.andUserIdEqualTo(tbAddress.getUserId());
            List<TbAddress> list = tbAddressMapper.selectByExample(example);
            for (TbAddress tbAddress1 : list) {
                tbAddress1.setIsDefault(false);
                tbAddressMapper.updateByPrimaryKey(tbAddress1);
            }
        }
    }

    @Override
    public String getData() {
        try {
            File file = data.getFile();
//            long t0 = System.nanoTime();
            String jsonData = this.jsonRead(file);
//            long t1 = System.nanoTime();
//            long millis = TimeUnit.NANOSECONDS.toMillis(t1-t0);
            return jsonData;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读取文件类容为字符串
     *
     * @param file
     * @return
     */
    private String jsonRead(File file) {
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }
}
