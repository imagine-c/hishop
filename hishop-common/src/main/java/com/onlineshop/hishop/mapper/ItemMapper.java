package com.onlineshop.hishop.mapper;

import com.onlineshop.hishop.dto.front.SearchItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {

    List<SearchItem> getItemList();

    SearchItem getItemById(@Param("id") Long id);

}