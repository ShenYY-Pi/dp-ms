package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.TbBlog;
import generator.service.TbBlogService;
import generator.mapper.TbBlogMapper;
import org.springframework.stereotype.Service;

/**
* @author PYY
* @description 针对表【tb_blog】的数据库操作Service实现
* @createDate 2024-08-23 19:13:03
*/
@Service
public class TbBlogServiceImpl extends ServiceImpl<TbBlogMapper, TbBlog>
    implements TbBlogService{

}




