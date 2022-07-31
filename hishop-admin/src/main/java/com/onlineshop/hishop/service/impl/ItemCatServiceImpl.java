package com.onlineshop.hishop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.mapper.TbItemCatMapper;
import com.onlineshop.hishop.pojo.TbItemCat;
import com.onlineshop.hishop.pojo.TbItemCatExample;
import com.onlineshop.hishop.domain.ZTreeNode;
import com.onlineshop.hishop.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Component
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public TbItemCat getItemCatById(Long id) {

        TbItemCat tbItemCat=tbItemCatMapper.selectByPrimaryKey(id);
        if(tbItemCat==null){
            throw new HiShopException("通过id获取商品分类失败");
        }
        return tbItemCat;
    }

    @Override
    public List<ZTreeNode> getItemCatList(int parentId) {

        TbItemCatExample example=new TbItemCatExample();
        TbItemCatExample.Criteria criteria=example.createCriteria();
        //排序
        example.setOrderByClause("sort_order");
        //条件查询
        criteria.andParentIdEqualTo(new Long(parentId));
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);

        //转换成ZtreeNode
        List<ZTreeNode> resultList=new ArrayList<>();

        for(TbItemCat tbItemCat:list){
            ZTreeNode node = DtoUtil.TbItemCat2ZTreeNode(tbItemCat);


            resultList.add(node);
        }

        return resultList;
    }

    @Override
    public int addItemCat(TbItemCat tbItemCat) {

        if(tbItemCat.getParentId()==0){
            //根节点
            tbItemCat.setSortOrder(0);
            tbItemCat.setStatus(1);
        }else{
            TbItemCat parent=tbItemCatMapper.selectByPrimaryKey(tbItemCat.getParentId());
            tbItemCat.setSortOrder(0);
            tbItemCat.setStatus(1);
            tbItemCat.setCreated(new Date());
            tbItemCat.setUpdated(new Date());
        }


        if(tbItemCatMapper.insert(tbItemCat)!=1){
            throw new HiShopException("添加商品分类失败");
        }
        return 1;
    }

    @Override
    public int updateItemCat(TbItemCat tbItemCat) {

        TbItemCat old=getItemCatById(tbItemCat.getId());
        tbItemCat.setCreated(old.getCreated());
        tbItemCat.setUpdated(new Date());

        if(tbItemCatMapper.updateByPrimaryKey(tbItemCat)!=1){
            throw new HiShopException("添加商品分类失败");
        }
        return 1;
    }

    @Override
    public void deleteItemCat(Long id) {
        deleteZTree(id);
    }

    @Override
    public void deleteZTree(Long id) {

        //查询该节点所有子节点
        List<ZTreeNode> node=getItemCatList(Math.toIntExact(id));
        if(node.size()>0){
            //如果有子节点，遍历子节点
            for(int i=0;i<node.size();i++){
                deleteItemCat((long) node.get(i).getId());
            }
        }
        //没有子节点直接删除
        if(tbItemCatMapper.deleteByPrimaryKey(id)!=1){
            throw new HiShopException("删除商品分类失败");
        }
    }

}
