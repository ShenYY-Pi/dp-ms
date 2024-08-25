package com.syy.dpms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.syy.dpms.dto.Result;
import com.syy.dpms.entity.Blog;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface BlogService extends IService<Blog> {
    Result queryHotBlog(Integer current);

    Result queryBlogById(Long id);

    Result likeBlog(Long id);

    Result queryBlogLikes(Long id);

    Result saveBlog(Blog blog);

    Result queryBlogOfFollow(Long max, Integer offset);
}
