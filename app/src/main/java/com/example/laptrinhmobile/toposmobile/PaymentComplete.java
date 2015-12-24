package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.Object.CTHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.HangHoa;
import com.example.laptrinhmobile.toposmobile.Object.HoaDon;
import com.example.laptrinhmobile.toposmobile.Object.ThanhToanHoaDon;
import com.example.laptrinhmobile.toposmobile.Object.Timer;
import com.example.laptrinhmobile.toposmobile.Object.TypePayment;
import com.example.laptrinhmobile.toposmobile.Other.HomeKeyLocker;
import com.example.laptrinhmobile.toposmobile.Other.MyImageRenderListener;
import com.example.laptrinhmobile.toposmobile.database.SQLController;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class PaymentComplete extends Activity {
//    public static final String RESULT = "results/part4/chapter15/Img%s.%s";

    Activity context = this;
    private static final String LOCATION = "PaymentComplete";
    ArrayList<HangHoa> arr_HangHoa;
//    ArrayList<TypePayment> arr_TypePayment;
//    HoaDon hoaDon;
    ArrayList<ThanhToanHoaDon> arr_ThanhToanHoaDon;
    String namePDF ;
    String pathPDF ;
    boolean isSavePDF = false;
    int totalQuantity = 0;
    double totalSum = 0, moneyCus = 0, moneyReturn = 0;
    String formattedQuantity ;
    DecimalFormat formatterQuantity;
    BaseFont urName;
    Font times;
    Timer timerKetThuc;
    SQLController sqlController;
    File filePDF;

    private HomeKeyLocker mHomeKeyLocker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_complete);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mHomeKeyLocker = new HomeKeyLocker();
        mHomeKeyLocker.lock(this);

        String HoaDonID = "";
        sqlController = new SQLController(context);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            arr_HangHoa = (ArrayList<HangHoa>) extras.getSerializable("ArrHangHoa");
            timerKetThuc = (Timer) extras.getSerializable("Timer");
            HoaDonID = extras.getString("HoaDonID");
            arr_ThanhToanHoaDon = (ArrayList<ThanhToanHoaDon>) extras.getSerializable("ArrThanhToanHoaDon");
        }

        saveToDatabase(arr_ThanhToanHoaDon, HoaDonID, timerKetThuc);
//        saveToDatabase(arr_HangHoa,arr_TypePayment,hoaDon,timerKetThuc);
        try {
            urName = BaseFont.createFont("assets/VTIMESN.TTF", "UTF-8",BaseFont.CACHED);
            times = new Font(urName, 12);
        } catch (DocumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        TextView txtViewNewTransaction = (TextView) findViewById(R.id.txtViewNewTransaction);
        txtViewNewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_new_transaction = new Intent(getApplicationContext(),Sales.class);
                view_new_transaction.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(view_new_transaction);
                finish();
            }
        });

        TextView txtEmailReceipt = (TextView) findViewById(R.id.txtViewEmailReceipt);
        txtEmailReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CreatNewFileInSdCard();
                } catch (DocumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Đang tạo...", Toast.LENGTH_LONG).show();
                if (isSavePDF) {
                    sharePDF();
                    isSavePDF = false;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Không tạo được file PDF để chia sẻ");
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        try {
            CreatNewFileInSdCard();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Uri path = Uri.fromFile(filePDF);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);                          //Start Activity
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(),
                    "No Application Available to View PDF",
                    Toast.LENGTH_SHORT).show();
        }
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        Bitmap bmp =drawTextToBitmap(this,R.drawable.backgroudlist,"Hello Android");
//
//        imageView.setImageBitmap(bmp);


    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        Toast.makeText(getApplicationContext(),"Just press Back button", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHomeKeyLocker.unlock();
        mHomeKeyLocker = null;
    }
    public String CreateFolder() {
        String path = "";
        File folder = new File(Environment.getExternalStorageDirectory() + "/PDF");
//        System.out.println(Environment.getExternalStorageDirectory());
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            // Do something on success
            path = Environment.getExternalStorageDirectory() + "/PDF" ;
//    		Toast.makeText(getApplicationContext(), "Created folder success", Toast.LENGTH_LONG).show();
        } else {
            // Do something else on failure
            System.out.println("Tạo thư mục mới thất bại");
        }
        return path;
    }
    public void CreatNewFileInSdCard() throws DocumentException {
        namePDF = "";
        pathPDF = "";
        String date = "" + timerKetThuc.getDay()+"-" + timerKetThuc.getMonth() +"-" + timerKetThuc.getYear() ;
        String nameFile = "BC"  + date;
        namePDF = nameFile;
        String path = CreateFolder();
        pathPDF = path;
        writeDataToPDF(path, nameFile);
    }

    public void writeDataToPDF(String path, String nameFile) {
        File file;
        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter docWriter = null;
        totalSum = 0;
        totalQuantity =0;

        formatterQuantity = new DecimalFormat("#.###");

        BaseFont bf;
        Font font12;
        Font fontBold12 ;
		Font fontBold16 ;
//        Font fontBold20 = new Font();

//		Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
//		Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12);
        try {
//			String link = getAssets()+"VTIMESN.TFF";
//			BaseFont bf = BaseFont.createFont("VTIMESN.TFF",BaseFont.CP1250,BaseFont.EMBEDDED);
//			Font a = new Font

//			font = new Font(bf,12);
            bf = BaseFont.createFont("/assets/TIMES.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font12 = new Font(bf,12);
            fontBold12 = new Font(bf,12,Font.BOLD);
		    fontBold16 = new Font(bf,14,Font.BOLD);
//            fontBold20 = new Font(bf,20,Font.BOLD);

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();
//            Log.d("PDFCreator", "PDF Path: " + path);
            file = new File(dir, nameFile+ ".pdf");
            filePDF = new File(dir, nameFile+ ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            docWriter = PdfWriter.getInstance(doc, fOut);

            doc.open();

            //list number in begin
//            List orderedList = new List(List.ORDERED);
//            orderedList.add(new ListItem("Item 1"));
//            orderedList.add(new ListItem("Item 2"));
//            orderedList.add(new ListItem("Item 3"));
//
//            doc.add(orderedList);
            //tao mới paragraph p1
            Paragraph p1 = new Paragraph("Simply Mart \n 273 Pham Van Chieu \n HOA DON BAN LE \n ---o0o--- \n Liên: 1 \n");
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(fontBold16);
            doc.add(p1);

            p1 = new Paragraph();
            float[] col = {1f, 1f};
            PdfPTable table1 = new PdfPTable(col);
            table1.setWidthPercentage(100f);
            String time = timerKetThuc.getDay()+"/" + timerKetThuc.getMonth() + "/" +timerKetThuc.getYear() + " " + timerKetThuc.getHour() + ":" + timerKetThuc.getMinutes();
            insertCell(table1, time, Element.ALIGN_LEFT, 1, fontBold12) ;
            insertCell(table1, "Quầy 1", Element.ALIGN_RIGHT, 1, fontBold12) ;
            p1.add(table1);
            p1.setFont(fontBold16);


            Phrase phrase = new Phrase("----------------------------------------------------------------------------------------------------------");

            p1.add(phrase);

//            phrase.add()
//            doc.add(p1);
            //specify column widths

            float[] columnWidths = {1f,1f,1f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);

//            insertCell(table,"--------------",com.itextpdf.text.Element.ALIGN_LEFT,2,font12);

            //insert column heading

            insertCell(table, "Đơn giá", Element.ALIGN_LEFT, 1, fontBold12);
            insertCell(table,"Số lượng",Element.ALIGN_CENTER, 1, fontBold12);
            insertCell(table,"Thành Tiền",Element.ALIGN_RIGHT, 1, fontBold12);
            table.setHeaderRows(1);

            //insert empty row

//            List listHH = new List(List.ORDERED);
//            table.add
            for(int i=0;i<arr_HangHoa.size();i++)
            {

                double total ;
                HangHoa temp = arr_HangHoa.get(i);
//                listHH.add(new ListItem(temp.getTenHH()));
                insertCell(table, (i+1) + ". " +temp.getTenHH(), Element.ALIGN_LEFT, 3, font12);

                insertCell(table,"\t"+temp.getMaHH()+"", Element.ALIGN_LEFT, 1, font12);
                insertCell(table,"VAT:  " +temp.getVATDauRa()+"%", Element.ALIGN_CENTER, 1, font12);
                insertCell(table,"", Element.ALIGN_CENTER, 1, font12);

//                String number = temp.getTienBan();
//                double amount = Double.parseDouble(number);
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formatted = formatter.format(temp.getTienBan());

                insertCell(table,"\t"+formatted+"", Element.ALIGN_LEFT, 1, font12);
                formattedQuantity = formatterQuantity.format(temp.getQuantity());
                insertCell(table,formattedQuantity, Element.ALIGN_CENTER, 1, font12);
                total = temp.getTienBan() * temp.getQuantity();
                formatted = formatter.format(temp.getTienBan()*temp.getQuantity());
                insertCell(table,formatted+"", Element.ALIGN_RIGHT, 1, font12);

                totalQuantity += temp.getQuantity();
                totalSum += total;
                totalSum += total;
            }

            p1.add(table);

            phrase = new Phrase("----------------------------------------------------------------------------------------------------------");
            p1.add(phrase);

            PdfPTable tableSum = new PdfPTable(columnWidths);
            tableSum.setWidthPercentage(100f);
            DecimalFormat formatter = new DecimalFormat("#,###");
            formattedQuantity = formatterQuantity.format(totalQuantity);

            insertCell(tableSum,"Tổng",Element.ALIGN_LEFT,1,fontBold12);
            insertCell(tableSum,formattedQuantity,Element.ALIGN_CENTER,1,fontBold12);
            String formatted = formatter.format(totalSum);
            insertCell(tableSum,formatted,Element.ALIGN_RIGHT,1,fontBold12);
            insertCell(tableSum,"Chiết khấu hóa đơn",Element.ALIGN_LEFT,2,fontBold12);
            insertCell(tableSum,"",Element.ALIGN_RIGHT,1,fontBold12);
            insertCell(tableSum,"Tồng tiền ",Element.ALIGN_LEFT,2,fontBold12);
            formatted = formatter.format(totalSum);
            insertCell(tableSum,formatted,Element.ALIGN_RIGHT,1,fontBold12);
            insertCell(tableSum,"Tiền khách trả",Element.ALIGN_LEFT,2,fontBold12);
            formatted = formatter.format(moneyCus);
            insertCell(tableSum,formatted,Element.ALIGN_RIGHT,1,fontBold12);
            insertCell(tableSum,"Tiền trả lại cho khách",Element.ALIGN_LEFT,2,fontBold12);
            formatted = formatter.format(moneyReturn);
            insertCell(tableSum, formatted, Element.ALIGN_RIGHT,1,fontBold12);

            p1.add(tableSum);
            phrase = new Phrase("----------------------------------------------------------------------------------------------------------");
            p1.add(phrase);


            doc.add(p1);
            isSavePDF = true;

            //set footer
//            Phrase footerText = new Phrase("This is an example of a footer");
//            HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
//            doc.setFooter(pdfFooter);
//            Header pdfHeader = new Header(footerText, false);	s

//            Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
        } catch (DocumentException de){
            Log.e("Payment Complete"," - PDFCreater Document Exception:" + de);
        } catch (IOException e) {
            // handle exception
            e.printStackTrace();
        }
        finally
        {

            doc.close();
            if (docWriter != null){
                //close the writer
                docWriter.close();
            }
        }
    }
    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font

        Phrase temp = new Phrase(text.trim(),font);
        PdfPCell cell = new PdfPCell(temp);
        cell.setBorder(Rectangle.NO_BORDER);
//		  System.out.println("Test write into PDF: " + temp.toString());
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }
    public void sharePDF() {
        try {
            File fileUri = new File(Environment.getExternalStorageDirectory() +"/PDF" ,namePDF +".pdf");
//			Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() +"/PDF" ,namePDF));
//            System.out.println("Path fileUri: "+fileUri.getAbsolutePath());

            Uri uri = Uri.fromFile(fileUri);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("application/pdf*");

//	        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	        sharingIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"xuan.thang0512@gmail.com"} );
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Gửi file Báo cáo PDF");
            sharingIntent.putExtra(Intent.EXTRA_TEXT,"Báo cáo bán hàng");
//            Log.v(getClass().getSimpleName(), "Uri=" + uri.toString());
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            //        System.out.println("Tên file: " + namePDF);
            //        System.out.println("tên path: " + pathPDF);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToDatabase(ArrayList<ThanhToanHoaDon> in_ArrThanhToanHoaDon,String HoaDonID, Timer in_Timer) {
        updateHoaDon(HoaDonID, in_Timer);
        InsertThanhToanHoaDon(in_ArrThanhToanHoaDon, HoaDonID, in_Timer);
    }

    public void updateHoaDon(String in_HoaDonID, Timer timer) {
        String GioKetThuc = timer.getGio() + "";
        String tableName = "HoaDon" + timer.getStrMonth() + timer.getYear();
        sqlController.open();
        sqlController.updateHoaDon(in_HoaDonID, GioKetThuc, tableName);
        sqlController.close();
    }
    public void InsertThanhToanHoaDon(ArrayList<ThanhToanHoaDon> in_ArrThanhToanHoaDon,String HoaDonID, Timer timer) {
        String tableNameCTHoaDon = "CTHoaDon" + timer.getStrMonth() + timer.getYear();
//        String tableNameHoaDon = "HoaDon" + timer.getStrMonth() + timer.getYear();
        String tableNameThanhToanHoaDon = "ThanhToanHoaDon" + timer.getStrMonth() + timer.getYear();
        for(int i=0; i<in_ArrThanhToanHoaDon.size();i++) {
            ThanhToanHoaDon thanhToanHoaDon = new ThanhToanHoaDon();

            thanhToanHoaDon.setHoaDonID(HoaDonID);
            thanhToanHoaDon.setMaHinhThuc("0001");
            thanhToanHoaDon.setMaNhomThanhToan("001");
            thanhToanHoaDon.setMathe("VND");
            thanhToanHoaDon.setChuThe("NULL");
            sqlController.open();
            double thanhtienban = sqlController.GetThanhTienInCTHoaDon(HoaDonID,tableNameCTHoaDon);
            thanhToanHoaDon.setThanhTien(thanhtienban);
            thanhToanHoaDon.setTyGiaNgoaiTe(1.00);
            thanhToanHoaDon.setThanhTienQuyDoi(thanhtienban * 1.00);
            thanhToanHoaDon.setTLFee(0.000);
            thanhToanHoaDon.setDaVanChuyen(0);
            int STT = sqlController.getCountForSTT(HoaDonID,tableNameThanhToanHoaDon) +1;
            thanhToanHoaDon.setSTT(STT);
            sqlController.InsertThanhToanHoaDon(tableNameThanhToanHoaDon, thanhToanHoaDon );
            sqlController.close();
        }
        Log.d(LOCATION, "Hoàn thất insert ThanhToanHoaDon");


    }

//    public Bitmap drawTextToBitmap(Context mContext,  int resourceId,  String mText) {
//        try {
//            Resources resources = mContext.getResources();
//            float scale = resources.getDisplayMetrics().density;
//            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
//
//            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
//            // set default bitmap config if none
//            if(bitmapConfig == null) {
//                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
//            }
//            // resource bitmaps are imutable,
//            // so we need to convert it to mutable one
//            bitmap = bitmap.copy(bitmapConfig, true);
//
//            Canvas canvas = new Canvas(bitmap);
//            // new antialised Paint
//            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            // text color - #3D3D3D
//            paint.setColor(Color.rgb(110, 110, 110));
//            // text size in pixels
//            paint.setTextSize((int) (12 * scale));
//            // text shadow
//            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
//
//            // draw text to the Canvas center
//            Rect bounds = new Rect();
//            paint.getTextBounds(mText, 0, mText.length(), bounds);
//            int x = (bitmap.getWidth() - bounds.width())/6;
//            int y = (bitmap.getHeight() + bounds.height())/5;
//
//            canvas.drawText(mText, x * scale, y * scale, paint);
//
//            return bitmap;
//        } catch (Exception e) {
//            // TODO: handle exception
//            return null;
//        }
//    }

//    public void extractImages(String filename) throws IOException, DocumentException {
//        PdfReader reader = new PdfReader(filename);
//        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
//        MyImageRenderListener listener = new MyImageRenderListener(RESULT);
//        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
//            parser.processContent(i, listener);
//        }
//    }

}
