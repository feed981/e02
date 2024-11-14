package com.feed02.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feed02.common.constants.WemediaConstants;
import com.feed02.common.exception.CustomException;
import com.feed02.model.common.dtos.PageResponseResult;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.common.enums.AppHttpCodeEnum;
import com.feed02.model.wemedia.dtos.WmNewsDto;
import com.feed02.model.wemedia.dtos.WmNewsPageReqDto;
import com.feed02.model.wemedia.pojos.WmMaterial;
import com.feed02.model.wemedia.pojos.WmNews;
import com.feed02.model.wemedia.pojos.WmNewsMaterial;
import com.feed02.utils.thread.WmThreadLocalUtil;
import com.feed02.wemedia.mapper.WmMaterialMapper;
import com.feed02.wemedia.mapper.WmNewsMapper;
import com.feed02.wemedia.mapper.WmNewsMaterialMapper;
import com.feed02.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 新增、修改文章、保存草稿
    @Override
    public ResponseResult submitNews(WmNewsDto dto) {
        //0.条件判断
        if(dto == null && dto.getContent() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 1. 保存或修改文章
        WmNews wmNews = new WmNews();
        // 属性拷贝 属性名词和类型相同才能拷贝
        BeanUtils.copyProperties(dto ,wmNews);

        // 封面图片 list to string
        if(dto.getImages() != null && dto.getImages().size() > 0){
            // "images":[ "url1.jpg" ,"url2.jpg" ] -> url1.jpg,url2.jpg
            String imgStr = StringUtils.join(dto.getImages(), ",");
            wmNews.setImages(imgStr);
        }

        // 0 是无图  1 是单图  3 是多图  -1 是自动
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            wmNews.setType(null);
        }

        // 保存或修改
        saveOrUpdateWmNews(wmNews);

        // 2. 判断是否为草稿  如果为草稿结束当前方法
        // 草稿不会更动关联表 wm_news_material
        // NORMAL 0 草搞
        if(dto.getStatus().equals(WmNews.Status.NORMAL.getCode())){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        // 3. 不是草稿，保存文章内容图片与素材的关系
        List<String> materials = ectractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials,wmNews.getId());

        // 4. 不是草稿，保存文章封面图片与素材的关系，如果当前布局是自动，需要匹配封面图片
        saveRelativeInfoForCover(dto,wmNews,materials);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 第一个功能：如果当前封面类型为自动，则设置封面类型的数据
     * 匹配规则：
     * 1，如果内容图片大于等于1，小于3  单图  type 1
     * 2，如果内容图片大于等于3  多图  type 3
     * 3，如果内容没有图片，无图  type 0
     *
     * 第二个功能：保存封面图片与素材的关系
     * @param dto
     * @param wmNews
     * @param materials
     */
    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {
        List<String> images = dto.getImages();

        // 如果当前封面类型为自动，则设置封面类型的数据
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){

            // 多图
            if(materials.size() >= 3) {
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());

            // 单图
            }else if(materials.size() >= 1 && materials.size() < 3) {
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());

            // 无图
            }else{
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }

            // 修改文章
            if(images != null && images.size() > 0){
                wmNews.setImages(StringUtils.join(images ,","));
            }
            updateById(wmNews);
        }

        if(images != null && images.size() > 0){
            saveRelativeInfo(images,wmNews.getId() ,WemediaConstants.WM_COVER_REFERENCE);
        }
    }

    /**
     * 处理文章内容图片与素材的关系
     * @param materials
     * @param id
     */
    private void saveRelativeInfoForContent(List<String> materials, Integer id) {
        saveRelativeInfo(materials ,id ,WemediaConstants.WM_CONTENT_REFERENCE);
    }

    /**
     * 提取文章内容中的图片信息
     *  "content":"[
     *     {
     *         "type":"text",
     *         "value":"随着智能手机的..." 
     *     },
     *     {
     *         "type":"image",
     *         "value":"http://192.168.200.130/group1/M00/00/00/wKjIgl5swbGATaSAAAEPfZfx6Iw790.png" 
     *     }
     * ]
     * @param content
     */
    private List<String> ectractUrlInfo(String content) {
        List<String> meterials = new ArrayList<>();
        List<Map> maps = JSON.parseArray(content, Map.class);
        maps.forEach(map->{
            if(map.get("type").equals("image")){
                String value = (String) map.get("value");
                meterials.add(value);
            }
        });
        return meterials;
    }
    

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 保存或修改文章
     * @param wmNews
     */
    private void saveOrUpdateWmNews(WmNews wmNews) {
        //补全属性
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setPublishTime(new Date());
        wmNews.setEnable((short)1); // 默认上架

        if(wmNews.getId() == null){
            // 新增
            save(wmNews);
        }else{
            // 修改
            // 删除文章图片与素材的关系
            wmNewsMaterialMapper.delete(new QueryWrapper<WmNewsMaterial>().eq("news_id", wmNews.getId()));
            updateById(wmNews);
        }
    }
    
    @Autowired
    private WmMaterialMapper wmMaterialMapper; // 素材

    /**
     * 保存文章图片与素材的关系到数据库中
     * @param materials
     * @param newsId
     * @param type
     */
    private void saveRelativeInfo(List<String> materials, Integer newsId, Short type) {
        if(materials != null && !materials.isEmpty()){
            List<WmMaterial> dbMaterials = wmMaterialMapper.selectList(new QueryWrapper<WmMaterial>().in("url", materials));

            if(dbMaterials == null || dbMaterials.size() == 0){
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }
            if(materials.size() != dbMaterials.size()){
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }

            List<Integer> idList = dbMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());

            // 批量保存
            // 引用类型 0 内容引用 1 主图引用
            wmNewsMaterialMapper.saveRelations(idList ,newsId ,type);
        }
    }
}
