package com.du.easytools.common.word;


import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


/**
 * @author :  dush
 * @date :  2022/9/27  11:23
 * @Decription  修改Word模板
 */
public class easyWord {

    private static final Logger logger = LoggerFactory.getLogger(easyWord.class);

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        easyWord easyWord = new easyWord();
        String filePath = "F:\\标准创新奖模板.docx";
        FileInputStream in = new FileInputStream(filePath);//载入文档
        XWPFDocument doc = new XWPFDocument(in);

        XWPFDocument doc1 = easyWord.processTemp(doc, 6, 10);
        XWPFDocument doc2 =  easyWord.processTemp(doc1,17,10);//第二次调用数表格时，多加1

        FileOutputStream out = new FileOutputStream(new File("create_table.docx"));
        doc2.write(out);
        out.close();

        long end = System.currentTimeMillis();

        logger.info("耗时：[{}]秒",(end-start / 1000)%60);

    }

    public void testWord(String filePath){

        XWPFTable table = null;
        List<XWPFTableRow> rows = null;
        List<XWPFTableCell> cells = null;
        List<XWPFParagraph> paragraphs = null;

        try{
            FileInputStream in = new FileInputStream(filePath);//载入文档
            FileOutputStream out = new FileOutputStream(new File("create_table.docx"));
                XWPFDocument doc = new XWPFDocument(in);//得到word文档的信息
                List<XWPFTable> tableList = doc.getTables();
            Object[] tableArray = tableList.toArray();
                Iterator<XWPFTable> it = doc.getTablesIterator();//得到word中的表格
                paragraphs = doc.getParagraphs();//得到Word中的段落

              for (XWPFParagraph paragraph : paragraphs){
                  System.out.println(paragraph.getText());
              }
                XWPFTable targetTable = tableList.get(5);

                rows = targetTable.getRows();

                //create table
                XWPFTable table1 = doc.createTable(rows.size(),0);

                //读取每一行数据
                for (int i=0;i<rows.size();i++) {
                //读取每一列数据
                cells = rows.get(i).getTableCells();
                XWPFTableRow tableRow = table1.getRow(i);

                //copyStyleAndContext(rows.get(i),tableRow);

                //System.out.println();

                }

            doc.insertTable(tableArray.length+1,table1);
            doc.write(out);
            out.close();
            System.out.println("create_table.docx written successully");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
      * 复制表格以完成模板的修改
      * @param  in
      * @param  target
      * @param  number
      * @return
      */
    public void copyTable(FileInputStream in,int target,int number) throws IOException {

        long start = System.currentTimeMillis();

        XWPFDocument doc = new XWPFDocument(in);//得到word文档的信息
        FileOutputStream out = new FileOutputStream(new File("create_table.docx"));

        XWPFTable table =null;
        List<XWPFTableRow> rows = null;
        List<XWPFParagraph> paragraphs = doc.getParagraphs();//获得文档所有段落信息
        List<XWPFTable> tableList = doc.getTables();//得到文档所有表格数据
        XWPFTable targetTable = tableList.get(target-1);//得到要复制的表

        //段落标识
        int flag = 0;

        for (int t=0;t<number;t++){

            rows = targetTable.getRows();

            //根据段落匹配标识
            for (int p=50;p<paragraphs.size();p++){

                XWPFParagraph paragraph = paragraphs.get(p);
                String text=paragraph.getText();

                //匹配标识
                if (("${mark_newTable").equals(text)){

                    flag =p;

                    XmlCursor cursor= paragraph.getCTP().newCursor();
                    cursor.toNextSibling();

                    XWPFParagraph nextParagraph = doc.insertNewParagraph(cursor);
                    XWPFRun nextRun = nextParagraph.createRun();
                    nextRun.setText("");

                    XmlCursor nextcursor = nextParagraph.getCTP().newCursor();
                    nextcursor.toNextSibling();

                    table = doc.insertNewTbl(nextcursor);
                    for(int i =1;i<rows.size();i++){
                        table.createRow();
                    }

                    //复制表格格式与内容
                    for (int i=0;i<rows.size();i++) {
                        XWPFTableRow tableRow = table.getRow(i);
                        copyStyleAndContext(rows.get(i),tableRow,t+1);
                    }

                    break;
                }

            }

        }

        //清空标识的段落
        XWPFParagraph paragraph = paragraphs.get(flag);
        paragraph.removeRun(0);

        //删除原来模板
        deleteTable(tableList.get(target-1));

        doc.write(out);
        out.close();

        long end = System.currentTimeMillis();
        logger.info("耗时：[{}]秒",(end-start / 1000)%60);

        logger.info("create_table.docx written successully");
    }

    /**
     * 复制表格样式
     * @param sourceRow
     * @param targetRow
     */
    private void copyStyleAndContext(XWPFTableRow sourceRow, XWPFTableRow targetRow,int times){
        //复制行属性
        targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if(null==cellList){
            return ;
        }
        //复制列内容、属性以及列中段落属性
        XWPFTableCell targetCell = null;
        for(int i=0;i<cellList.size();i++){

            if (0==i){
                targetCell = targetRow.getCell(0);
            }
            else {
                targetCell = targetRow.addNewTableCell();
            }

            //内容设置
            List<XWPFParagraph> delPList = dealParagraphs(cellList.get(i).getParagraphs(),times);

//            for (XWPFParagraph paragraph : delPList){
//                System.out.println(paragraph.getRuns().get(0));
//            }

            targetCell.setParagraph(delPList.get(0));

            //列属性
            targetCell.getCTTc().setTcPr(cellList.get(i).getCTTc().getTcPr());
            //段落属性
            targetCell.getParagraphs().get(0).getCTP().setPPr(cellList.get(i).getParagraphs().get(0).getCTP().getPPr());

        }
    }

    /**
     * 删除表格
     * @param table 表格对象
     */
    public static void deleteTable(XWPFTable table){
        List<XWPFTableRow> rows = table.getRows();
        int rowLength = rows.size();
        for (int i = 0; i < rowLength; i++) {
            table.removeRow(0);
        }
    }

    /**
     * 处理模板段落赋值变量
     * @param paragraphList
     */
    public static List<XWPFParagraph> dealParagraphs (List<XWPFParagraph> paragraphList,int times){

        //变量标识
        final String var = "var_";

        List<XWPFRun> runs = null;

        int deal = times;

        if(CollectionUtils.isEmpty(paragraphList)){
            return new ArrayList<XWPFParagraph>();
        }

        for(XWPFParagraph paragraph:paragraphList){

            String text = null;

            runs = paragraph.getRuns();
            text = runs.get(0).getText(0);

            if(StringUtils.isEmpty(text)){
                    continue;
            }

            if (text.contains(var)){

                //变量截取处理
                if (times>1 && times<=10){
                    text = text.substring(0,text.length()-1);
                } else if (times>10){
                    text = text.substring(0,text.length()-2);
                }

                text = text.replace(text, text.toString().concat(String.valueOf(deal)));
                runs.get(0).setText(text,0);
            }

        }

        return paragraphList;
    }

    /**
     * 模板内容修改
     * @param  in
     * @param  target
     * @param  number
     * @return
     */
    public XWPFDocument processTemp(XWPFDocument in,int target,int number) throws IOException {

        //long start = System.currentTimeMillis();

        XWPFDocument doc = in;//得到word文档的信息
        FileOutputStream out = new FileOutputStream(new File("create_table.docx"));

        XWPFTable table =null;
        List<XWPFTableRow> rows = null;
        List<XWPFParagraph> paragraphs = doc.getParagraphs();//获得文档所有段落信息
        List<XWPFTable> tableList = doc.getTables();//得到文档所有表格数据
        XWPFTable targetTable = tableList.get(target-1);//得到要复制的表

        //段落标识
        int flag = 0;

        for (int t=0;t<number;t++){

            rows = targetTable.getRows();

            //根据段落匹配标识
            for (int p=50;p<paragraphs.size();p++){

                XWPFParagraph paragraph = paragraphs.get(p);
                String text=paragraph.getText();

                //匹配标识
                if (("${mark_newTable").equals(text)){

                    flag =p;

                    XmlCursor cursor= paragraph.getCTP().newCursor();
                    cursor.toNextSibling();

                    XWPFParagraph nextParagraph = doc.insertNewParagraph(cursor);
                    XWPFRun nextRun = nextParagraph.createRun();
                    nextRun.setText("");

                    XmlCursor nextcursor = nextParagraph.getCTP().newCursor();
                    nextcursor.toNextSibling();

                    table = doc.insertNewTbl(nextcursor);
                    for(int i =1;i<rows.size();i++){
                        table.createRow();
                    }

                    //复制表格格式与内容
                    for (int i=0;i<rows.size();i++) {
                        XWPFTableRow tableRow = table.getRow(i);
                        copyStyleAndContext(rows.get(i),tableRow,t+1);
                    }

                    break;
                }

            }

        }

        //清空标识的段落
        XWPFParagraph paragraph = paragraphs.get(flag);
        paragraph.removeRun(0);

        //删除原来模板
        deleteTable(tableList.get(target-1));

        //doc.write(out);
        //out.close();

//        long end = System.currentTimeMillis();
//        logger.info("耗时：[{}]秒",(end-start / 1000)%60);
//
//        logger.info("create_table.docx written successully");

        return doc;
    }

    /**
     * 复制表格
     * @param doc
     * @param sourceTableIndex
     * @return
     */
    private XWPFTable copyTableByCTTbl(XWPFDocument doc, int sourceTableIndex) {

        List<XWPFTable> tables = doc.getTables();
        // 创建新的 CTTbl，table
        CTTbl ctTbl = CTTbl.Factory.newInstance();
        // 复制原来的CTTbl
        ctTbl.set(tables.get(sourceTableIndex).getCTTbl());
        //获得原来的表格内容
        IBody iBody = tables.get(sourceTableIndex).getBody();
        // 创建一个table，使用复制好的Cttbl和iBody
        XWPFTable newTable = new XWPFTable(ctTbl, iBody);

        return newTable;
    }


    /**
      * 在模板特定字符位置插入图片
      * @param
      * @return
      */
    public static void processParagraphs(List<XWPFParagraph> paragraphs,Map<String,Object> params,XWPFDocument doc) throws IOException, InvalidFormatException {

        List<XWPFRun> runs = null;
        String text = null;

        if (paragraphs == null) {
            return;
        }

        for (XWPFParagraph paragraph : paragraphs) {

            runs = paragraph.getRuns();

            for (int i = 0; i < runs.size(); i++) {

                text = runs.get(i).getText(0);

                //图片插入段落
                if (params.containsKey(text)){

                    //插入图片处理
                    if ("插入位置字符".equals(text)){

                        ByteArrayInputStream inputStream = (ByteArrayInputStream) params.get(text);

                        //添加图片
                        runs.get(i).addPicture(inputStream,XWPFDocument.PICTURE_TYPE_PNG,"img", Units.toEMU(10),Units.toEMU(10));

                        //设置浮动位置
                        CTDrawing drawing = runs.get(i).getCTR().getDrawingArray(0);
                        CTGraphicalObject graphicalObject = drawing.getAnchorArray(0).getGraphic();

                        //将新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
                        CTAnchor anchor  = getAnchorWithGraphic(graphicalObject,
                                Units.toEMU(60),Units.toEMU(60),//图片大小
                                Units.toEMU(100),Units.toEMU(100),//相对当前段落位置的偏移量
                                false //是否隐藏
                        );
                        //添加浮动属性
                        drawing.setAnchorArray(new CTAnchor[]{anchor});
                        //删除行内属性
                        drawing.removeAnchor(0);

                        //去掉原有标识字符
                        text = text.replace(text,"");
                    }

                }

            }

        }

    }

    /**
     *  修改图片的浮动显示
     *
     */
    public static CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject, int width, int height, int leftOffset, int topOffset, boolean behind) {

        String anchorXML =
                "<wp:anchor  xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                        + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"" + ((behind) ? 1 : 0) + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                        + "<wp:simplePos x=\"0\" y=\"0\"/>"
                        + "<wp:positionH relativeFrom=\"column\">"
                        + "<wp:posOffset>" + leftOffset + "</wp:posOffset>"
                        + "</wp:positionH>"
                        + "<wp:positionV relativeFrom=\"paragraph\">"
                        + "<wp:posOffset>" + topOffset + "</wp:posOffset>" +
                        "</wp:positionV>"
                        + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                        + "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
                        + "<wp:wrapNone/>"
                        + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + null + "\"/><wp:cNvGraphicFramePr/>"
                        + "</wp:anchor>";
        CTDrawing drawing= null;
        try{
            drawing=CTDrawing.Factory.parse(anchorXML);
        }catch(XmlException e) {
            e.printStackTrace();
        }

        CTAnchor anchor= drawing.getAnchorArray(0);
        anchor.setGraphic(ctGraphicalObject);
        return anchor;

    }


}
