package com.du.easytools.testDemo;

import com.du.easytools.common.bean.easyBeanUtil;
import com.du.easytools.common.collection.easyCollectionUtil;
import com.du.easytools.common.date.easyDateUtil;
import com.du.easytools.common.string.easyStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author :  dush
 * @date :  2022/9/7  15:16
 * @Decription 测试Demo
 */
public class DemoTest {
    private static final Logger logger = LoggerFactory.getLogger(DemoTest.class);
    public static void main(String args[]){

        //Collection工具类测试
        List<String> colla = Arrays.asList("aa", "bb", "cc");
        List<String> collb = Arrays.asList("aa", "bb", "cd");

        logger.info("测试 ====== easyCollectionUtil.isEmpty");
        System.out.println(easyCollectionUtil.isEmpty(colla));
        logger.info("测试 ====== easyCollectionUtil.isNotEmpty");
        System.out.println(easyCollectionUtil.isNotEmpty(colla));
        logger.info("测试 ====== easyCollectionUtil.isEqualCollection");
        System.out.println(easyCollectionUtil.isEqualCollection(colla,collb));

        List<User> collc = new ArrayList<User>();
        User zhan = new User(); zhan.setName("zhang");zhan.setAge(18);
        collc.add(zhan);
        User li = new User(); li.setName("li");li.setAge(18);
        collc.add(li);
        User wang = new User(); wang.setName("wang");wang.setAge(20);
        collc.add(wang);

        User param = new User();
        param.setAge(18);

        logger.info("测试 ====== easyBeanUtil.isEmpty");
        System.out.println(easyBeanUtil.isEmpty(null));

        logger.info("测试 ====== easyCollectionUtil.filter");
        easyCollectionUtil.filter(collc,param);
        System.out.println(collc);

        logger.info("测试 ====== easyStringUtil.str2List");
        String str = "a,b,c,d";
        easyStringUtil.strList2Show(easyStringUtil.str2List(",",str));

//        logger.info("测试 ====== Assert");
//        User userAssert = new User();
//        Assert.notNullPKId(userAssert.getName(),"用户姓名");

        logger.info("测试 ====== easyDateUtil.currentTime");
        System.out.println(easyDateUtil.time("2022-09-15 11:48:22"));

        Date date = new Date();
        System.out.println(easyDateUtil.time2Str(date,easyDateUtil.FORMAT_YMDHMS));


    }


}
