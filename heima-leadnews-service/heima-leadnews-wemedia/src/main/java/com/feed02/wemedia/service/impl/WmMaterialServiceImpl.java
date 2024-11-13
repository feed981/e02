package com.feed02.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feed02.file.service.FileStorageService;
import com.feed02.model.common.dtos.PageResponseResult;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.common.enums.AppHttpCodeEnum;
import com.feed02.model.wemedia.dtos.WmMaterialDto;
import com.feed02.model.wemedia.pojos.WmMaterial;
import com.feed02.utils.thread.WmThreadLocalUtil;
import com.feed02.wemedia.mapper.WmMaterialMapper;
import com.feed02.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper , WmMaterial> implements WmMaterialService {
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        //1.检查参数
        if(multipartFile == null || multipartFile.getSize() == 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.上传图片到minIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        // 后缀:副档名
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadHtmlFile("",
                    fileName + postfix,
                    multipartFile.getInputStream(),
                    "image/jpeg");
            log.info("上传图片到MinIO中，fileId:{}",fileId);
        } catch (IOException e) {
            log.error("WmMaterialServiceImpl-上传图片失败");
            throw new RuntimeException(e);
        }

        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileId);
        wmMaterial.setIsCollection((short)0);
        wmMaterial.setType((short)0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);

        //4.返回结果
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(WmMaterialDto dto) {

        // 1.检查参数
        dto.checkParam();

        // 2.分页查询
        IPage page = new Page(dto.getPage(), dto.getSize());
        QueryWrapper<WmMaterial> queryWrapper = new QueryWrapper<>();
//        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 是否收藏 1: 收藏
        if(dto.getIsCollection() != null && dto.getIsCollection() == 1){
//            lambdaQueryWrapper.eq(WmMaterial::getIsCollection ,dto.getIsCollection());
            queryWrapper.eq("is_collection" ,dto.getIsCollection());
        }

        // 按用户查询
//        lambdaQueryWrapper.eq(WmMaterial::getUserId ,WmThreadLocalUtil.getUser().getId());
        queryWrapper.eq("user_id" ,WmThreadLocalUtil.getUser().getId());

        // 时间倒叙
//        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        queryWrapper.orderByDesc("created_time");

        page = page(page ,queryWrapper);

        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }
}
