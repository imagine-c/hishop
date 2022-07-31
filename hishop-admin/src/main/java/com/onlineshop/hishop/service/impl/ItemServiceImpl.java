package com.onlineshop.hishop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.dto.ItemDto;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.jedis.JedisClient;
import com.onlineshop.hishop.mapper.TbItemCatMapper;
import com.onlineshop.hishop.mapper.TbItemDescMapper;
import com.onlineshop.hishop.mapper.TbItemMapper;
import com.onlineshop.hishop.mapper.TbPanelContentMapper;
import com.onlineshop.hishop.pojo.*;
import com.onlineshop.hishop.service.ItemService;
import com.onlineshop.hishop.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
@Component
@PropertySource("classpath:conf/admin.properties")
public class ItemServiceImpl implements ItemService {

    private final static Logger log= LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private TbPanelContentMapper tbPanelContentMapper;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;

    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Override
    public ItemDto getItemById(Long id) {
        ItemDto itemDto=new ItemDto();
        TbItem tbItem=tbItemMapper.selectByPrimaryKey(id);
        itemDto= DtoUtil.TbItem2ItemDto(tbItem);
        TbItemCat tbItemCat=tbItemCatMapper.selectByPrimaryKey(itemDto.getCid());
        if (tbItemCat!=null)
            itemDto.setCname(tbItemCat.getName());
        TbItemDesc tbItemDesc=tbItemDescMapper.selectByPrimaryKey(id);
        itemDto.setDetail(tbItemDesc.getItemDesc());
        return itemDto;
    }

    @Override
    public TbItem getNormalItemById(Long id) {

        return tbItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTablesResult getItemList(int draw, int start, int length, int cid, String search,
                                        String orderCol, String orderDir) {

        DataTablesResult result=new DataTablesResult();

        //分页执行查询返回结果
        PageHelper.startPage(start/length+1,length);
        List<TbItem> list = tbItemMapper.selectItemByCondition(cid,"%"+search+"%",orderCol,orderDir);
        PageInfo<TbItem> pageInfo=new PageInfo<>(list);
        result.setRecordsFiltered((int)pageInfo.getTotal());
        result.setRecordsTotal(getAllItemCount().getRecordsTotal());

        result.setDraw(draw);
        result.setData(list);

        return result;
    }

    @Override
    public DataTablesResult getItemSearchList(int draw, int start, int length,int cid, String search,
                                              String minDate, String maxDate, String orderCol, String orderDir) {

        DataTablesResult result=new DataTablesResult();

        //分页执行查询返回结果
        PageHelper.startPage(start/length+1,length);
        List<TbItem> list = tbItemMapper.selectItemByMultiCondition(cid,"%"+search+"%",minDate,maxDate,orderCol,orderDir);
        PageInfo<TbItem> pageInfo=new PageInfo<>(list);
        result.setRecordsFiltered((int)pageInfo.getTotal());
        result.setRecordsTotal(getAllItemCount().getRecordsTotal());

        result.setDraw(draw);
        result.setData(list);

        return result;
    }

    @Override
    public DataTablesResult getAllItemCount() {
        TbItemExample example=new TbItemExample();
        Long count=tbItemMapper.countByExample(example);
        DataTablesResult result=new DataTablesResult();
        result.setRecordsTotal(Math.toIntExact(count));
        return result;
    }

    @Override
    public TbItem alertItemState(Long id, Integer state) {

        TbItem tbMember = getNormalItemById(id);
        tbMember.setStatus(state.byteValue());
        tbMember.setUpdated(new Date());

        if (tbItemMapper.updateByPrimaryKey(tbMember) != 1){
            throw new HiShopException("修改商品状态失败");
        }
        if (state==0)
            this.jmsMessagingTemplate.convertAndSend(this.topic,"delete,"+id);
        if (state==1)
            this.jmsMessagingTemplate.convertAndSend(this.topic,"add,"+id);
//        if (state==0) {
//            try {
//                System.out.println("status:"+state+"delete");
//                sendRefreshESMessage("delete", id);
//            } catch (Exception e) {
//                log.error("同步索引出错");
//            }
//        }
//        if (state==1){
//            try {
//                System.out.println("status:"+state+"add");
//                sendRefreshESMessage("add", id);
//            } catch (Exception e) {
//                log.error("同步索引出错");
//            }



        //同步缓存
        deleteProductDetRedis(id);

        return getNormalItemById(id);
    }

//    @Override
//    public int deleteItem(Long id) {
//
//        if(tbItemMapper.deleteByPrimaryKey(id)!=1){
//            throw new HiShopException("删除商品失败");
//        }
//        if(tbItemDescMapper.deleteByPrimaryKey(id)!=1){
//            throw new HiShopException("删除商品详情失败");
//        }
//        //发送消息同步索引库
//        try {
//            sendRefreshESMessage("delete",id);
//        }catch (Exception e){
//            log.error("同步索引出错");
//        }
//        return 0;
//    }

    @Override
    public TbItem addItem(ItemDto itemDto) {
        long id= IDUtil.getRandomId();
        TbItem tbItem= DtoUtil.ItemDto2TbItem(itemDto);
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        if(tbItem.getImage().isEmpty()){
            tbItem.setImage("http://62.234.40.104:88/user/img/5b48b58b511dc.jpg");
        }
        if(tbItemMapper.insert(tbItem)!=1){
            throw new HiShopException("添加商品失败");
        }
        TbItemDesc tbItemDesc=new TbItemDesc();
        tbItemDesc.setItemId(id);
        tbItemDesc.setItemDesc(itemDto.getDetail());
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        if(tbItemDescMapper.insert(tbItemDesc)!=1){
            throw new HiShopException("添加商品详情失败");
        }

        this.jmsMessagingTemplate.convertAndSend(this.topic,"add,"+id);
        //发送消息同步索引库
//        try {
//            sendRefreshESMessage("add",id);
//        }catch (Exception e){
//            log.error("同步索引出错");
//        }
        return getNormalItemById(id);
    }

    @Override
    public TbItem updateItem(Long id,ItemDto itemDto) {

        TbItem oldTbItem=getNormalItemById(id);

        TbItem tbItem= DtoUtil.ItemDto2TbItem(itemDto);

        if(tbItem.getImage().isEmpty()){
            tbItem.setImage(oldTbItem.getImage());
        }
        tbItem.setId(id);
        tbItem.setStatus(oldTbItem.getStatus());
        tbItem.setCreated(oldTbItem.getCreated());
        tbItem.setUpdated(new Date());
        if(tbItemMapper.updateByPrimaryKey(tbItem)!=1){
            throw new HiShopException("更新商品失败");
        }

        TbItemDesc tbItemDesc=new TbItemDesc();

        tbItemDesc.setItemId(id);
        tbItemDesc.setItemDesc(itemDto.getDetail());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setCreated(oldTbItem.getCreated());

        if(tbItemDescMapper.updateByPrimaryKey(tbItemDesc)!=1){
            throw new HiShopException("更新商品详情失败");
        }
        //同步缓存
        deleteProductDetRedis(id);
        //发送消息同步索引库

        this.jmsMessagingTemplate.convertAndSend(this.topic,"add,"+id);
//        try {
//            sendRefreshESMessage("add",id);
//        }catch (Exception e){
//            log.error("同步索引出错");
//        }
        return getNormalItemById(id);
    }

    @Override
    public boolean isExist(Long pid) {
        TbPanelContentExample example = new TbPanelContentExample();
        TbPanelContentExample.Criteria cri = example.createCriteria();
        cri.andProductIdEqualTo(pid);
        List<TbPanelContent> list = tbPanelContentMapper.selectByExample(example);
        if (!list.isEmpty())
            return true;
        return false;
    }

    /**
     * 同步商品详情缓存
     * @param id
     */
    public void deleteProductDetRedis(Long id){
        try {
            log.error(PRODUCT_ITEM);
            jedisClient.del(PRODUCT_ITEM+":"+id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发送消息同步索引库
     * @param type
     * @param id
     */
//    public void sendRefreshESMessage(String type,Long id) {
//        jmsTemplate.send(topicDestination, new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                TextMessage textMessage = session.createTextMessage(type+","+String.valueOf(id));
//                return textMessage;
//            }
//        });
//    }
}
