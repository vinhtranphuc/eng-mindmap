package com.tranphucvinh.config.store;

import java.io.IOException;
import java.util.List;

import io.minio.GetObjectResponse;
import io.minio.messages.Bucket;

public interface MinioService {

    /**
     * check Whether bucket already exists
     * @param bucketName
     * @return
     */
    boolean bucketExists(String bucketName);

    /**
     * create a bucket
     * @param bucketName
     */
    void makeBucket(String bucketName);

    /**
     * list all bucket names
     * @return
     */
    List<String> listBucketName();

    /**
     * list all buckets
     * @return
     */
    List<Bucket> listBuckets();

    /**
     * delete Bucket by Name
     * @param bucketName
     * @return
     */
    boolean removeBucket(String bucketName);

    /**
     * list all object names in the bucket
     * @param bucketName
     * @return
     */
    List<String> listObjectNames(String bucketName);

    /**
     * upload files in the bucket
     * @param bucketName
     * @param fileRequest
     * @return
     */
    FileInfo putObject(String bucketName, FileRequest fileRequest);

    /**
     * put list object in the bucket
     * @param bucketName
     * @param fileRequestList
     * @return
     * @throws IOException
     */
    List<FileInfo> putListObject(String bucketName, List<FileRequest> fileRequestList) throws IOException;

    /**
     * download file from bucket
     * @param bucketName
     * @param objectName
     * @return
     */
    GetObjectResponse downloadObject(String bucketName, String objectName);

    /**
     * delete file in bucket
     * @param bucketName
     * @param objectName
     * @return
     */
    boolean removeObject(String bucketName, String objectName);

    /**
     * delete files in bucket
     * @param bucketName
     * @param objectNameList
     * @return
     */
    boolean removeListObject(String bucketName, List<String> objectNameList);

    /**
     * get file path from bucket
     * @param bucketName
     * @param objectName
     * @return
     */
    String getObjectUrl(String bucketName,String objectName);
}
