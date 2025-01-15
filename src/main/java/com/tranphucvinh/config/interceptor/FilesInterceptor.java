package com.tranphucvinh.config.interceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tranphucvinh.config.aop.MarkFileGroup;
import com.tranphucvinh.config.store.FileException;
import com.tranphucvinh.config.util.ApplicationContextProvider;
import com.tranphucvinh.config.util.GroupRefer;
import com.tranphucvinh.mybatis.mapper.FileMapper;
import com.tranphucvinh.mybatis.model.FileVO;

@Service
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class FilesInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(FilesInterceptor.class);

    private FileMapper fileMapper;

    private static Class<?>[] allowGroupFieldType = {List.class, Boolean.class};

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];

        Class<?> resultTypeClass = mappedStatement.getResultMaps().get(0).getType();
        List<GroupRefer> grRefList = getGroupReferList(resultTypeClass);
        if(grRefList.size() < 1) {
            return invocation.proceed();
        }

        logger.info("----------------FilesInterceptor--------------");

        Object result = invocation.proceed();
        List<?> resultList = (List<?>) result;

        if(resultList.size() > 0) {
            fileMapper = ApplicationContextProvider.getApplicationContext().getBean(FileMapper.class);

            for(Object obj: resultList) {

                for(GroupRefer gr: grRefList) {
                    logger.info("grpRefTable : "+ gr.getGrpRefTable());
                    logger.info("grpRefType : "+ gr.getGrpRefType());
                    logger.info("groupFieldName : "+ gr.getGroupFieldName());
                    logger.info("primaryIdFieldName : "+ gr.getPrimaryIdFieldName());

                    // get primaryId value
                    Field primaryField = null;
                    try {
                        primaryField = obj.getClass().getDeclaredField(gr.getPrimaryIdFieldName());
                    } catch (Exception e) {
                        throw new FileException("Cannot get primaryId from fieldName ["+gr.getPrimaryIdFieldName()+"] in "+obj.getClass());
                    }
                    boolean primaryFieldAccessible = primaryField.isAccessible();
                    primaryField.setAccessible(true);

                    // set primary id
                    Object primaryIdObj = primaryField.get(obj);
                    if(Objects.isNull(primaryIdObj)) {
                        continue;
                    }
                    Long primaryId = (Long) primaryIdObj;
                    gr.setGrpRefPrimaryId(primaryId);
                    logger.info("grpRefPrimaryId : "+ primaryId);

                    // get files
                    List<FileVO> files = fileMapper.selectGroupFiles(gr);
                    logger.info("Found number of files : "+ files.size());

                    // return accessible
                    primaryField.setAccessible(primaryFieldAccessible);

                    Field fileGroupField = obj.getClass().getDeclaredField(gr.getGroupFieldName());
                    boolean fileGroupAccessible = fileGroupField.isAccessible();
                    fileGroupField.setAccessible(true);

                    // set data
                    if(List.class.equals(fileGroupField.getType())) {
                        fileGroupField.set(obj, files);
                    }
                    else if(Boolean.class.equals(fileGroupField.getType())) {
                        fileGroupField.set(obj, files.size()>0?true:false);
                    }

                    // return accessible
                    fileGroupField.setAccessible(fileGroupAccessible);
                }
            }
        }

        logger.info("----------------FilesInterceptor--------------");

        return resultList;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public static List<GroupRefer> getGroupReferList(Class<?> clazz) {
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<Field> allFields = new ArrayList<>(Arrays.asList(fieldsArr));
        List<GroupRefer> result = new ArrayList<>();
        for(Field field : allFields) {
            if(field.getDeclaredAnnotations().length > 0) {
                MarkFileGroup mf = field.getAnnotation(MarkFileGroup.class);
                if(Objects.nonNull(mf)) {
                    GroupRefer group = new GroupRefer();
                    // check groupFiledType
                    if(!Arrays.asList(allowGroupFieldType).contains(field.getType())) {
                        throw new FileException("Error at fieldName ["+field.getName()+"]. Object assign from @MarkFileGroup must be exists in "+Arrays.toString(allowGroupFieldType));
                    }
                    group.setGrpRefTable(mf.grpRefTable());
                    group.setGrpRefType(mf.grpRefType());
                    group.setPrimaryIdFieldName(mf.primaryIdFieldName());
                    group.setGroupFieldName(field.getName());
                    result.add(group);
                }
            }
        }
        return result;
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }
}
