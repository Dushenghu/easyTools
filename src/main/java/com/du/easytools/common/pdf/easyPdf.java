package com.du.easytools.common.pdf;

/**
 * @author :  dush
 * @date :  2023/1/5  11:35
 * @Decription  导出PDF
 */

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.codec.binary.StringUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * com.itextpdf.text.Paragraph：表示一个缩进的文本段落，在段落中，你可以设置对齐方式，缩进，段落前后间隔等
 * com.itextpdf.text.Chapter：表示PDF的一个章节，他通过一个Paragraph类型的标题和整形章数创建
 * com.itextpdf.text.Font：这个类包含了所有规范好的字体，包括family of font，大小，样式和颜色，所有这些字体都被声明为静态常量
 * com.itextpdf.text.List：表示一个列表；
 * com.itextpdf.text.Anchor：表示一个锚，类似于HTML页面的链接。
 * com.itextpdf.text.pdf.PdfWriter：当这个PdfWriter被添加到PdfDocument后，所有添加到Document的内容将会写入到与文件或网络关联的输出流中。
 * com.itextpdf.text.pdf.PdfReader：用于读取PDF文件；
 *
 */

public class easyPdf {

    /**
      * 将输入内容导出为PDF
      * @param  content
      * @return
      */
    public void createNewPdf(String content,String filePath) throws FileNotFoundException, DocumentException {
        /*
        String con = content;
        String fP = filePath;

        Document document = new Document();

        PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(fP.toString()));

        document.open();

        document.add(new Paragraph(con.toString()));

        PdfPTable table = new PdfPTable(2);
        document.add(table);

        document.close();
        */
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        PdfPTable table = new PdfPTable(5);
        for (int aw = 0; aw < 10; aw++) {
            // 构建每一格
            table.addCell("cell");
        }
        document.add(table);
        document.close();

    }



    /**
      * 根据模板导出pdf
      * @param
      * @return
      */

    public void doPdf() throws IOException, DocumentException {

        String picPath1 = "F:\\logo.png";
        String picPath2 = "F:\\hz.png";

        //创建A4大小的文档
        Document document = new Document(PageSize.A4);

        document.open();

        String name = "11,22,33,44,55,66,77,88,99,101,111,121,131,141,151,161,171,161";
        String names = "杜嘟嘟,嘟 嘟,杜嘟嘟,嘟 嘟,杜嘟嘟,嘟 嘟,杜嘟嘟";

        List<String> namesList = Arrays.asList(names.split(","));

        FileOutputStream fos = new FileOutputStream("F:\\new.pdf");

        //读取模板的内容
        PdfReader pdfReader = new PdfReader(new FileInputStream("F:\\template.pdf"));
        if (null != pdfReader){

            //创建输出流
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            //实例化PdfStamper，准备编辑内容
            PdfStamper pdfStamper = new PdfStamper(pdfReader,bos);

            PdfContentByte over = pdfStamper.getOverContent(1);

            //获取所有元素
            AcroFields fields = pdfStamper.getAcroFields();

            //设置中文格式编码，否则不显示
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            //Font font1 = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H",20,Font.BOLD);

            Font boldFont = new Font(baseFont,0,1);

            //添加替换元素
            fields.addSubstitutionFont(baseFont);

            //填充内容
            Map<String,Object> doData = new HashMap<>();
            doData.put("awardDeptName","单位");
            doData.put("awardType","奖项");
            doData.put("awardTypeName","奖项类别");
            doData.put("awardTypeNode","奖项备注");
            doData.put("awardLevelName","奖项等级");
            doData.put("projectName","项目名称");
            doData.put("generalManger","杜嘟嘟");
            doData.put("year","2022");

            //遍历赋值(可分别设置样式)
            for ( String key : doData.keySet()){

                if ("awardTypeName".equals(key)){
                    fields.setFieldProperty(key,"textsize",40f,null);
                }
                String value = (String) doData.get(key);
                fields.setField(key,value);
            }

            //添加图片
            addImage(fields,"logo1",pdfStamper,picPath1,1f);
            addImage(fields,"logo2",pdfStamper,picPath2,0.8f);

            //生成的文档可编辑
            pdfStamper.setFormFlattening(true);
            this.dealList(namesList,over);
            pdfStamper.close();

            //将编辑后的文件输出
            fos.write(bos.toByteArray());
            document.close();
            bos.close();
            fos.close();
        }


    }

    //处理绝对文本1
    public void doText1(PdfWriter pdfWriter) throws DocumentException, IOException {

        //PdfWriter pdfWriter = PdfWriter.getInstance(document, fos);

        //document.open();
        Font font1 = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H",20,Font.BOLD);

        //文本绝对定位
        Paragraph p = new Paragraph();
//        List<Phrase> phraseList = new ArrayList<>();

        for (int i =1;i<=50;i++){
            Phrase ph = new Phrase(String.valueOf(i)+" ",font1);
            p.add(ph);
        }

//            p.setSpacingBefore();
        p.setAlignment(Element.ALIGN_RIGHT);//设置对齐方式
//            p.setLeading(30);

        //文本框位置
        Rectangle rects = new Rectangle(230, 320, 400, 400);
        rects.setBorder(Rectangle.BOX);//显示边框，默认不显示，常量值：LEFT, RIGHT, TOP, BOTTOM，BOX,
        rects.setBorderWidth(1f);//边框线条粗细
        rects.setBorderColor(BaseColor.GREEN);//边框颜色
        rects.setBackgroundColor(BaseColor.WHITE);//背景颜色
        pdfWriter.getDirectContentUnder().rectangle(rects);

        ColumnText ct = new ColumnText(pdfWriter.getDirectContent());
        ct.addElement(p);
        ct.setSimpleColumn(rects);
        ct.go();

    }

    //处理绝对文本
    public void dealText(PdfContentByte over) throws DocumentException {
        ColumnText columnText = new ColumnText(over);

        // llx 左侧边框距离 lly下侧边框距离 urx 右侧边框距离 ury 上测边框距离
        // columnText.setSimpleColumn(115, 120, 300, 185);

        Rectangle rects = new Rectangle(180, 200, 600, 360);
        columnText.setSimpleColumn(rects);

        String names = "杜嘟嘟 嘟 嘟 杜嘟嘟 嘟 嘟 杜嘟嘟\n嘟 嘟 杜嘟嘟 嘟 嘟 杜嘟嘟 嘟 嘟\n杜嘟嘟 嘟 嘟 杜嘟嘟 嘟 嘟 杜嘟嘟\n";
        Paragraph paragraph = new Paragraph(names);
        paragraph.setAlignment(Paragraph.ALIGN_JUSTIFIED);

//        Phrase ph1 = new Phrase("嘟 嘟"+"\n");
//        Phrase ph2 = new Phrase("杜嘟嘟");

        Font font1 = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H",12,Font.BOLD);
        paragraph.setFont(font1);
        columnText.addElement(paragraph);
        columnText.go();

    }

    //处理人名对齐
    public void dealList(List<String> list,PdfContentByte over) throws DocumentException {

        //根据人名创建段落
        for(int i = 1; i<=list.size();i++){

            ColumnText columnText = new ColumnText(over);

            Paragraph p = new Paragraph(list.get(i-1));

            Font font1 = FontFactory.getFont("STSong-Light", "UniGB-UCS2-H",12,Font.BOLD);

            if(i == 1){
                Rectangle rects = new Rectangle(180, 200, 600, 360);
                columnText.setSimpleColumn(rects);
            }
//            else if (i >1 && i%5 !=1 ){
//                Rectangle rects = new Rectangle(180+(30*i), 200, 600-(30*i), 360);
//                columnText.setSimpleColumn(rects);
//            }
            else if (i >1 && i%5 ==1 ){
                Rectangle rects = new Rectangle(180, 200-(40*i), 600, 360+(40*i));
                columnText.setSimpleColumn(rects);
            }

            p.setFont(font1);
            columnText.addElement(p);
            columnText.go();

        }


    }



    //添加图片
    private static void addImage(AcroFields form,String notePlace,PdfStamper stamper,String picPath,float transparent) throws IOException, DocumentException {
        //通过定位符获取页和坐标,左下角为起点
        int pageNo = form.getFieldPositions(notePlace).get(0).page;
        Rectangle signRect = form.getFieldPositions(notePlace).get(0).position;

        float x = signRect.getLeft();
        float y = signRect.getBottom();

        //读图片
        Image image = Image.getInstance(picPath);


        //获取操作页面
        PdfContentByte under = stamper.getOverContent(pageNo);

        if (!"".equals(transparent)){
            //透明度
            PdfGState pgs = new PdfGState();
            pgs.setFillOpacity(transparent);
            under.setGState(pgs);
        }

        //根据域的大小缩放图片
        image.scaleToFit(signRect.getWidth(),signRect.getHeight());

        //添加图片并设置位置
        image.setAbsolutePosition(x,y);
        under.addImage(image);
    }

    //处理数据
    private String changeList(List<String> list){

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {

            String str = "";
            if(list.get(i).length() == 2){
                str = addBlankInMiddle(list.get(i));
            }else{
                str = new String(list.get(i));
            }
            sb.append(str);
            sb.append("\t");
        }

        return sb.toString();
    }

    //名字两个字的中间加上空格
    public  String addBlankInMiddle(String str){

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i == 0){
                sb.append(chars[i]).append("   ");
            }else {
                sb.append(chars[i]);
            }
        }

        return sb.toString();

    }

    //新建table
    private static void appendTableToDocument(Document document){
        //定义 6列的表格
        PdfPTable table = new PdfPTable(new float[] { 80, 80, 80, 80, 80, 80});
        table.setTotalWidth(520);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);//居中

        try {
            //定义数据的字体
            BaseFont baseFont = BaseFont.createFont("Helvetica","UTF-8",BaseFont.NOT_EMBEDDED);
            Font textFont = new Font(baseFont, 6, Font.NORMAL);

            //表头模拟数据
            for (int i = 0; i < 6; i++) {
                PdfPCell heardCell = new PdfPCell();
                heardCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                heardCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                heardCell.setPhrase(new Phrase("value", textFont));
                table.addCell(heardCell);
            }
            //表格数据
            DecimalFormat df = new DecimalFormat("0.00");
            for (int i = 0; i < 1000; i++) {
                PdfPCell value1 = new PdfPCell();
                value1.setPhrase(new Phrase("2022"+i, textFont));
                table.addCell(value1);
                Double value = Math.random()*10;
                PdfPCell value2 = new PdfPCell();
                value2.setPhrase(new Phrase(df.format(value), textFont));

            }
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
