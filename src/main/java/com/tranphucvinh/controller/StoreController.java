package com.tranphucvinh.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranphucvinh.config.store.FileInfo;
import com.tranphucvinh.config.store.FileRequest;
import com.tranphucvinh.config.store.FileResponse;
import com.tranphucvinh.config.store.FilesWrapper;
import com.tranphucvinh.config.util.GroupRefer;
import com.tranphucvinh.mybatis.model.FileVO;
import com.tranphucvinh.mybatis.model.GroupReferVO;
import com.tranphucvinh.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

//    @Autowired
    private StoreService storeService;

    /**
     * get list file info by group refer
     * @param groupRefer
     * @return
     */
    @GetMapping("/group/files")
    public ResponseEntity<List<FileVO>> getGroupFiles(@ParameterObject @ModelAttribute GroupRefer groupRefer) {
        List<FileVO> result = storeService.getGroupFiles(groupRefer);
        return ResponseEntity.ok(result);
    }

    /**
     * Upload a file
     * + only upload to Minio server, not save to DB
     * @param FileRequest.mutipartFile : file data
     * @param FileRequest.replaceFileId : unrequired, if null fileId auto create with uuid
     * @return FileInfo : file information has generated from file upload
     */
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<FileInfo> uploadFile(@ModelAttribute FileRequest fileRequest) {
        FileInfo fileInfo = storeService.uploadFile(fileRequest);
        return ResponseEntity.ok(fileInfo);
    }

    /**
     * Upload a file referent to data in table
     * + upload a file to Minio server & save data to DB
     * @param FileRequest.mutipartFile : file data
     * @param FileRequest.replaceFileId : unrequired, if null fileId auto create with uuid
     * @param GroupReferVO.grpRefTable : table name
     * @param GroupReferVO.grpRefPrimaryId : primary id of table
     * @param GroupReferVO.grpRefType : type or column name
     * @param GroupReferVO.grpSeq : position of file in group, if null it's will + 1
     * @return FileInfo : file information has generated from file upload
     */
    @PostMapping(value = "/uploadRefer", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<FileVO> uploadRef(
        @io.swagger.v3.oas.annotations.parameters.RequestBody @ModelAttribute FileRequest fileRequest,
        @Valid @ParameterObject @ModelAttribute GroupReferVO grpRefPrm) {
        FileVO result = storeService.uploadFileRefer(grpRefPrm, fileRequest);
        return ResponseEntity.ok(result);
    }

    /**
     * Upload list file
     * + only upload to Minio server, not save to DB
     * + not work on swagger
     * Request Ex :
     * curl --location 'http://127.0.0.1:8082/store/uploadList' \
        --form 'files[0].mutipartFile=@"[file 1 directory]"' \
        --form 'files[0].fileName="file1.jpg"' \
        --form 'files[1].mutipartFile=@"[file 2 directory]' \
        --form 'files[1].fileName="file2.jpg"'
     * @param FileRequest.mutipartFile : file data
     * @param FileRequest.replaceFileId : unrequired, if null fileId auto create with uuid
     * @return List<FileInfo> : List of file info has generated from files upload
     * @throws IOException
     */
    @PostMapping(value = "/uploadList", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<FileInfo>> uploadList(
            @io.swagger.v3.oas.annotations.parameters.RequestBody
            @ModelAttribute FilesWrapper files) throws IOException {
        List<FileInfo> result = storeService.uploadListFile(files.getFiles());
        return ResponseEntity.ok(result);
    }

    /**
     * Upload list file referent to data in table
     * + upload files to Minio server & save data to DB
     * + not work on swagger
     * + note : don't request GroupReferVO.grpSeq, it's will auto increase for each files
     * Ex :
     * curl --location 'localhost:8082/store/uploadListRefer' \
        --form 'files[0].mutipartFile=@"[file 1 directory]"' \
        --form 'files[0].fileName="file1.jpg"' \
        --form 'files[1.mutipartFile=@"[file 2 directory]"' \
        --form 'files[1].fileName="file2.jpg"' \
        --form 'grpRefTable="tb_example"' \
        --form 'grpRefPrimaryId="1"' \
        --form 'grpRefType="example_type"'
     * @param FileRequest.mutipartFile : file data
     * @param FileRequest.replaceFileId : unrequired, if null fileId auto create with uuid
     * @param GroupReferVO.grpRefTable : table name
     * @param GroupReferVO.grpRefPrimaryId : primary id of table
     * @param GroupReferVO.grpRefType : type or column name
     * @param GroupReferVO.grpRefType : unused
     * @return List<FileVO> : List of file info has generated from files upload
     * @throws IOException
     */
    @PostMapping(value = "/uploadListRefer", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<List<FileVO>> uploadListRefer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody
            @ModelAttribute FilesWrapper files,
            @ParameterObject @ModelAttribute GroupRefer grpRefPrm) throws IOException {
        List<FileVO> result = storeService.uploadListFileRefer(grpRefPrm, files.getFiles());
        return ResponseEntity.ok(result);
    }

    /**
     * Download a file by fileName
     * @param response
     * @param fileName : store in Minio Server & DB
     * @return byte[]
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> download(HttpServletResponse response, @PathVariable("fileName") String fileName) throws UnsupportedEncodingException, IOException {
        FileResponse fileRes = storeService.download(fileName);
        return ResponseEntity.ok()
             .header("Content-type", fileRes.getContentType())
             .header("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"))
             .body(fileRes.getResource());
    }

    /**
     * Delete a file by fileName
     * @param fileName : store in Minio Server & DB
     * @return Void
     */
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<FileVO> deleteFile(@PathVariable("fileName") String fileName) {
        FileVO fileDeleted = storeService.deleteFile(fileName, true);
        return ResponseEntity.ok().body(fileDeleted);
    }

    /**
     * Delete files by list of fileName
     * @param fileNames : store in Minio Server & DB
     * @return Void
     */
    @DeleteMapping("/deleteFiles")
    public ResponseEntity<List<FileVO>> deleteFiles(@RequestBody List<String> fileNames) {
        List<FileVO> result = storeService.deleteFiles(fileNames, false);
        return ResponseEntity.ok().body(result);
    }
}
