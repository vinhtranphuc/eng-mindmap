package com.tranphucvinh.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tranphucvinh.constant.GrpRefTableEnum;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MarkFileGroup {

    GrpRefTableEnum grpRefTable();

    GrpRefTableEnum.RefTypeEnum grpRefType();

    String primaryIdFieldName() default "id";
}
