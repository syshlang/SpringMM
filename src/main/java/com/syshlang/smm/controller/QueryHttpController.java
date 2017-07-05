package com.syshlang.smm.controller;

import com.syshlang.smm.pojo.QueryPojo;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunys on 2017/7/1 12:39.
 * Description: HttpRequestHandlerAdapter 是http请求处理适配器
 *              使用该处理器适配器实现HttpRequestHandler接口
 */
public class QueryHttpController implements HttpRequestHandler{
    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        //调用service层，查询数据库
        List<QueryPojo> list = new ArrayList<QueryPojo>();
        QueryPojo pojo = new QueryPojo();
        pojo.setId("1");
        pojo.setName("aaaaaa");
        list.add(pojo);
        httpServletRequest.setAttribute("listpojo",list);
        httpServletRequest.setAttribute("des","HttpRequestHandlerAdapter 是http请求处理适配器,使用该处理器适配器实现HttpRequestHandler接口");
        httpServletRequest.getRequestDispatcher("/view/query.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
