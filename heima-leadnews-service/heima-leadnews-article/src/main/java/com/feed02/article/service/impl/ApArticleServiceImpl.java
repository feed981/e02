package com.feed02.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feed02.article.mapper.ApArticleMapper;
import com.feed02.article.service.IApArticleService;
import com.feed02.common.constants.ArticleConstants;
import com.feed02.model.article.dtos.ArticleHomeDto;
import com.feed02.model.article.pojos.ApArticle;
import com.feed02.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper , ApArticle> implements IApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    private final static short MAX_PAGE_SIZE = 50;
    /**
     * 加载文章列表
     * @param dto
     * @param type 1 加载更多 2 加载最新
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        // 1. 效验参数
        // 效验 分页条数
        Integer size = dto.getSize();
        if(size == null || size == 0){
            size = 10;
        }

        // 分页不超过50
        size = Math.min(size, MAX_PAGE_SIZE);

        // 效验 type
        if(!type.equals(ArticleConstants.LOADTYPE_LOAD_MORE)
                && !type.equals(ArticleConstants.LOADTYPE_LOAD_NEW) ){
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        // 效验 频道
        if(StringUtils.isBlank(dto.getTag())) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        // 效验 时间
        if(dto.getMaxBehotTime() == null){
            dto.setMaxBehotTime(new Date());
        }

        if(dto.getMinBehotTime() == null){
            dto.setMinBehotTime(new Date());
        }

        // 2. 查询
        List<ApArticle> apArticles = apArticleMapper.loadArticleList(dto, type);
        // 3. 返回
        return ResponseResult.okResult(apArticles);
    }

}
