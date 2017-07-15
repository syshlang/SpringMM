package com.syshlang.smm.service.impl;

import com.syshlang.smm.dao.ItemsDao;
import com.syshlang.smm.exception.CustomException;
import com.syshlang.smm.pojo.Items;
import com.syshlang.smm.pojo.ItemsCustom;
import com.syshlang.smm.pojo.ItemsQueryVo;
import com.syshlang.smm.service.api.ItemsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 
 * <p>Title: ItemsServiceImpl</p>
 * <p>Description: 商品管理</p>
 *
 * @author	syshlang
 * @date	2017-07-13下午3:49:54
 * @version 1.0
 */
public class ItemsServiceImpl implements ItemsService{

	@Autowired
	private ItemsDao itemsDao;

	@Override
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo)
			throws Exception {

		return itemsDao.findItemsList(itemsQueryVo);
	}

	@Override
	public ItemsCustom findItemsById(Integer id) throws Exception {
		
		Items items = itemsDao.findItemsById(id);
		if(items==null){
			throw new CustomException("修改的商品信息不存在!");
		}
		//中间对商品信息进行业务处理
		//....
		//返回ItemsCustom
		ItemsCustom itemsCustom = null;
		//将items的属性值拷贝到itemsCustom
		if(items!=null){
			itemsCustom = new ItemsCustom();
			BeanUtils.copyProperties(items, itemsCustom);
		}
		
		
		return itemsCustom;
		
	}

	@Override
	public void updateItems(Integer id, ItemsCustom itemsCustom) throws Exception {
		//添加业务校验，通常在service接口对关键参数进行校验
		//校验 id是否为空，如果为空抛出异常
		
		//更新商品信息使用updateByPrimaryKeyWithBLOBs根据id更新items表中所有字段，包括 大文本类型字段
		//updateByPrimaryKeyWithBLOBs要求必须转入id
		itemsCustom.setId(id);
		itemsDao.updateItems(itemsCustom);
	}

}
