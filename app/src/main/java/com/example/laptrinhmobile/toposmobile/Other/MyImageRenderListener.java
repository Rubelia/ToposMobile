package com.example.laptrinhmobile.toposmobile.Other;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LapTrinhMobile on 10/27/2015.
 */
public class MyImageRenderListener implements RenderListener {

    String path;
    public MyImageRenderListener(String path) {
        this.path = path;
    }

    @Override
    public void beginTextBlock() {

    }

    @Override
    public void renderText(TextRenderInfo textRenderInfo) {

    }

    @Override
    public void endTextBlock() {

    }

    @Override
    public void renderImage(ImageRenderInfo imageRenderInfo) {
        try {
            String filename;
            FileOutputStream os;
            PdfImageObject image = imageRenderInfo.getImage();
            if (image == null) return;
            filename = String.format(path, imageRenderInfo.getRef().getNumber(), image.getFileType());
            os = new FileOutputStream(filename);
            os.write(image.getImageAsBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
