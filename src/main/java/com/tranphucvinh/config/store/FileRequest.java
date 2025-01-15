package com.tranphucvinh.config.store;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRequest {
    @Parameter
    private MultipartFile mutipartFile;
    private String replaceFileId; // unrequired, if null fileId auto create with UUID

    @JsonIgnore
    private String fileName;
}
