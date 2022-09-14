package com.du.easytools.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.du.easytools.common.collection.easyCollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  dush
 * @date :  2022/9/14  11:02
 * @Decription Json相关操作工具类
 */
public class easyJsonUtil {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(easyCollectionUtil.class);
    //MSG
    private static final String SUCCESS_MSG = "请求成功";
    private static final String FAILURE_MSG = "请求失败";
    //JSON特征
    private static SerializerFeature[] features = { SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect };

    /**
      * 生成Json字符串
      * @param  ob
      * @return json
      */
    public static String toJsonString(Object ob){
        String obStr = JSON.toJSONString(ob,features);
        return obStr;
    }

    /**
      * 返回正确的Json数据
      * code 为 200
      */
    public static String resultSuccess(Object data){
        Map<String,Object> rs = new HashMap<String,Object>();
        rs.put("code",HttpStatus.OK.value()+ "" );
        rs.put("msg",SUCCESS_MSG);
        rs.put("data",data == null ? new Object() : data);
        return toJsonString(rs);
    }

    /**
      * 返回错误的Json提示
      * code 设置为 -200
      *
      */
    public static String resultError(String msg,Object data){
        Map<String,Object> rs = new HashMap<String, Object>();
        rs.put("code","-200");
         msg = StringUtils.isNotBlank(msg) ? msg : FAILURE_MSG;
         //优化提示
        if (msg.contains("SQLException") || msg.contains("NullPointerException")){
            msg = "数据异常，请重试！";
        }
        rs.put("msg",msg);
        rs.put("data",data == null ? new Object() : data);
        rs.put("time",System.currentTimeMillis());
        return toJsonString(rs);
    }

}
