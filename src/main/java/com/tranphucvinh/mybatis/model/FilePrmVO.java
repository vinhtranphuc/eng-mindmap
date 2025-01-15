package com.tranphucvinh.mybatis.model;

import com.tranphucvinh.config.store.FileInfo;
import com.tranphucvinh.config.util.GroupRefer;
import com.tranphucvinh.constant.GrpRefTableEnum;

import lombok.Data;

@Data
public class FilePrmVO {

    private Long accountId;
    private String id;
    private String fileName;
    private String rootName;
    private String extension;
    private Long fileSize;
    private String imageDimensions;

    private GrpRefTableEnum grpRefTable;
    private Long grpRefPrimaryId;
    private GrpRefTableEnum.RefTypeEnum grpRefType;
    private Integer grpSeq;

    public FilePrmVO(FileInfo fileInfo, GroupRefer grpRef, Long accountId) {
        this.id = fileInfo.getId();
        this.fileName = fileInfo.getFileName();
        this.rootName = fileInfo.getRootName();
        this.extension = fileInfo.getExtension();
        this.fileSize = fileInfo.getFileSize();
        this.imageDimensions = fileInfo.getImageDimensions();
        this.grpRefTable = grpRef.getGrpRefTable();
        this.grpRefPrimaryId = grpRef.getGrpRefPrimaryId();
        this.grpRefType = grpRef.getGrpRefType();
        this.accountId = accountId;
    }
}
