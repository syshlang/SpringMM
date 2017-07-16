package com.syshlang.smm.dao;

import com.syshlang.smm.mapper.ItemsMapper;
import com.syshlang.smm.mapper.ItemsMapperCustom;
import com.syshlang.smm.pojo.Items;
import com.syshlang.smm.pojo.ItemsCustom;
import com.syshlang.smm.pojo.ItemsQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sunys on 2017/7/16 2:55.
 * Description:
 */
@Repository
public class ItemsDao {
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Autowired
    private ItemsMapper itemsMapper;

    public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo)
            throws Exception {
        //通过ItemsMapperCustom查询数据库
        return itemsMapperCustom.findItemsList(itemsQueryVo);
    }

    public Items findItemsById(Integer id) throws Exception {

        return  itemsMapper.selectByPrimaryKey(id);
    }
    public void updateItems(ItemsCustom itemsCustom) throws Exception {
        itemsMapper.updateByPrimaryKeyWithBLOBs(itemsCustom);
    }
}
