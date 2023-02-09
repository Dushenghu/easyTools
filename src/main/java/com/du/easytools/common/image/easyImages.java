package com.du.easytools.common.image;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author :  dush
 * @date :  2023/2/9  10:10
 * @Decription  图片处理类
 */
public class easyImages {

    /**
      *  通过 BufferedImage 图片流调整图片大小
      * @param
      * @return
      */
    public static BufferedImage resizeImages(BufferedImage oldImage,int width,int height){

        Image newImage = oldImage.getScaledInstance(width,height,Image.SCALE_AREA_AVERAGING);

        BufferedImage outputImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        outputImage.getGraphics().drawImage(newImage,0,0,null);

        return outputImage;
    }


    /**
      * byte[] ===> base64
      * @param
      * @return
      */
    public static String image2Base64(byte[] image){

        BASE64Encoder encoder = new BASE64Encoder();

        //返回BASE64编码过的字节组数组
        return encoder.encode(image);

    }

    /**
      * base64 ===> byte[]
      * @param
      * @return
      */
    public static  byte[] base642Byte(String base64) throws IOException {

        BASE64Decoder decoder = new BASE64Decoder();

        //返回base64编码过的字节数组字符串
        return decoder.decodeBuffer(base64);

    }

    /**
      * BufferedImage ===> byte[]
      * @param
      * @return
      */
    public static byte[] BufferedImage2Byte(BufferedImage image ) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image,"png",outputStream);

        return outputStream.toByteArray();
    }


    /**
      * byte[] ===> BufferedImage
      * @param
      * @return
      */
    public static BufferedImage byte2BuffedImage(byte[] imageByte) throws IOException {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByte);

        BufferedImage image = null;

        image = ImageIO.read(inputStream);

        return image;
    }

    /**
      * 对base64编码的图片流进行背景透明处理
      * @param base64
      * @return   base64
      */
    public String transBase64Pic (String base64) throws IOException {

        //base64 ===> byte[]
        byte[] ba = base642Byte(base64);

        //btye[] ===> BufferedImage
        BufferedImage image = byte2BuffedImage(ba);

        // 高度和宽度
        int height = image.getHeight();
        int width = image.getWidth();

        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片

        int alpha = 0; // 图片透明度
        for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
            for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                int rgb = bufferedImage.getRGB(j2, j1);
                int R = (rgb & 0xff0000) >> 16;
                int G = (rgb & 0xff00) >> 8;
                int B = (rgb & 0xff);
                if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                    rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                }
                bufferedImage.setRGB(j2, j1, rgb);
            }
        }

        // 绘制设置了RGB的新图片
        g2D.drawImage(bufferedImage, 0, 0, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", stream);

        //处理返回格式
        //base64
        BASE64Encoder encoder = new BASE64Encoder();
        base64 = encoder.encode(stream.toByteArray());
        return base64;

        //byte[]
        //return stream.toByteArray();
    }
}
