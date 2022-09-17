package com.onlineshop.hishop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.dto.front.AllGoodsResult;
import com.onlineshop.hishop.dto.front.Product;
import com.onlineshop.hishop.dto.front.ProductDet;
import com.onlineshop.hishop.mapper.TbItemDescMapper;
import com.onlineshop.hishop.mapper.TbItemMapper;
import com.onlineshop.hishop.mapper.TbPanelContentMapper;
import com.onlineshop.hishop.mapper.TbPanelMapper;
import com.onlineshop.hishop.pojo.*;
import com.onlineshop.hishop.service.ContentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@PropertySource("classpath:conf/sso.yml")
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbPanelMapper tbPanelMapper;
    @Autowired
    private TbPanelContentMapper tbPanelContentMapper;
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${PRODUCT_HOME}")
    private String PRODUCT_HOME;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Value("${HEADER_PANEL}")
    private String HEADER_PANEL;

    @Value("${HEADER_PANEL_ID}")
    private Integer HEADER_PANEL_ID;

    @Value("${RECOMEED_PANEL}")
    private String RECOMEED_PANEL;

    @Value("${RECOMEED_PANEL_ID}")
    private Integer RECOMEED_PANEL_ID;

    @Value("${ITEM_EXPIRE}")
    private Long ITEM_EXPIRE;






    @Override
    public List<TbPanel> getHome() {

        List<TbPanel> list=new ArrayList<>();

        //查询缓存
        try{
            //有缓存则读取
            String json = redisTemplate.opsForValue().get(PRODUCT_HOME);
            if(json!=null){
                list = new Gson().fromJson(json, new TypeToken<List<TbPanel>>(){}.getType());
                log.info("读取了首页缓存");
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //没有缓存
        TbPanelExample example=new TbPanelExample();
        TbPanelExample.Criteria criteria=example.createCriteria();
        //条件查询
        criteria.andPositionEqualTo(0);
        criteria.andStatusEqualTo(1);
        example.setOrderByClause("sort_order");
        list=tbPanelMapper.selectByExample(example);
        for(TbPanel tbPanel:list){
            TbPanelContentExample exampleContent=new TbPanelContentExample();
            exampleContent.setOrderByClause("sort_order");
            TbPanelContentExample.Criteria criteriaContent=exampleContent.createCriteria();
            //条件查询
            criteriaContent.andPanelIdEqualTo(tbPanel.getId());
            List<TbPanelContent> contentList=tbPanelContentMapper.selectByExample(exampleContent);
            for(TbPanelContent content:contentList){
                if(content.getProductId()!=null){
                    TbItem tbItem=tbItemMapper.selectByPrimaryKey(content.getProductId());
                    content.setProductName(tbItem.getTitle());
                    content.setSalePrice(tbItem.getPrice());
                    content.setSubTitle(tbItem.getSellPoint());
                }
            }

            tbPanel.setPanelContents(contentList);
        }
        //把结果添加至缓存
        try{
            redisTemplate.opsForValue().set(PRODUCT_HOME, new Gson().toJson(list));
            log.info("添加了首页缓存");
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<TbPanel> getRecommendGoods() {


        List<TbPanel> list = new ArrayList<>();
        //查询缓存
        try{
            //有缓存则读取
            String json=redisTemplate.opsForValue().get(RECOMEED_PANEL);
            if(json!=null){
                list = new Gson().fromJson(json, new TypeToken<List<TbPanel>>(){}.getType());
                log.info("读取了推荐板块缓存");
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        list = getTbPanelAndContentsById(RECOMEED_PANEL_ID);
        //把结果添加至缓存
        try{
            redisTemplate.opsForValue().set(RECOMEED_PANEL, new Gson().toJson(list));
            log.info("添加了推荐板块缓存");
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ProductDet getProductDet(Long id) {

        //查询缓存
        try{
            //有缓存则读取
            String json=redisTemplate.opsForValue().get(PRODUCT_ITEM+":"+id);
            if(json!=null){
                ProductDet productDet= new Gson().fromJson(json,ProductDet.class);
                log.info("读取了商品"+id+"详情缓存");
                //重置商品缓存时间
                redisTemplate.expire(PRODUCT_ITEM+":"+id, ITEM_EXPIRE, TimeUnit.SECONDS);
                return productDet;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        TbItem tbItem=tbItemMapper.selectByPrimaryKey(id);
        ProductDet productDet=new ProductDet();
        productDet.setProductId(id);
        productDet.setProductName(tbItem.getTitle());
        productDet.setSubTitle(tbItem.getSellPoint());
        productDet.setStatus(tbItem.getStatus());
        if(tbItem.getLimitNum()!=null&&!tbItem.getLimitNum().toString().isEmpty()){
            productDet.setLimitNum(Long.valueOf(tbItem.getLimitNum()));
        }else{
            productDet.setLimitNum(Long.valueOf(tbItem.getNum()));
        }
        productDet.setNum(tbItem.getNum());
        productDet.setSalePrice(tbItem.getPrice());

        TbItemDesc tbItemDesc=tbItemDescMapper.selectByPrimaryKey(id);
        productDet.setDetail(tbItemDesc.getItemDesc());

        if(tbItem.getImage()!=null&&!tbItem.getImage().isEmpty()){
            String images[]=tbItem.getImage().split(",");
            productDet.setProductImageBig(images[0]);
            List list=new ArrayList();
            for(int i=0;i<images.length;i++){
                list.add(images[i]);
            }
            productDet.setProductImageSmall(list);
        }
        //无缓存 把结果添加至缓存
        try{
            redisTemplate.opsForValue().set(PRODUCT_ITEM+":"+id,new Gson().toJson(productDet), ITEM_EXPIRE, TimeUnit.SECONDS);
            log.info("添加了商品"+id+"详情缓存");
        }catch (Exception e){
            e.printStackTrace();
        }
        return productDet;
    }

    @Override
    public AllGoodsResult getAllProduct(int page, int size, String sort, Long cid, int priceGt, int priceLte) {

        AllGoodsResult allGoodsResult=new AllGoodsResult();
        List<Product> list=new ArrayList<>();
        //分页执行查询返回结果
        if(page<=0) {
            page = 1;
        }
        PageHelper.startPage(page,size);

        //判断条件
        String orderCol="created";
        String orderDir="desc";
        if(sort.equals("1")){
            orderCol="price";
            orderDir="asc";
        }else if(sort.equals("-1")){
            orderCol="price";
            orderDir="desc";
        }else{
            orderCol="created";
            orderDir="desc";
        }

        List<TbItem> tbItemList = tbItemMapper.selectItemFront(cid,orderCol,orderDir,priceGt,priceLte);
        PageInfo<TbItem> pageInfo=new PageInfo<>(tbItemList);

        for(TbItem tbItem:tbItemList){
            Product product= DtoUtil.TbItem2Product(tbItem);
            list.add(product);
        }

        allGoodsResult.setData(list);
        allGoodsResult.setTotal((int) pageInfo.getTotal());

        return allGoodsResult;
    }

    @Override
    public List<TbPanelContent> getNavList() {

        List<TbPanelContent> list = new ArrayList<>();
        //查询缓存
        try{
            //有缓存则读取
            String json=redisTemplate.opsForValue().get(HEADER_PANEL);
            if(json!=null){
                list = new Gson().fromJson(json, new TypeToken<List<TbPanelContent>>(){}.getType());
                log.info("读取了导航栏缓存");
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        TbPanelContentExample exampleContent=new TbPanelContentExample();
        exampleContent.setOrderByClause("sort_order");
        TbPanelContentExample.Criteria criteriaContent=exampleContent.createCriteria();
        //条件查询
        criteriaContent.andPanelIdEqualTo(HEADER_PANEL_ID);
        list=tbPanelContentMapper.selectByExample(exampleContent);

        //把结果添加至缓存
        try{
            redisTemplate.opsForValue().set(HEADER_PANEL, new Gson().toJson(list));
            log.info("添加了导航栏缓存");
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
    List<TbPanel> getTbPanelAndContentsById(Integer panelId){

        List<TbPanel> list=new ArrayList<>();
        TbPanelExample example=new TbPanelExample();
        TbPanelExample.Criteria criteria=example.createCriteria();
        //条件查询
        criteria.andIdEqualTo(panelId);
        criteria.andStatusEqualTo(1);
        list=tbPanelMapper.selectByExample(example);
        for(TbPanel tbPanel:list){
            TbPanelContentExample exampleContent=new TbPanelContentExample();
            exampleContent.setOrderByClause("sort_order");
            TbPanelContentExample.Criteria criteriaContent=exampleContent.createCriteria();
            //条件查询
            criteriaContent.andPanelIdEqualTo(tbPanel.getId());
            List<TbPanelContent> contentList=tbPanelContentMapper.selectByExample(exampleContent);
            for(TbPanelContent content:contentList){
                if(content.getProductId()!=null){
                    TbItem tbItem=tbItemMapper.selectByPrimaryKey(content.getProductId());
                    content.setProductName(tbItem.getTitle());
                    content.setSalePrice(tbItem.getPrice());
                    content.setSubTitle(tbItem.getSellPoint());
                }
            }

            tbPanel.setPanelContents(contentList);
        }
        return list;
    }

}
