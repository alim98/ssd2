package com.alim.ssn.main.create;

import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class PdfCreatorImpl implements PdfCreator {
    @Override
    public File createPdf(List<byte[]> bytes) {
        File file;
        Random random=new Random();
        int fileName=random.nextInt(10000000);

        String FILE_NAME = Environment.getExternalStorageDirectory() + File.separator +fileName+".pdf";
        PdfDocument pdfDocument = null;
        try {
            pdfDocument = new PdfDocument(new PdfWriter(FILE_NAME));

            Document document = new Document(pdfDocument);

            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new WatermarkingEventHandler());

            for (int i = 0; i < bytes.size(); i++) {
                addImage(bytes.get(i), document);
            }

            document.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        file = new File(FILE_NAME);
        return file;
    }

    private void addImage(byte[] bytes, Document document) {

        Image img = new Image(ImageDataFactory.create(bytes));
        if (img.getImageWidth() > img.getImageHeight()) {
            img.setRotationAngle(Math.toRadians(270));
            img.scaleAbsolute(737.0079f, 525.58203f);

        }
        document.add(img);
        document.add(new AreaBreak());
    }
    protected class WatermarkingEventHandler implements IEventHandler {

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument document = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfFont font = null;
            try {
                font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            } catch (IOException e) {
                e.printStackTrace();
            }
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), document);
            new Canvas(canvas, document, page.getPageSize())
                    .setFontColor(ColorConstants.LIGHT_GRAY)
                    .setFontSize(22)
                    .setFont(font)
                    .showTextAligned(new Paragraph("Created at JOZVA.ir"), 160, 40, document.getPageNumber(page), TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);

        }
    }
}
