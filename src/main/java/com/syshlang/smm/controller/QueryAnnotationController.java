package com.syshlang.smm.controller;

import com.syshlang.smm.exception.CustomException;
import com.syshlang.smm.pojo.ItemsCustom;
import com.syshlang.smm.pojo.ItemsQueryVo;
import com.syshlang.smm.service.api.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * Created by sunys on 2017/7/5 22:53.
 * Description: 使用注解映射器和适配器
 */


@Controller  //标识该类是一个控制器
@RequestMapping("items")
public class QueryAnnotationController {

    @Autowired
    private ItemsService itemsService;
    @RequestMapping("/Query")
    public ModelAndView QueryController(){
        //调用service层，查询数据库
        List<ItemsCustom> list = null;
        try {
            ItemsCustom itemsCustom = new ItemsCustom();
            itemsCustom.setName("机");
            ItemsQueryVo itemsQueryVo  = new ItemsQueryVo();
            itemsQueryVo.setItemsCustom(itemsCustom);
            list = itemsService.findItemsList(itemsQueryVo);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回ModelAndView
        ModelAndView modelAndView = new ModelAndView();
        //相当于request的setAttribut,在jsp页面通过list取值
        modelAndView.addObject("listpojo",list);
        modelAndView.addObject("des","使用org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping注解映射器<br/>org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter注解适配器开发");
        modelAndView.setViewName("/view/query.jsp");
        return modelAndView;
    }
}
