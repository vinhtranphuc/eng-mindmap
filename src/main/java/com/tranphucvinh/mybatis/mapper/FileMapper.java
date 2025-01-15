package com.tranphucvinh.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tranphucvinh.config.util.GroupRefer;
import com.tranphucvinh.constant.GrpRefTableEnum;
import com.tranphucvinh.mybatis.model.FilePrmVO;
import com.tranphucvinh.mybatis.model.FileVO;


public interface FileMapper {

    int selectReferCnt(GroupRefer grpRef);

    void saveFile(FilePrmVO filePrm);

    FileVO selectFile(@Param("fileName") String fileName, @Param("fileId") String fileId, @Param("aboutDeleted") Boolean aboutDeleted);

    int selectFilesWithoutGrpRefCnt(
            @Param("fileIds") String[] fileIds,
            @Param("grpRefTable") GrpRefTableEnum grpRefTable,
            @Param("grpRefType") GrpRefTableEnum.RefTypeEnum grpRefType,
            @Param("grpRefPrimaryId") Long grpRefPrimaryId
            );

    List<FileVO> selectFilesByFileNames(@Param("fileNames") List<String> fileNames);

    List<FileVO> selectGroupFiles(GroupRefer prm);

    void deleteFile(@Param("accountId") Long accountId,@Param("fileId") String fileId);
}
