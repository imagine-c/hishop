package com.onlineshop.hishop.service.impl;


import com.google.gson.Gson;
import com.onlineshop.hishop.dto.EsCount;
import com.onlineshop.hishop.dto.EsInfo;
import com.onlineshop.hishop.dto.front.SearchItem;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.mapper.ItemMapper;
import com.onlineshop.hishop.service.SearchItemService;
import com.onlineshop.hishop.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
@PropertySource("classpath:conf/search.yml")
public class SearchItemServiceImpl implements SearchItemService {

    private final static Logger log = LoggerFactory.getLogger(SearchItemServiceImpl.class);

    @Autowired
    private ItemMapper itemMapper;

    @Value("${ES_CONNECT_IP}")
    private String ES_CONNECT_IP;

    @Value("${ES_NODE_CLIENT_PORT}")
    private String ES_NODE_CLIENT_PORT;

    @Value("${ES_CLUSTER_NAME}")
    private String ES_CLUSTER_NAME;

    @Value("${ITEM_INDEX}")
    private String ITEM_INDEX;

    @Value("${ITEM_TYPE}")
    private String ITEM_TYPE;

    @Override
    public int importAllItems() {

        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", ES_CLUSTER_NAME).build();
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(ES_CONNECT_IP), 9300));

            //????????????
            BulkRequestBuilder bulkRequest = client.prepareBulk();

            //??????????????????
            List<SearchItem> itemList = itemMapper.getItemList();

            //??????????????????
            for (SearchItem searchItem : itemList) {
                String image = searchItem.getProductImageBig();
                if (image != null && !"".equals(image)) {
                    String[] strings = image.split(",");
                    image = strings[0];
                } else {
                    image = "";
                }
                searchItem.setProductImageBig(image);
                bulkRequest.add(client.prepareIndex(ITEM_INDEX, ITEM_TYPE, String.valueOf(searchItem.getProductId()))
                        .setSource(jsonBuilder()
                                .startObject()
                                .field("productId", searchItem.getProductId())
                                .field("salePrice", searchItem.getSalePrice().floatValue())
                                .field("productName", searchItem.getProductName())
                                .field("subTitle", searchItem.getSubTitle())
                                .field("productImageBig", searchItem.getProductImageBig())
                                .field("categoryName", searchItem.getCategoryName())
                                .field("cid", searchItem.getCid())
                                .endObject()
                        )
                );
            }

            BulkResponse bulkResponse = bulkRequest.get();

            log.info("??????????????????");

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new HiShopException("??????ES?????????????????????????????????");
        }

        return 1;
    }

    @Override
    public EsInfo getEsInfo() {

        String healthUrl = "http://" + ES_CONNECT_IP + ":" + ES_NODE_CLIENT_PORT + "/_cluster/health";
        String countUrl = "http://" + ES_CONNECT_IP + ":" + ES_NODE_CLIENT_PORT + "/_count";
        String healthResult = HttpUtil.sendGet(healthUrl);
        String countResult = HttpUtil.sendGet(countUrl);
        if (StringUtils.isBlank(healthResult) || StringUtils.isBlank(countResult)) {
            throw new HiShopException("??????????????????????????????ES????????????");
        }
        EsInfo esInfo = new EsInfo();
        EsCount esCount = new EsCount();
        try {
            esInfo = new Gson().fromJson(healthResult, EsInfo.class);
            esCount = new Gson().fromJson(countResult, EsCount.class);
            esInfo.setCount(esCount.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            throw new HiShopException("??????ES????????????");
        }

        return esInfo;
    }
}
