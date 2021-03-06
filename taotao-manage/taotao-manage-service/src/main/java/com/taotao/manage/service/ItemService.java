package com.taotao.manage.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService extends BaseService<Item> {
    @Autowired
    private ItemDescService itemDescService;
    @Autowired
    private ItemMapper itemMapper;
    private ItemParamItemService itemParamItemService;
    /*保存商品描述
      * */
    public Boolean saveItem(Item item, String desc,String itemParams) {
        item.setStatus(1);
        item.setId(null);
       Integer count1 = super.save(item);
        //保存商品描述
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = this.itemDescService.save(itemDesc);
        //保存规格参数
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        Integer count3 = this.itemParamItemService.save( itemParamItem);
        return count1.intValue()==1&&count2.intValue()==1&&count3.intValue()==1;
    }

    public EasyUIResult queryItemList(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        Example example = new Example(Item.class);
        //根据创建时间倒序排序
        example.setOrderByClause("created DESC");

        List<Item> items = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(items);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }

    public Boolean updateItem(Item item, String desc,String itemParams) {
        item.setStatus(null);
        Integer count1 = super.updateSelective(item);
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = this.itemDescService.updateSelective(itemDesc);

        Integer count3 = this.itemParamItemService.updateItemParamItem(item.getId(), itemParams);
        return count1.intValue()==1&&count2.intValue()==1&&count3.intValue()==1;
    }
}
