package com.du.easytools.common.file;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :  dush
 * @date :  2022/9/30  14:24
 * @Decription  获取某一文件夹下所有文件目录
 */
public class easyFilesList {

    private static final Logger logger = LoggerFactory.getLogger(easyFilesList.class);

    public static void main(String[] args){
        for (String s :getAllFiles("F:\\home",false)){
            logger.info("[{}]",s);
        };

    }


    /**
      * 获取某一文件夹下文件目录
      * @param  path  文件夹路径
      * @param  isAddDirectory  是否添加子文件夹名称与路径
      * @return filesList
      */
    public static List<String> getAllFiles(String path,boolean isAddDirectory) {

        List<String> filesList = new ArrayList<String>();
        File baseFile = new File(path);

        if (baseFile.isFile() || !baseFile.exists()) {
            return filesList;
        }

        File[] files = baseFile.listFiles();

        for (File file : files) {

            if (file.isDirectory()) {

                if(isAddDirectory){
                    filesList.add("文件夹名称："+file.getName());
                    filesList.add("文件夹路径："+file.getAbsolutePath());
                }

                filesList.addAll(getAllFiles(file.getAbsolutePath(),isAddDirectory));
            }
            else {
                filesList.add("文件名称："+file.getName());
                if (isAddDirectory){
                    filesList.add("文件路径："+file.getAbsolutePath());
                }
            }

        }

        return filesList;
    }

}
