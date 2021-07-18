package com.hly.july.biz.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hly.july.common.core.constant.FileStatusEnum;
import com.hly.july.common.core.constant.FileTypeEnum;
import com.hly.july.common.core.exception.ServiceInternalException;
import com.hly.july.common.core.result.ResultCode;
import com.hly.july.common.core.util.DateUtils;
import com.hly.july.common.db.entity.FileHelper;
import com.hly.july.common.db.entity.User;
import com.hly.july.common.db.mapper.FileHelperMapper;
import com.hly.july.common.db.service.IFileHelperService;
import com.hly.july.common.web.util.FileHelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @ClassName FileHelperServiceImpl
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/7/16 16:48
 * @Version 1.0.0
 **/
@Slf4j
@Service
public class FileHelperServiceImpl extends ServiceImpl<FileHelperMapper, FileHelper> implements IFileHelperService {

    @Resource
    private FileHelperUtil fileHelperUtil;

    @Value("${file.upload.videoPath}")
    private String videoSavePath;

    @Value("${file.upload.audioPath}")
    private String audioSavePath;

    @Value("${file.upload.imagePath}")
    private String imageSavePath;

    public FileHelper saveMultipartFile(MultipartFile file,String uploaderId,Integer fileType) throws ServiceInternalException {
        if(file!=null&&StringUtils.isNotEmpty(uploaderId)&&fileType!=null&&FileTypeEnum.getAllTypeCodeList().contains(fileType)){
            String md5 = fileHelperUtil.getMd5ByApache(file);
            FileHelper sameMD5File = getSameMD5File(md5);
            if(sameMD5File!=null){
                log.info("saveFile ,found same md5 file already exists, exists:{}",sameMD5File);
                return sameMD5File;
            }
            String fileId = IdWorker.getIdStr();
            String fileName = file.getOriginalFilename();//获取文件名加后缀
            String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1);//文件后缀
            FileHelper fileHelper = new FileHelper();
            fileHelper.setFileId(fileId);
            fileHelper.setFileName(fileId);
            fileHelper.setFileSuffix(fileSuffix.toLowerCase());
            fileHelper.setUploaderId(uploaderId);
            fileHelper.setType(fileType);
            fileHelper.setFileMd5(md5);
            fileHelper.setStatus(FileStatusEnum.IN_PROCESS.getCode());
            fileHelper.setGmtCreate(DateUtils.getCurrentDateTime());
            fileHelper.setGmtUpdate(DateUtils.getCurrentDateTime());
            fileHelper.setFileSize(0L);
            String midPath = DateUtils.getCurrentTime("yyyy-MM-dd");
            String endPath = fileId+"."+fileSuffix;
            String subPath = Paths.get(midPath,endPath).toString();
            fileHelper.setSavePath(subPath);
            log.info("saveFile uploaderId:{}, subPath:{}",uploaderId,subPath);
            Path fullPath;
            Path dirPath;
            if(FileTypeEnum.VIDEO.getCode().equals(fileType)){
                fullPath = Paths.get(videoSavePath, subPath);
                dirPath = Paths.get(videoSavePath, midPath);
            }else if(FileTypeEnum.AUDIO.getCode().equals(fileType)){
                fullPath = Paths.get(audioSavePath, subPath);
                dirPath = Paths.get(videoSavePath, midPath);
            }else if(FileTypeEnum.IMAGE.getCode().equals(fileType)){
                fullPath = Paths.get(imageSavePath, subPath);
                dirPath = Paths.get(videoSavePath, midPath);
            }else{
                throw new ServiceInternalException(ResultCode.FILE_TYPE_ERROR);
            }
            // 判断该路径是否存在
            File saveFile = fullPath.toFile();
            File dirFile = dirPath.toFile();
            log.info("saveFile uploaderId:{}, fullPath:{},dirFile:{}",uploaderId,saveFile.toPath().toAbsolutePath().toString(),dirFile.toPath().toAbsolutePath().toString());
            if (!dirFile.exists()) {
                // 创建该文件夹
                dirFile.mkdirs();
            }
            // 先保存IN_process进度
            saveOrUpdate(fileHelper);

            try {
                file.transferTo(saveFile);
            }catch (IOException e){
                e.printStackTrace();
                throw new ServiceInternalException(ResultCode.FILE_SAVE_ERROR);
            }
            fileHelper.setGmtUpdate(DateUtils.getCurrentDateTime());
            fileHelper.setFileSize(saveFile.length()); //单位B
            fileHelper.setStatus(FileStatusEnum.PUBLIC.getCode());
            saveOrUpdate(fileHelper);
            return fileHelper;

        }else{
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
    }

    public FileHelper getSameMD5File(String md5) throws ServiceInternalException{
        if(StringUtils.isNotEmpty(md5)){
            QueryWrapper<FileHelper> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("file_md5", md5);
            List<FileHelper> fileHelpers = baseMapper.selectList(queryWrapper);

            for (FileHelper fileHelper : fileHelpers) {
                if(FileStatusEnum.getAllInvisibleCodeList().contains(fileHelper.getStatus())){
                    if(isExistsByPath(fileHelper.getSavePath(),fileHelper.getType())){
                        return fileHelper;
                    }else{
                        // 数据库已有相同MD5的文件，但是数据库的状态不对(已经找不到此文件)
                        FileHelper updater = new FileHelper();
                        updater.setFileId(fileHelper.getFileId());
                        updater.setStatus(FileStatusEnum.NOTFOUND.getCode());
                        updater.setGmtUpdate(DateUtils.getCurrentDateTime());
                        baseMapper.updateById(updater);
                    }
                }
            }
            return null;
        }else{
            throw new ServiceInternalException(ResultCode.FILE_MD5_ERROR);
        }
    }

    public boolean isExistsByPath(String path,Integer fileType) throws ServiceInternalException{
        Path fullPath;
        if(FileTypeEnum.VIDEO.getCode().equals(fileType)){
            fullPath = Paths.get(videoSavePath, path);
        }else if(FileTypeEnum.AUDIO.getCode().equals(fileType)){
            fullPath = Paths.get(audioSavePath, path);
        }else if(FileTypeEnum.IMAGE.getCode().equals(fileType)){
            fullPath = Paths.get(imageSavePath, path);
        }else{
            throw new ServiceInternalException(ResultCode.FILE_TYPE_ERROR);
        }
        log.info("isExistsByPath fullPath:{}",fullPath.toString());
        if(fullPath!=null){
            File file_path = fullPath.toFile();
            if(file_path.exists()){
                return true;
            }else{
                return false;
            }
        }else{
            throw new ServiceInternalException(ResultCode.FILE_PATH_ERROR);
        }

    }

    public File getFileByPath(String path,Integer fileType) throws ServiceInternalException{
        Path fullPath;
        if(FileTypeEnum.VIDEO.getCode().equals(fileType)){
            fullPath = Paths.get(videoSavePath, path);
        }else if(FileTypeEnum.AUDIO.getCode().equals(fileType)){
            fullPath = Paths.get(audioSavePath, path);
        }else if(FileTypeEnum.IMAGE.getCode().equals(fileType)){
            fullPath = Paths.get(imageSavePath, path);
        }else{
            throw new ServiceInternalException(ResultCode.FILE_TYPE_ERROR);
        }
        log.info("getFile fullPath:{}",fullPath.toString());
        if(fullPath!=null){
            File file_path = fullPath.toFile();
            if(file_path.exists()){
                return file_path;
            }else{
                return null;
            }
        }else{
            throw new ServiceInternalException(ResultCode.FILE_PATH_ERROR);
        }

    }

    public File fetchFileByFileId(String fileId,List<Integer> fileStatusList) throws ServiceInternalException{
        if(ObjectUtils.isEmpty(fileId)){
            log.info("fetchFileByFileId fileId is null ");
            throw new ServiceInternalException(ResultCode.API_VALIDATION_ERROR);
        }
        FileHelper helper = getById(fileId);
        if(helper==null){
            log.info("fetchFileByFileId file is null fileId:{}",fileId);
            throw new ServiceInternalException(ResultCode.FILE_NOT_FOUND);
        }
        if(!fileStatusList.contains(helper.getStatus())){
            log.info("fetchFileByFileId file status limit, helper:{}",helper.toString());
            throw new ServiceInternalException(ResultCode.FILE_NOT_FOUND);
        }
        if(!isExistsByPath(helper.getSavePath(),helper.getType())){
            // 数据库的状态不对(已经找不到此文件)
            FileHelper updater = new FileHelper();
            updater.setFileId(helper.getFileId());
            updater.setStatus(FileStatusEnum.NOTFOUND.getCode());
            updater.setGmtUpdate(DateUtils.getCurrentDateTime());
            baseMapper.updateById(updater);
            log.info("fetchFileByFileId file not found, helper:{}",helper.toString());
            throw new ServiceInternalException(ResultCode.FILE_NOT_FOUND);
        }else{
            File videoFile = getFileByPath(helper.getSavePath(),helper.getType());
            return videoFile;
        }
    }

}
