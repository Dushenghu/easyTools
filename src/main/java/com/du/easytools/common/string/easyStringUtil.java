package com.du.easytools.common.string;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author :  dush
 * @date :  2022/9/14  11:51
 * @Decription String相关操作工具类
 */
public class easyStringUtil {

    private static final Logger logger = LoggerFactory.getLogger(easyStringUtil.class);

    /**
      * 判断字符串是否为空(trim后判断)
      * @param str
      * @return boolean
      */
    public static boolean isBlank(String str){
        return StringUtils.isBlank(str);
    }

    /**
     * 判断字符串是否非空(trim后判断)
     * @param str
     * @return boolean
     */
    public static boolean isNotBlank(String str){
        return StringUtils.isNotBlank(str);
    }

    /**
      * 将以某一符号分隔的字符串转换为字符串数组
      * @param  mark,str
      * @return String[]
      */
    public static String[] str2List(String mark,String str){
        int i = 0;
        String temp  = str;
        String[] returnStr = new String[str.length()+1 - temp.replace(mark,"").length()];

        str = str + mark;
        while (str.indexOf(mark)>0){
            returnStr[i] = str.substring(0,str.indexOf(mark));
            str = str.substring(str.indexOf(mark)+1,str.length());
            i++;
        }
        return returnStr;
    }

    /**
     * 如果字符串为空，则返回参数二;否则返回原字符串
     * @param str  原值
     * @return String 处理后值
     */
    public static String strNull2Other(String str,String other) {
        String temp = str;
        if(isBlank(temp) || str.equalsIgnoreCase("NULL")){
            temp = other;
        }
        return temp;
    }

    /**
      * 字符串数组打印
      * @param  strList
      * @return
      */
    public static void strList2Show(String[] strList){
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i<strList.length;i++){
            sb.append(strList[i]).append(" ");
        }
        System.out.println(sb.toString());
    }
}
