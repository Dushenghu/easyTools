package com.du.easytools.common.word;



import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.List;


/**
 * @author :  dush
 * @date :  2022/9/27  11:23
 * @Decription
 */
public class easyWord {
    private static final Logger logger = LoggerFactory.getLogger(easyWord.class);


    public static void main(String[] args) throws IOException {
        easyWord easyWord = new easyWord();
        String filePath = "F:\\标准创新奖模板.docx";
        FileInputStream in = new FileInputStream(filePath);//载入文档

        easyWord.copyTable(in,6);

        //easyWord.testWord(filePath);
        //easyWord.testWord(filePath);
        //easyWord.copyTable(in,6,25);
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

                copyStyleAndContext(rows.get(i),tableRow);

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

    //复制表格以完成模板的修改
    public void copyTable(FileInputStream in,int target) throws IOException {

        long start = System.currentTimeMillis();

        XWPFDocument doc = new XWPFDocument(in);//得到word文档的信息
        FileOutputStream out = new FileOutputStream(new File("create_table.docx"));

        XWPFTable table =null;
        List<XWPFTableRow> rows = null;
        List<XWPFParagraph> paragraphs = doc.getParagraphs();

        List<XWPFTable> tableList = doc.getTables();//得到文档所有表格数据

        XWPFTable targetTable = tableList.get(target-1);//得到要复制的表
        rows = targetTable.getRows();

        //替换新建表格
        for (int p=50;p<paragraphs.size();p++){
            XWPFParagraph paragraph = paragraphs.get(p);
            String text=paragraph.getText();
            if (text.contains("${mark_newTable}")){

//
                List<XWPFRun> Oldruns = paragraph.getRuns();
                Oldruns.get(0).setText("\r",0);

                //移动游标，新建表格
                XmlCursor cursor= paragraph.getCTP().newCursor();
//                XmlCursor cursor1 = paragraph.getCTP().newCursor();
//
//                XWPFParagraph newP =doc.insertNewParagraph(cursor);
//                XWPFRun newR = newP.createRun();
//                newR.setText("\r");

                table = doc.insertNewTbl(cursor);
                for(int i =1;i<rows.size();i++){
                    table.createRow();
                }

                //复制表格格式与内容
                for (int i=0;i<rows.size();i++) {
                    XWPFTableRow tableRow = table.getRow(i);
                    copyStyleAndContext(rows.get(i),tableRow);
                }

                Oldruns.get(0).setText("\r",0);


            }

        }

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
    private void copyStyleAndContext(XWPFTableRow sourceRow, XWPFTableRow targetRow){
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
            //targetCell.setText(cellList.get(i).getText());
            targetCell.setParagraph(cellList.get(i).getParagraphs().get(0));

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


}
