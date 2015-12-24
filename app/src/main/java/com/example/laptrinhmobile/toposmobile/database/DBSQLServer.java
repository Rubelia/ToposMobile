package com.example.laptrinhmobile.toposmobile.database;

import android.os.Environment;

import java.sql.*;
import java.io.*;
/**
 * Created by LapTrinhMobile on 10/28/2015.
 */
public class DBSQLServer {
    public DBSQLServer() {}

    public Connection dbConnect(String db_connect_string,
                                String db_userid, String db_password)
    {
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    db_connect_string, db_userid, db_password);

            System.out.println("connected");
            return conn;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void insertImage(Connection conn,String img)
    {
        int len;
        String query;
        PreparedStatement pstmt;

        try
        {
            File file = new File(img);
            FileInputStream fis = new FileInputStream(file);
            len = (int)file.length();

            query = ("insert into TableImage VALUES(?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1,file.getName());
            pstmt.setInt(2, len);

            // Method used to insert a stream of bytes
            pstmt.setBinaryStream(3, fis, len);
            pstmt.executeUpdate();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getImageData(Connection conn)
    {
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File image = new File(sdCardDirectory, "test.png");

//        FileOutputStream outStream;
        byte[] fileBytes;
        String query;
        try
        {
            query = "select HinhAnh from HangHoa_HinhAnh_Mobile";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);
            if (rs.next())
            {
                fileBytes = rs.getBytes(1);
                OutputStream targetFile=
                        new FileOutputStream(image);

                targetFile.write(fileBytes);
                targetFile.close();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
