package com.markerhub.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Code;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class BlogController {
    @Autowired
    BlogService blogService;

    @CrossOrigin
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){
        Page<Blog> page = new Page<>(currentPage,5);
        IPage<Blog> blogPage = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return new Result(200,blogPage,"查询成功！");
    }

    @CrossOrigin
    @GetMapping("/blogs/{id}")
    public Result detail(@PathVariable(name = "id") Long id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"该博客已删除");
        return new Result(200,blog,"查询成功！");
    }

    @SaCheckLogin
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){
        StpUtil.login(1);
        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            Assert.isTrue(temp.getUserId() == StpUtil.getLoginIdAsLong(), "没有权限编辑");

        } else {

            temp = new Blog();
            temp.setUserId(StpUtil.getLoginIdAsLong());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(temp);
        return new Result(200,blog,"修改成功");
    }


}
