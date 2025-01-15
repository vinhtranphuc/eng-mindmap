package com.tranphucvinh.config.store;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {

    private byte[] resource;
    private String contentType;
}
