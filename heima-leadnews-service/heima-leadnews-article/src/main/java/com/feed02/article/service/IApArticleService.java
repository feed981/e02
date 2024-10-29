package com.feed02.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feed02.model.article.dtos.ArticleHomeDto;
import com.feed02.model.article.pojos.ApArticle;
import com.feed02.model.common.dtos.ResponseResult;

public interface IApArticleService extends IService<ApArticle> {
    public ResponseResult load(ArticleHomeDto dto ,Short type);
}
