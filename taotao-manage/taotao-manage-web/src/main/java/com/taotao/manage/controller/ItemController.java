package com.taotao.manage.controller;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;
import com.taotao.manage.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("item")
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDescService itemDescService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc")String desc,@RequestParam("itemParams")String itemParams){
        try {
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("新增商品 Item={},Desc={}",item,desc);
            }
            if (StringUtils.isEmpty(item.getTitle())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            //保存商品
            Boolean bool = this.itemService.saveItem(item,desc,itemParams);
            //保存失败
            if (!bool){
                if (LOGGER.isInfoEnabled()){
                    LOGGER.info("新增商品失败 Item={}}",item);
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("新增商品成功 ItemId={}",item.getId());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
          LOGGER.error("新增商品出错了 Item ="+item,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    /*
    * 查询商品列表
    * */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                      @RequestParam(value = "rows",defaultValue = "30")Integer rows){
        try {
            return ResponseEntity.ok(this.itemService.queryItemList(page,rows));
        } catch (Exception e) {
            LOGGER.error("新增商品出错了 page ="+page,"rows ="+rows,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item,@RequestParam("desc")String desc,@RequestParam("itemParams")String itemparams){
        try {
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("编辑商品 Item={},Desc={}",item,desc);
            }
            if (StringUtils.isEmpty(item.getTitle())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            //编辑商品
            Boolean bool = this.itemService.updateItem(item,desc,itemparams);
            //编辑失败
            if (!bool){
                if (LOGGER.isInfoEnabled()){
                    LOGGER.info("编辑商品失败 Item={}}",item);
                }
               // 500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("编辑商品成功 ItemId={}",item.getId());
            }
            //204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error("编辑商品出错了 Item ="+item,e);
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
