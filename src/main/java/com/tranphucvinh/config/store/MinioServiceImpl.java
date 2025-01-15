package com.tranphucvinh.config.store;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import io.minio.GetObjectResponse;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

//@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioUtil minioUtil;
    private final MinioConfig minioConfig;


    @Override
    public boolean bucketExists(String bucketName) {
        return minioUtil.bucketExists(bucketName);
    }

    @Override
    public void makeBucket(String bucketName) {
        minioUtil.makeBucket(bucketName);
    }

    @Override
    public List<String> listBucketName() {
        return minioUtil.listBucketNames();
    }

    @Override
    public List<Bucket> listBuckets() {
        return minioUtil.listBuckets();
    }

    @Override
    public boolean removeBucket(String bucketName) {
        return minioUtil.removeBucket(bucketName);
    }

    @Override
    public List<String> listObjectNames(String bucketName) {
        return minioUtil.listObjectNames(bucketName);
    }

    @SneakyThrows
    @Override
    public FileInfo putObject(String bucketName, FileRequest fileRequest) {
        // bucket name
        bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minioConfig.getBucketName();
        if (!this.bucketExists(bucketName)) {
            this.makeBucket(bucketName);
        }
        // create file info
        FileInfo fileInfo = createFileInfo(fileRequest);
        minioUtil.putObject(bucketName, fileRequest);
        return fileInfo;
    }

    @Override
    public List<FileInfo> putListObject(String bucketName, List<FileRequest> fileRequestList) throws IOException {
        List<FileInfo> fileInfoList = new ArrayList<>();
        for(FileRequest fileRequest:fileRequestList) {
            FileInfo fileInfo = createFileInfo(fileRequest);
            fileInfoList.add(fileInfo);

            minioUtil.putObject(bucketName, fileRequest);
        }
//        minioUtil.putListObject(bucketName, fileRequestList);
        return fileInfoList;
    }

    /**
     * create file information before save
     * @param fileRequest
     * @return
     * @throws IOException
     */
    private FileInfo createFileInfo(FileRequest fileRequest) throws IOException {

        String rootName = fileRequest.getMutipartFile().getOriginalFilename();
        Long fileSize = fileRequest.getMutipartFile().getSize();

        String extension = FileUtil.getExtensionByStringHandling(rootName).get();
        String fileId = StringUtils.isEmpty(fileRequest.getReplaceFileId())
                ? UUID.randomUUID().toString().replaceAll("-", "")
                : fileRequest.getReplaceFileId();

        String fileName = fileId+"."+extension;
        fileRequest.setFileName(fileName);

        LocalDateTime createdTime = LocalDateTime.now();

        String imageDimensions = null;
        if(FileUtil.isImage(extension)) {
            imageDimensions = FileUtil.getImageDimension(fileRequest.getMutipartFile());
        }

        return FileInfo.builder()
                .id(fileId)
                .rootName(rootName)
                .fileName(fileName)
                .extension(extension)
                .fileSize(fileSize)
                .createdTime(createdTime)
                .imageDimensions(imageDimensions)
                .build();
    }

    @Override
    public GetObjectResponse downloadObject(String bucketName, String objectName) {
        return minioUtil.getObject(bucketName,objectName);
    }

    @Override
    public boolean removeObject(String bucketName, String objectName) {
        return minioUtil.removeObject(bucketName, objectName);
    }

    @Override
    public boolean removeListObject(String bucketName, List<String> objectNameList) {
        return minioUtil.removeObject(bucketName,objectNameList);
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        return minioUtil.getObjectUrl(bucketName, objectName);
    }
}
