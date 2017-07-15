package com.syshlang.smm.mapper;

import com.syshlang.smm.pojo.ItemsCustom;
import com.syshlang.smm.pojo.ItemsQueryVo;

import java.util.List;

public interface ItemsMapperCustom {
    //商品查询列表
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo)throws Exception;
}