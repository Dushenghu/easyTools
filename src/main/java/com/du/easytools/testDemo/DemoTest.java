package com.du.easytools.testDemo;

import com.du.easytools.common.word.ftlCopyTable.copyTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author :  dush
 * @date :  2022/9/7  15:16
 * @Decription 测试Demo
 */
public class DemoTest {
    private static final Logger logger = LoggerFactory.getLogger(DemoTest.class);
    public static void main(String args[]) throws IOException {

        /*
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

        */

        //富文本表格复制测试
        copyTable cpT = new copyTable();
        Scanner in = new Scanner( System.in );

        System.out.println("请输入要复制的表格数目");

        int listSize = in.nextInt();

        String templeteName = "declaration";

        String exportWordURL = "F:/1.doc";

        Map<String,Object> dataMap = new HashMap<String,Object>();

        List<Object> list = new ArrayList<Object>();

        /*User u  = new User();
        u.setName("小杜");
        u.setSex("男");
        u.setAge(18);

        list.add(u);
        list.add(u);
        list.add(u);

        dataMap1.put("list",list);*/

        for (int i=1;i<listSize+1;i++){

            FinishOrg param = new FinishOrg();

            param.setFinishOrgIndex(i);
            param.setOrgName("完成单位名称"+i);
            param.setFinishOrgContact("完成单位联系人"+i);
            param.setFinishOrgContactTel("完成单位联系人手机号"+i);
            param.setFinishOrgFax("完成单位传真"+i);
            param.setFinishOrgEmail("完成单位邮箱"+i);
            param.setFinishOrgAddress("完成单位地址"+i);
            param.setFinishOrgPostcode("完成单位邮政编码"+i);
            param.setFinishOrgContribution("完成单位主要贡献"+i);

            list.add(param);

        }

        dataMap.put("list",list);

        cpT.createWord(dataMap,templeteName,exportWordURL);

    }


}
