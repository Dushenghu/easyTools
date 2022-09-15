package com.du.easytools.common.helper;

import com.du.easytools.common.collection.easyCollectionUtil;
import com.du.easytools.common.string.easyStringUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author :  dush
 * @date :  2022/9/14  17:27
 * @Decription 断言判断辅助工具类
 */
public abstract class Assert {

    /**
      * 保存时主键校验
      */
    public static void nullPKId(Integer id,String key){
        if (id != null){
            throw new IllegalArgumentException(key+" 必须为空");
        }
    }

    /**
      * 保存时主键校验
      */
    public static void nullPKId(String id,String key){
        if (id != null){
            throw new IllegalArgumentException(key+" 必须为空");
        }
    }

    /**
      * 更新(单条)时主键校验
      */
    public static void notNullPKId(Integer id, String key) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(key + " 不能为空或<=0");
        }
    }

    /**
      * 更新(单条)时主键校验
      */
    public static void notNullPKId(String id, String key) {
        if (easyStringUtil.isBlank(id)) {
            throw new IllegalArgumentException(key + " 不能为空");
        }
    }

    /**
      * 更新(批量)时主键校验
      */
    public static void notNullPKIds(List<String> ids, String key) {
        if (easyCollectionUtil.isEmpty(ids)) {
            throw new IllegalArgumentException(key + " 不能为空");
        }
    }


    /**
      * 邮箱校验
      */
    public static void email(String text) {
        String regex = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        pattern(text, regex, true, "邮箱");
    }

    /**
      * 手机号校验
      */
    public static void mobile(String text) {
        String regex = "((^(13|15|17|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        pattern(text, regex, true, "手机号");
    }

    /**
      * 正则表达式
      */
    public static void pattern(String text, String regex, boolean flag, String key) {
        boolean result = false;
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            result = matcher.matches();
        } catch (Exception e) {
            result = false;
        }
        if (result != flag) {
            throw new IllegalArgumentException(key + "_合法");
        }
    }
}
