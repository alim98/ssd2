package com.alim.ssn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
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


public class PdfCreator extends AppCompatActivity {
    private static final int PICK_REQUEST_CODE = 234;

    private static String FILE = Environment.getExternalStorageDirectory() + File.separator + "second.pdf";
 /*   private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_creator);
        getPermissions();
        Button button = findViewById(R.id.btn_pdf);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//todo add this permissions to main activity
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        200);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    void uploadImage() {

           /* Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePhotoIntent, 4001);*/

        Intent pick = new Intent(Intent.ACTION_PICK);
        pick.setType("image/*");
        pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(pick, PICK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            java.util.List<Uri> uriList = new ArrayList<>();
            java.util.List<byte[]> bytes = new ArrayList<>();
            List<byte[]> byteArrays = new ArrayList<>();
            if (data.getClipData() != null) {
                Uri uri = null;

                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    uri = data.getClipData().getItemAt(i).getUri();
                    uriList.add(uri);
                    byte[] byteArray = null;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        //How to load tiles from a large bitmap

                        //convet bitmap to byte[]
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();


                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        byteArray = stream.toByteArray();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    java.util.List<String> paths = new ArrayList<>();
                    byteArrays.add(byteArray);
                    for (int j = 0; j < uriList.size(); j++) {
                        paths.add(getRealPath(uriList.get(j)));

                    }
                    create(byteArrays);
                }
            }

        }

    }

    private String getRealPath(Uri uri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Context applicationContext = getApplicationContext();
        Cursor cursor = applicationContext.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    void create(List<byte[]> bytes) {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(FILE));
            Document document = new Document(pdfDocument);

            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new WatermarkingEventHandler());


            // addMetaData(document);
            //addTitlePage(document);
            // addContent(document);

            for (int i = 0; i < bytes.size(); i++) {
                addImage(bytes.get(i), document);
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addImage(byte[] bytes, Document document) throws IOException {

        Image img = new Image(ImageDataFactory.create(bytes));

        // img.scaleToFit(document.getPageSize());
        // img.setCompressionLevel(9);

    /*    float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin()) / img.getWidth()) * 100;


        img.scalePercent(scaler);*/
        if (img.getImageWidth() > img.getImageHeight()) {
            img.setRotationAngle(Math.toRadians(270));
            img.scaleAbsolute(737.0079f, 525.58203f);

        }
        document.add(img);
//        document.close();
        document.add(new AreaBreak());
        Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show();


    }

    /* private static void addMetaData(Document document) {
         document.addTitle("My first PDF");
         document.addSubject("Using iText");
         document.addKeywords("Java, PDF, iText");
         document.addAuthor("Jozva");
         document.addCreator("Jozva");
     }

     private static void addTitlePage(Document document)
             throws DocumentException, IOException {
         BaseFont myfont = BaseFont.createFont("assets/fonts/xbzar.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
         Font persianBzarFont = new Font(myfont, 32);
         Paragraph preface = new Paragraph();









      *//*   ColumnText columnText=new ColumnText(writer.getDirectContent());
        columnText.setSimpleColumn(36, 770, 569, 36);
        columnText.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        columnText.addElement(new Paragraph("Arabic", persianBzarFont));
        columnText.addElement(new Paragraph("علی میککیبنتسیبش", persianBzarFont));
        columnText.go();*//*



     *//* // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("جزوه درس مهندسی نرم افزار استاد رامتین خسروی", persianBzarFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        *//**//*preface.add(new Paragraph(
                "با نصب اپلیکیشن جزوا میتوانید تمامی جزوات درس های خود را همیشه به همراه داشته باشید. " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);*//**//*

        preface.add(new Paragraph(
                "با نصب اپلیکیشن جزوا میتوانید تمامی جزوات درس های خود را همیشه به همراه داشته باشید."
                , persianBzarFont));
        addEmptyLine(preface, 3);

        preface.add(new Paragraph(
                "برای اطلاعات بیشتر و دانلود اپلیکیشن میتوانید به سایت ما سر بزنید.",
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "www.jozva.com",
                redFont));

        document.add(preface);*//*
        // Start a new page
        document.newPage();
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("ESTIMATING APP", catFont);
        anchor.setName("ESTIMATING APP");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // Add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // Add a table
        createTable(subCatPart);

        // Now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // Now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Job Name:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Test 001"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("Date:");
        table.addCell("1.1");
        table.addCell("");
        table.addCell("Labor Rate:");
        table.addCell("2.2");
        table.addCell("");
        table.addCell("Labor Cost:");
        table.addCell("3.2");
        table.addCell("3.3");

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }
*/
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

  /*  class EventHelper extends PdfPageEventHelper {
        Font FONT = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD, new GrayColor(0.65f));

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Created by JOZVA.com", FONT), 160f, 40f, writer.getPageNumber() % 2 == 1 ? 0 : -0);
        }
    }*/

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
