package com.tranphucvinh.constant;

import lombok.Getter;

public enum MessageCodeEnum {

    /* System */
    SYS_PAGINATION_001("Page or pageSize illegal")
    ,SYS_FILE_001("File cannot be Upload")
    ,SYS_FILE_002("Delete file [%s] fail !")
    ,SYS_FILE_003("Table %s not exists id = %s")
    ,SYS_FILE_004("file.id = %s not exists from refTableName = %s & refTableId = %s")
    ,SYS_FILE_005("Request upload refer invalid")
    ,SYS_FILE_006("fileType & documentType must be requried !")
    /* End System */

    /* BUSINESS */
    ,LOGIN_001("You entered an incorrect ID or password. Please check the information you entered again.");
    /* End BUSINESS */

    MessageCodeEnum(String message) {
        this.message = message;
    }

    @Getter
    private String message;
}
