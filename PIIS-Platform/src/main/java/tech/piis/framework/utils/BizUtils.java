package tech.piis.framework.utils;

import lombok.extern.slf4j.Slf4j;
import tech.piis.common.utils.DateUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.common.utils
 * User: Tuzki
 * Date: 2020/9/29
 * Time: 16:50
 * Description:业务处理工具类
 */
@Slf4j
public class BizUtils {


    @SuppressWarnings("unchecked")
    public static <T> void setCreatedOperation(Class<T> clazz, T object) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<T>) clazz.getSuperclass();
        }
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object fieldName = field.getName();
                if (fieldName.equals("createdBy")) {
                    field.set(object, SecurityUtils.getUsername());
                }
                if(fieldName.equals("createBy")){
                    field.set(object, SecurityUtils.getUsername());
                }
                if(fieldName.equals("createTime")){
                    field.set(object, DateUtils.getNowDate());
                }
                if (fieldName.equals("createdTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
            }
        } catch (Exception e) {
            log.error("###BizUtils### reflect is failed!");
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> void setUpdatedOperation(Class<T> clazz, T object) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<T>) clazz.getSuperclass();
        }
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object fieldName = field.getName();
                if (fieldName.equals("updatedBy")) {
                    field.set(object, SecurityUtils.getUsername());
                }
                if (fieldName.equals("updatedTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
                if (fieldName.equals("updateBy")) {
                    field.set(object, SecurityUtils.getUsername());
                }
                if (fieldName.equals("updateTime")) {
                    field.set(object, DateUtils.getNowDate());
                }
            }
        } catch (Exception e) {
            log.error("###BizUtils### reflect is failed!");
            e.printStackTrace();
        }
    }
}
