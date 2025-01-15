package com.tranphucvinh.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tranphucvinh.config.security.AuthUtil;
import com.tranphucvinh.config.store.FileException;
import com.tranphucvinh.config.store.FileInfo;
import com.tranphucvinh.config.store.FileRequest;
import com.tranphucvinh.config.store.FileResponse;
import com.tranphucvinh.config.store.MinioConfig;
import com.tranphucvinh.config.store.MinioService;
import com.tranphucvinh.config.util.GroupRefer;
import com.tranphucvinh.mybatis.mapper.FileMapper;
import com.tranphucvinh.mybatis.model.FilePrmVO;
import com.tranphucvinh.mybatis.model.FileVO;

import io.minio.GetObjectResponse;
import lombok.RequiredArgsConstructor;

//@Service
@RequiredArgsConstructor
public class StoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreService.class);

    private final MinioService minioService;
    private final MinioConfig minioConfig;

    @Resource
    private FileMapper fileMapper;

    /**
     * upload file into minio server
     * @param fileRequest
     * @return
     */
    public FileInfo uploadFile(FileRequest fileRequest) {
        return minioService.putObject(minioConfig.getBucketName(),fileRequest);
    }

    /**
     * upload list file into Minio server
     * @param files
     * @return
     * @throws IOException
     */
    public List<FileInfo> uploadListFile(List<FileRequest> files) throws IOException {
        return minioService.putListObject(minioConfig.getBucketName(), files);
    }

    /**
     * download file exists in minio server
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public FileResponse download(String fileName) throws UnsupportedEncodingException, IOException {
        GetObjectResponse in = null;
        try {
            in = minioService.downloadObject(minioConfig.getBucketName(), fileName);
            return new FileResponse(IOUtils.toByteArray(in), in.headers().get("Content-Type"));
        }  finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.info("StoreService | download | IOException : " + e.getMessage());
                }
            }
        }
    }

    /**
     * delete file from minio server
     * @param fileName
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FileVO deleteFile(String fileId, Boolean checkDeleted) {
        FileVO file = fileMapper.selectFile(null, fileId, true);
        if(Objects.isNull(file)) {
            throw new FileException("Not exists file Id: "+fileId);
        }
        if(checkDeleted && Objects.nonNull(file.getDeletedDt())) {
            throw new FileException("File already deleted before !");
        }

        boolean state =  minioService.removeObject(minioConfig.getBucketName(), file.getFileName());
        if(!state){
            throw new FileException("Delete file fail !");
        }

        // delete from DB
        Long accountId = AuthUtil.crrAcc().getId();
        fileMapper.deleteFile(accountId, file.getId());

        return file;
    }

    /**
     * delete list file from minio server
     * @param fileNames
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<FileVO> deleteFiles(List<String> fileIds, Boolean checkDeleted) {
        Long accountId = AuthUtil.crrAcc().getId();
        List<FileVO> deletedFiles = new ArrayList<>();
        for(String fileId:fileIds) {
            FileVO file = fileMapper.selectFile(null, fileId, true);
            if(Objects.isNull(file)) {
                throw new FileException("Not exists fileId : "+fileId);
            }
            if(checkDeleted && Objects.nonNull(file.getDeletedDt())) {
                throw new FileException("FileId ["+fileId+"] already deleted before !");
            }

            // delete from DB
            fileMapper.deleteFile(accountId, file.getId());
            deletedFiles.add(file);
        }

        List<String> fileNames = deletedFiles.stream().map(t -> t.getFileName()).collect(Collectors.toList());
        boolean state =  minioService.removeListObject(minioConfig.getBucketName(), fileNames) ;
        if(!state){
            throw new FileException("Delete files fail !");
        }

        return deletedFiles;
    }

    /**
     * upload file with group reference
     * @param grpRefPrm
     * @param fileRequest
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FileVO uploadFileRefer(GroupRefer grpRefPrm, FileRequest fileRequest) {
        // check group refer
        checkGrpRef(grpRefPrm);

        // precheck replace file id
        if(StringUtils.isNotEmpty(fileRequest.getReplaceFileId())
           && fileMapper.selectFilesWithoutGrpRefCnt(
                   new String[] {fileRequest.getReplaceFileId()},
                   grpRefPrm.getGrpRefTable(),
                   grpRefPrm.getGrpRefType(),
                   grpRefPrm.getGrpRefPrimaryId()
              ) > 0) {
            throw new FileException("Replace fileId exists in another group !");
        }

        // case delete file
        if(StringUtils.isNotEmpty(fileRequest.getReplaceFileId()) && Objects.isNull(fileRequest.getMutipartFile())) {
            deleteFile(fileRequest.getReplaceFileId(), false);
            return fileMapper.selectFile(null, fileRequest.getReplaceFileId(), true);
        }
        // upload to minio server
        FileInfo fileInfo = uploadFile(fileRequest);

        // save file
        saveFile(fileInfo, grpRefPrm);

        // return file
        return fileMapper.selectFile(fileInfo.getFileName(), null, false);
    }

    /**
     * upload list file with group reference
     * @param grpRefPrm
     * @param files
     * @return
     * @throws IOException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<FileVO> uploadListFileRefer(GroupRefer grpRefPrm, List<FileRequest> files) throws IOException {
        // check group refer
        checkGrpRef(grpRefPrm);

        // precheck replace file id
        String[] replaceFileIds = files.stream().filter(t -> StringUtils.isNotEmpty(t.getReplaceFileId())).map(t -> t.getReplaceFileId()).toArray(String[]::new);
        if(replaceFileIds.length > 0
           && fileMapper.selectFilesWithoutGrpRefCnt(
                   replaceFileIds,
                   grpRefPrm.getGrpRefTable(),
                   grpRefPrm.getGrpRefType(),
                   grpRefPrm.getGrpRefPrimaryId()
              ) > 0) {
            throw new FileException("Replace fileId exists in another group !");
        }

        // get delete files
        List<FileRequest> deleteFiles = new ArrayList<>();
        for(FileRequest t:new ArrayList<>(files)) {
            boolean isDelete = (StringUtils.isNotEmpty(t.getReplaceFileId()) && Objects.isNull(t.getMutipartFile()));
            if(isDelete) {
                deleteFiles.add(t);
                files.remove(t);
            }
        }
        if(deleteFiles.size() > 0) {
            List<String> deleteFileIds = deleteFiles.stream().map(t -> t.getReplaceFileId()).collect(Collectors.toList());
            deleteFiles(deleteFileIds, false);
        }

        // upload to minio server
        List<FileInfo> fileInfoList = uploadListFile(files);

        // save files
        for(FileInfo fileInfo: fileInfoList) {
            saveFile(fileInfo, grpRefPrm);
        }

        // return files
        List<String> fileNames = fileInfoList.stream().map(t -> t.getFileName()).collect(Collectors.toList());
        return fileMapper.selectFilesByFileNames(fileNames);
    }

    /**
     * check group reference
     * @param grpRefPrm
     */
    private void checkGrpRef(GroupRefer grpRefPrm) {
        if(!Arrays.asList(grpRefPrm.getGrpRefTable().grpRefTypes).contains(grpRefPrm.getGrpRefType())) {
            throw new FileException(grpRefPrm.getGrpRefTable().name()+" not contains "+grpRefPrm.getGrpRefType().name());
        }
        int refCnt = fileMapper.selectReferCnt(grpRefPrm);
        if(refCnt < 1) {
            throw new FileException("Primary id = "+grpRefPrm.getGrpRefPrimaryId()+" of reference table "+grpRefPrm.getGrpRefTable().name()+" not exists !");
        }
    }

    /**
     * save file infomation
     * @param fileInfo
     * @param grpRefPrm
     */
    private void saveFile(FileInfo fileInfo, GroupRefer grpRefPrm) {
        Long accountId = AuthUtil.crrAcc().getId();
        FilePrmVO filePrm = new FilePrmVO(fileInfo, grpRefPrm, accountId);
        fileMapper.saveFile(filePrm);
    }

    public List<FileVO> getGroupFiles(GroupRefer groupRefer) {
        return fileMapper.selectGroupFiles(groupRefer);
    }
}
