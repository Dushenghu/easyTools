package com.du.easytools.common.collection;


import static com.du.easytools.common.bean.easyBeanUtil.objectCompare;
import static com.du.easytools.common.bean.easyBeanUtil.objectToStr;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;


/**
 * @author :  dush
 * @date :  2022/9/7  14:41
 * @Decription 集合相关操作工具类
 */
public class easyCollectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(easyCollectionUtil.class);

    /**
      * 判断Collection是否为空
      * @param  coll
      * @return
      */
    public static boolean isEmpty(Collection<?> coll){
        return CollectionUtils.isEmpty(coll);
    }

    /**
      * 判断Collection是否非空
      * @param  coll
      * @return
      */
    public static boolean isNotEmpty(Collection<?> coll){
        return CollectionUtils.isNotEmpty(coll);
    }

    /**
      * 判断Map是否为空
      * @param  map
      * @return
      */
    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    /**
     * 判断Map是否非空
     * @param  map
     * @return
     */
    public static boolean isNotEmpty(Map<?,?> map){
        return MapUtils.isNotEmpty(map);
    }

    /**
      * 判断两个集合是否相同
      * @param  colla
      * @param  collb
      * @return
      */
    public static boolean isEqualCollection(Collection<?> colla,Collection<?> collb){
        return CollectionUtils.isEqualCollection(colla,collb);
    }

    /**
      * 过滤集合中满足条件的所有元素
      * @param coll
      * @param filter(Object)
      * @return
      */
    public static void filter(Collection<?> coll,Object filter){
        CollectionUtils.filter(coll, new Predicate() {
            @Override
            public boolean evaluate(Object ob) {
                try {
                    if(objectCompare(ob,filter)) {
                        logger.info("筛选对象：[{}]",objectToStr(ob));
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

}
