package com.tranphucvinh.mybatis.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class FileVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String fileName;
    private String grpRefTable;
    private Long grpRefPrimaryId;
    private String grpRefType;
    private Integer grpSeq;
    private String rootName;
    private String contentType;
    private String extension;
    private String fileSize;
    private String imageDimensions;
    private Long createdId;
    private String createdDt;
    private Long updatedId;
    private String updatedDt;
    private Long deletedId;
    private String deletedDt;

    private boolean deleted = false;

    public void setDeletedId(Long deletedId) {
        this.deletedId = deletedId;
        if(Objects.nonNull(deletedId)) {
            this.setDeleted(true);
        }
    }
}
