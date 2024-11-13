package com.feed02.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feed02.model.common.dtos.PageResponseResult;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.wemedia.dtos.WmNewsPageReqDto;
import com.feed02.model.wemedia.pojos.WmNews;
import com.feed02.utils.thread.WmThreadLocalUtil;
import com.feed02.wemedia.mapper.WmNewsMapper;
import com.feed02.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper , WmNews> implements WmNewsService {
    @Override
    public ResponseResult findList(WmNewsPageReqDto dto) {
        // 检查参数
        dto.checkParam();

        // 分页查询
        IPage page = new Page<>(dto.getPage(), dto.getSize());

        QueryWrapper queryWrapper = new QueryWrapper();

        // 状态查询
        if(dto.getStatus() != null){
            queryWrapper.eq("status" ,dto.getStatus());
        }

        // 频道查询
        if(dto.getChannelId() != null){
            queryWrapper.eq("channel_id" ,dto.getChannelId());
        }

        // 时间范围查询
        if(dto.getBeginPubDate() != null && dto.getEndPubDate() != null){
            queryWrapper.between("publish_time" ,dto.getBeginPubDate() ,dto.getEndPubDate());
        }

        // 关键字馍糊查询
        if(dto.getKeyword() != null){
            queryWrapper.like("title" ,dto.getKeyword());
        }

        // 查询当前登入人的文章
        queryWrapper.eq("user_id" , WmThreadLocalUtil.getUser().getId());

        // 按照发布时间倒叙查询
        queryWrapper.orderByDesc("publish_time");


        page = page(page ,queryWrapper);

        // 结果返回
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
