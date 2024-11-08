package com.feed02.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feed02.article.mapper.ApArticleContentMapper;
import com.feed02.article.service.IApArticleContentService;
import com.feed02.model.article.pojos.ApArticleContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ApArticleServiceContentImpl extends ServiceImpl<ApArticleContentMapper , ApArticleContent> implements IApArticleContentService {
}
