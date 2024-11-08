package com.feed02.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feed02.model.article.pojos.ApArticleContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApArticleContentMapper extends BaseMapper<ApArticleContent> {
}
