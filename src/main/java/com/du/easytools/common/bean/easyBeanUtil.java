package com.du.easytools.common.bean;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author :  dush
 * @date :  2022/9/7  17:02
 * @Decription 对象相关操作工具类
 */
public class easyBeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(easyBeanUtil.class);

    /**
      * 比较两个对象是否一致包含相同属性值
      * @param  beforeObj
      * @param  afterObj
      * @return
      */
    //使用field对象来处理
    public static boolean objectCompare(Object beforeObj ,Object afterObj) throws  Exception{
        Field[] beforeFields = beforeObj.getClass().getDeclaredFields();
        Field[] afterFields = afterObj.getClass().getDeclaredFields();
        Field.setAccessible(beforeFields, true); //设置私有属性可以访问到
        Field.setAccessible(afterFields, true);
        //循环遍历比较属性
        if(beforeFields != null && afterFields != null && beforeFields.length == afterFields.length){

            for(int i=0; i<beforeFields.length; i++){

                //取出对应的属性值
                Object beforeValue = beforeFields[i].get(beforeObj);
                Object afterValue = afterFields[i].get(afterObj);

                //如果两个对象属性都不为空时
                if (beforeValue != null && !"".equals(beforeValue) && afterValue != null && !"".equals(afterValue) ){
                    if (beforeValue.equals(afterValue)){
                        //属性值相等;
                        StringBuffer sBuf=new StringBuffer();
                        sBuf.append("字段:").append(beforeFields[i].getName()).append(" ").append("值：").append(beforeValue);
                        logger.info("相同属性:[{}]",sBuf.toString());
                        return true;
                    }else {
                        //属性值不相等；

                        /**
                        //对不同属性进行记录
                        StringBuffer differentBuf=new StringBuffer();
                        if((beforeValue != null && !"".equals(beforeValue) && !beforeValue.equals(afterValue)) || ((beforeValue == null || "".equals(beforeValue)) && afterValue != null)){
                         differentBuf.append("字段:").append( beforeFields[i].getName()).append(" ")
                         .append("原值为:").append(beforeValue).append(" ")
                         .append("现值为:").append(afterValue);
                         logger.info("不同变量变化为:[{}]",differentBuf.toString());
                        }
                         */

                        continue;
                    }
                }else{
                    continue;
                }
            }
            return false;
        }
        return false;
    }

    /**
      * 克隆对象
      * @param ob
      * @return ob
      */
    public static Object cloneBean(Object ob) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return BeanUtils.cloneBean(ob);
    }

    /**
      * 判断对象是否为空
      * @param  ob
      * @return boolean
      */
    public static boolean isEmpty(Object ob){
        if ("".equals(ob)||null == ob){
            return true;
        }
        return false;
    }


    /**
      * 对象所有属性toString
      * @param  ob
      * @return  String
      */
    public static String objectToStr(Object ob) throws IllegalAccessException {
        StringBuffer sb = new StringBuffer();
        if (isEmpty(ob)){
            sb = null;
        }else {
            Field[] obFields= ob.getClass().getDeclaredFields();
            //设置私有属性可访问
            Field.setAccessible(obFields,true);
            //循环遍历属性
            logger.info("开始转换对象属性");
            if (!isEmpty(ob)){
                for (int i=0; i<obFields.length; i++){
                    sb.append(obFields[i].getName()).append(": ").append(obFields[i].get(ob)).append(" ");
                }
            }
            logger.info("转换对象属性结束");
        }
        return sb == null ? " " : sb.toString();
    }
}
