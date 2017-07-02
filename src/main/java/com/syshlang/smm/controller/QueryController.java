package com.syshlang.smm.controller;

import com.syshlang.smm.pojo.QueryPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunys on 2017/7/1 2:44.
 * Description: 实现Controller接口的处理器
 */
public class QueryController implements Controller{

    //添加一个日志器
    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        //调用service层，查询数据库
        List<QueryPojo> list = new ArrayList<QueryPojo>();
        QueryPojo pojo = new QueryPojo();
        pojo.setId("1");
        pojo.setName("aaaaaa");
        list.add(pojo);
        //返回ModelAndView
        ModelAndView modelAndView = new ModelAndView();
        //相当于request的setAttribut,在jsp页面通过list取值
        modelAndView.addObject("listpojo",list);
        modelAndView.addObject("des","实现Controller接口的处理");
        //指定视图View
        modelAndView.setViewName("/view/query.jsp");
        //输出日志文件
        logger.info("the first jsp pages");
        return modelAndView;
    }
}
