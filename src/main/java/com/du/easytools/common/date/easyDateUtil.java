package com.du.easytools.common.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Date;

/**
 * @author :  dush
 * @date :  2022/9/15  10:17
 * @Decription date操作工具类
 */
public class easyDateUtil {

    private static final Logger logger = LoggerFactory.getLogger(easyDateUtil.class);

    //时间格式
    public static final String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YMD = "yyyy-MM-dd";

    /**
      * 获取当前时间(joda-time)
      * @return  DateTime
      */
    public static DateTime currentTime(){
        return new DateTime();
    }

    /**
      * 获得当前已格式化的时间(格式：YYYY-MM-DD hh:mm:ss)
      * @param
      * @return   string
      */
    public static String now(){
        return now(FORMAT_YMDHMS);
    }

    /**
      * 获得当前格式化的时间
      * @param  pattern 时间格式
      * @return   String
      */
    public static String now(String pattern){
        return currentTime().toString(pattern);
    }

    /**
     * 将时间字符串直接转换为joda-time对象
     * @param  timeStr
     * @return joda-time
     */
    public static DateTime time(String timeStr){
        return timeFormat(timeStr,null);
    }

    /**
      * 将时间字符串转换为joda-time对象
      * @param  timeStr
      * @param  timePattern 时间格式
      * @return  DateTime
      */
    public static DateTime timeFormat(String timeStr,String timePattern){
        if (null == timeStr || "".equals(timeStr)){
            return null;
        }
        if (null != timePattern && !"".equals(timePattern)){
            //有格式转换器
            return DateTimeFormat.forPattern(timePattern).parseDateTime(timeStr);
        } else {
            //没有格式转化器
            if (timeStr.length() == 19){
                return DateTimeFormat.forPattern(FORMAT_YMDHMS).parseDateTime(timeStr);
            }
            else if (timeStr.length() == 10){
                return DateTimeFormat.forPattern(FORMAT_YMD).parseDateTime(timeStr);
            }
            else {
                return new DateTime(timeStr);
            }
        }
    }

    /**
     * 将时间转换为指定格式的字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String time2Str(Date date, String pattern) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(pattern);
    }

}
