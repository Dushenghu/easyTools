package com.du.easytools.testDemo;

import com.du.easytools.common.pdf.easyPdf;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author :  dush
 * @date :  2023/1/5  11:53
 * @Decription
 */
public class pdfTest {

    public static void main(String args[]) throws IOException, DocumentException {
        easyPdf easyPdf = new easyPdf();
        //easyPdf.createNewPdf("Hello_pdf","F:\\newPdf.pdf");
        easyPdf.doPdf();
    }
}
