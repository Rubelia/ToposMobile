package com.example.laptrinhmobile.toposmobile.Other;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LapTrinhMobile on 10/20/2015.
 */
//direction = 1 : bên trái ; = 0 : bên phải
//
public class PublicFunction {

    private static final String LOCATION = "PublicFunction";
    public PublicFunction() {}
    public String ThemKiTu(String str, String kytu, int chieudai, int direction) {
        String result = new String();
        result = str;
        switch (direction) {
            case 1:
                for (int i = str.length(); i<chieudai;i++) {
                    result = kytu + result;
                }
                break;
            case 0:
                for (int i = str.length(); i<chieudai;i++) {
                    result += kytu;
                }
                break;
        }
        Log.d(LOCATION,"Them ki tu: " + result);
        return result;
    }

    public String dateFormat(String format, Date date) {
        String result = new String();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        result = simpleDateFormat.format(date);
//        Log.d(LOCATION,"dateFormat" + result);
        return result;
    }
    public String ConvertToUnSign(String string) {
        char[] charA = { 'à', 'á', 'ạ', 'ả', 'ã',// 0<16
                'â', 'ầ', 'ấ', 'ậ', 'ẩ', 'ẫ', 'ă', 'ằ', 'ắ', 'ặ', 'ẳ', 'ẵ' };// a,// ă,// â
        char[] charE = { 'ê', 'ề', 'ế', 'ệ', 'ể', 'ễ',// 17--27
                'è', 'é', 'ẹ', 'ẻ', 'ẽ' };// e
        char[] charI = { 'ì', 'í', 'ị', 'ỉ', 'ĩ' };// i 28-&gt;32
        char[] charO = { 'ò', 'ó', 'ọ', 'ỏ', 'õ',// o 33-&gt;49
                'ô', 'ồ', 'ố', 'ộ', 'ổ', 'ỗ',// ô
                'ơ', 'ờ', 'ớ', 'ợ', 'ở', 'ỡ' };// ơ
        char[] charU = { 'ù', 'ú', 'ụ', 'ủ', 'ũ',// u 50-&gt;60
                'ư', 'ừ', 'ứ', 'ự', 'ử', 'ữ' };// ư
        char[] charY = { 'ỳ', 'ý', 'ỵ', 'ỷ', 'ỹ' };// y 61-&gt;65
        char[] charD = { 'đ', ' ' }; // 66-67
        String charact = String.valueOf(charA, 0, charA.length)
                + String.valueOf(charE, 0, charE.length)
                + String.valueOf(charI, 0, charI.length)
                + String.valueOf(charO, 0, charO.length)
                + String.valueOf(charU, 0, charU.length)
                + String.valueOf(charY, 0, charY.length)
                + String.valueOf(charD, 0, charD.length);

        String convertString = string.toLowerCase();
        Character[] returnString = new Character[convertString.length()];
        for (int i = 0; i < convertString.length()-1; i++) {
            char temp = convertString.charAt(i);
            if ((int) temp < 97 || temp > 122) {
                char tam1 = this.GetAlterChar(temp,charact);
                if ((int) temp != 32)
                    convertString = convertString.replace(temp, tam1);
            }
        }
        return convertString;

    }
    private char GetAlterChar(char pC, String charact) {
        if ((int) pC == 32) {
            return ' ';
        }
//        char tam = pC;// Character.toLowerCase(pC);
//        int i = 0;
//        while (i < charact.length()-1 || charact.charAt(i) != tam) {
//            i++;
//        }
//        while(int i=0;i< charact.; i++ ) {
//
//        }
        for(int i=0;i< charact.length();i++)
        {
            if(i == 32){
                return ' ' ;
            } else if (i < 0 || i > 67)
                return pC;
            if (i == 66) {
                return 'd';
            }
            if (i >= 0 || i <= 16) {
                return 'a';
            }
            if (i >= 17 || i <= 27) {
                return 'e';
            }
            if (i >= 28 || i <= 32) {
                return 'i';
            }
            if (i >= 33 || i <= 49) {
                return 'o';
            }
            if (i >= 50 || i <= 60) {
                return 'u';
            }
            if (i >= 61 || i <= 65) {
                return 'y';
            }
        }
        return pC;
    }

    public String UnicodeToNoSign(String source)
    {
        String kq = source;
        String c = "";
        String stra = "áàảãăắằẳẵâấầẩẫạậặ", strA = "ÁÀẢÃẠÂĂẤẦẨẪẬẮẰẲẴẶ";
        String stre = "éèẻẽẹêếềểễệ", strE = "ÉÈẺẼẸÊẾỀỂỄỆ";
        String stri = "íìỉĩịi", strI = "ÍÌỈĨỊ";
        String stro = "óòỏõọôơốồổỗộớờởỡợ", strO = "ÓÒỎÕỌÔƠỐỒỔỖỘỚỜỞỠỢ";
        String stru = "úùủũụưứừửữự", strU = "ÚÙỦŨỤƯỨỪỬỮỰ";
        String stry = "ýỳỷỹỵ", strY = "ÝỲỶỸỴ";
        String strd = "đ", strD = "Đ";
        for (int i = 0;i<source.length()-1;i++) {
            c = source.substring(i, i+1);

            if (stra.contains(c)) kq = kq.replace(c, "a");
            if (strA.contains(c)) kq = kq.replace(c, "A");

            if (stre.contains(c)) kq = kq.replace(c, "e");
            if (strE.contains(c)) kq = kq.replace(c, "E");

            if (stri.contains(c)) kq = kq.replace(c, "i");
            if (strI.contains(c)) kq = kq.replace(c, "I");

            if (stro.contains(c)) kq = kq.replace(c, "o");
            if (strO.contains(c)) kq = kq.replace(c, "O");

            if (stru.contains(c)) kq = kq.replace(c, "u");
            if (strU.contains(c)) kq = kq.replace(c, "U");

            if (stry.contains(c)) kq = kq.replace(c, "y");
            if (strY.contains(c)) kq = kq.replace(c, "Y");

            if (strd.contains(c)) kq = kq.replace(c, "d");
            if (strD.contains(c)) kq = kq.replace(c, "D");

        }
        return kq;
    }
}
