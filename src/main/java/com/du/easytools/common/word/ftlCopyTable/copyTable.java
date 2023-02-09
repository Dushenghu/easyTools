package com.du.easytools.common.word.ftlCopyTable;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.*;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author :  dush
 * @date :  2022/10/17  15:24
 * @Decription
 */
public class copyTable {

    private Logger logger = Logger.getLogger(copyTable.class.toString());
    private Configuration config = null;

    public copyTable() {
        config = new Configuration(Configuration.VERSION_2_3_20);
        config.setDefaultEncoding("utf-8");
    }

    /**
      * 生成WORD
      * @param dataMap //表格数据
      * @param templateName //模板名称
      * @param saveFilePath 保存文件路径的全路径名（路径+文件名）
      * @return
      */
     public void createWord(Map<String,Object> dataMap, String templateName, String saveFilePath) throws IOException {

         //加载模板路径
         //config.setClassForTemplateLoading(copyTable.class,"/");
         FileTemplateLoader fileTemplateLoader = new FileTemplateLoader(new File(System.getProperty("user.dir") + "/src/main/resources/template/"));
         config.setTemplateLoader(fileTemplateLoader);

         //设置遍历异常处理器
         config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

         //模板
         Template template = null;

//         if(templateName.endsWith(".ftl")) {
//             templateName = templateName.substring(0, templateName.indexOf(".ftl"));
//         }
         //templateName = "templete";

         try {
             template = config.getTemplate(templateName + ".ftl");
         } catch (TemplateNotFoundException e) {
             logger.info("模板文件未找到"+e);
             e.printStackTrace();
         } catch (MalformedTemplateNameException e) {
             logger.info("模板类型不正确"+e);
             e.printStackTrace();
         } catch (IOException e) {
             logger.info("IO读取失败"+e);
             e.printStackTrace();
         }

         File outFile = new File(saveFilePath);
         if(!outFile.getParentFile().exists()) {
             outFile.getParentFile().mkdirs();
         }

         Writer out = null;
         FileOutputStream fos = null;
         try {
             fos = new FileOutputStream(outFile);
         } catch (FileNotFoundException e) {
             logger.info("输出文件时未找到文件"+e);
             e.printStackTrace();
         }
         out = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"));
         //将模板中的预先的代码替换为数据
         try {
             template.process(dataMap, out);
         } catch (TemplateException e) {
             logger.info("填充模板时异常"+e);
             e.printStackTrace();
         } catch (IOException e) {
             logger.info("IO读取时异常"+e);
             e.printStackTrace();
         }
         logger.info("由模板文件：" + templateName + ".ftl" + " 生成文件 ：" + saveFilePath + " 成功！！");
         try {
             out.close();//web项目不可关闭
         } catch (IOException e) {
             logger.info("关闭Write对象出错"+e);
             e.printStackTrace();
         }
     }
    /**
     * 获得图片的Base64编码
     * @param imgFile
     * @return
     * @Author
     */
    public String getImageStr(String imgFile) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
        } catch (FileNotFoundException e) {
            logger.info("加载图片未找到"+e);
            e.printStackTrace();
        }
        try {
            data = new byte[in.available()];
            //注：FileInputStream.available()方法可以从输入流中阻断由下一个方法调用这个输入流中读取的剩余字节数
            in.read(data);
            in.close();
        } catch (IOException e) {
            logger.info("IO操作图片错误"+e);
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);


    }
}
