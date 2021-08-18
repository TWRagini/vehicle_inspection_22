package com.melayer.vehicleftprnew;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by twtech on 5/2/18.
 */

public class DatabaseOperation {

    Context mContext;
    SQLiteDatabase database;
    String TableName;
    String Tag = "DatabaseOperation";
    //String uDB=Environment.getExternalStorageDirectory().getPath() + "/TWDragondroidDB/UserInfo.db";
    String vDB = Environment.getExternalStorageDirectory().getPath() + "/VehicleFTPRDB/VehicleInfo.db";
    String pDB = Environment.getExternalStorageDirectory().getPath() + "/VehicleFTPRDB/userInfo.db";


    public DatabaseOperation(Context context) {
        this.mContext = context;
    }

    public void vehicleDetail(String vName, String vCode) {
        // new MyLogger().storeMassage("userDetails method", "Called");
        try {
            SQLiteDatabase database = mContext.openOrCreateDatabase(vDB, mContext.MODE_PRIVATE, null);
            String sqlQuery1 = "create table if not exists vehicle (vehiclName varchar(50),vehicleCode varchar(50))";
            database.execSQL(sqlQuery1);
            database.execSQL("insert into vehicle (vehiclName,vehicleCode) values('" + vName + "','" + vCode + "')");
            database.close();
            // new MyLogger().storeMassage("insert details", "Successfully");
        } catch (Exception e) {
            // new MyLogger().storeMassage(Tag,"Exception in userDetail() "+e.getMessage());

            Log.e("Exception while OTP ", e.getMessage());
        }
    }

    public void updateOTP2(String vName, String vCode) {
        try {
            SQLiteDatabase database = mContext.openOrCreateDatabase(vDB, mContext.MODE_PRIVATE, null);
            database.execSQL("update vehicle set vehiclName='" + vName + "', vehicleCode='" + vCode + "'");
            database.close();
            Log.e("OTP  updated  ", "successfully ! ! ! !");
        } catch (Exception e) {
            new MyLogger().storeMassage(Tag, "Exception in updateOTP2() " + e.getMessage());

            Log.e("Exception while ", "Updating OTP");
        }
    }

    public String[] RetrieveRegData() {


        String vehicleData = Environment.getExternalStorageDirectory().getPath() + "/VehicleFTPRDB/VehicleInfo.db";

        String returnData = "finished", srNo = "";
        StringBuffer sb, sb1;

        File dbFile = mContext.getDatabasePath(vehicleData);
        // new MyLogger().storeMassage("DatabaseName", databaseName);
        //new DatabaseOperations(mContext).storeRegularLog("1", "");

        //if (dbFile.exists()) {


        database = mContext.openOrCreateDatabase(vehicleData, Context.MODE_PRIVATE, null);


        // if (totalCount != 0) {

        // String sqlQuery = "select vehiclName from  vehicle  where Status='Null' order by SrNo desc limit 100";
        String sqlQuery = "select * from  vehicle limit 100";

        Cursor cr = database.rawQuery(sqlQuery, null);
////////////////////////////////
        String[] array = new String[cr.getCount()];
        int i = 0;
        while (cr.moveToNext()) {
            String uname = cr.getString(cr.getColumnIndex("vehiclName"));
            array[i] = uname;
            i++;
        }
//////////////////////////////////////
//            sb = new StringBuffer();
//
//            cr.moveToFirst();
//            while (!cr.isAfterLast()) {
//                String str2 = cr.getString(cr.getColumnIndex("vehiclName"));
//                sb.append(str2 + "\n");
//
//                cr.moveToNext();
//            }
//
//            cr.close();
//
//            returnData = sb.toString();
//
//
//            Log.e("Return Data", returnData);


        // }

        return array;


    }

    /////////

    public void storeUserName(String uName) {
        // new MyLogger().storeMassage("userDetails method", "Called");
        try {
            SQLiteDatabase database = mContext.openOrCreateDatabase(pDB, mContext.MODE_PRIVATE, null);
            String sqlQuery1 = "create table if not exists user (SrNo INTEGER PRIMARY KEY AUTOINCREMENT,userName varchar(20))";
            database.execSQL(sqlQuery1);
            database.execSQL("insert into user (userName) values('" + uName + "')");
            database.close();
            // new MyLogger().storeMassage("insert details", "Successfully");
        } catch (Exception e) {
            // new MyLogger().storeMassage(Tag,"Exception in userDetail() "+e.getMessage());

            Log.e("Exception while OTP ", e.getMessage());
        }
    }


    public void updateUser(String uName) {
        try {
            SQLiteDatabase database = mContext.openOrCreateDatabase(pDB, mContext.MODE_PRIVATE, null);
            database.execSQL("update user set userName='" + uName + "'");
            database.close();
            Log.e("OTP  updated  ", "successfully ! ! ! !");
        } catch (Exception e) {
            new MyLogger().storeMassage(Tag, "Exception in updateOTP2() " + e.getMessage());

            Log.e("Exception while ", "Updating OTP");
        }
    }

    public String retrieveUser() {
        String uName;
        SQLiteDatabase database = mContext.openOrCreateDatabase(pDB, mContext.MODE_PRIVATE, null);

        String sqlQuery = "select userName from  user ";

        Cursor cr = database.rawQuery(sqlQuery, null);
        cr.moveToFirst();
        uName = cr.getString(cr.getColumnIndex("userName"));
        cr.close();
        return uName;
    }


    public void deleteUser(File vehicleDB) {
        // SQLiteDatabase database = mContext.openOrCreateDatabase(uDB, mContext.MODE_PRIVATE, null);
        SQLiteDatabase.deleteDatabase(vehicleDB);

    }

//    public String retrieveVehicleCode(String vehicleNo) {
//        String vehicleCode = "";
//        //SQLiteDatabase database = mContext.openOrCreateDatabase(vDB, mContext.MODE_PRIVATE, null);
//        SQLiteDatabase database = mContext.openOrCreateDatabase(vDB, mContext.MODE_PRIVATE, null);
//
//        //String query = " SELECT vehicleCode from  vehicle where vehiclName = ' " + vehicleNo + " ' ";
//
//        String sqlQuery = "select vehicleCode from  vehicle where vehiclName = " +vehicleNo+ " ";
//
//        return vehicleCode;
//    }
    ///////////
    public String retriveVehicleCode(String vehicleNo)
    {
        String vehicleCode = "";
        SQLiteDatabase database = mContext.openOrCreateDatabase(vDB, mContext.MODE_PRIVATE, null);

       // String sqlQuery = "select vehicleCode from  vehicle where vehiclName = " +vehicleNo+ " ";

        String sqlQuery = "SELECT vehicleCode FROM 'vehicle' where vehiclName ='" +vehicleNo+ "'LIMIT 0,1";

       // SELECT vehicleCode FROM 'vehicle' where vehiclName = 'GJ 27 TT 0258' LIMIT 0,1

        Cursor cr = database.rawQuery(sqlQuery, null);
        cr.moveToFirst();
        vehicleCode = cr.getString(cr.getColumnIndex("vehicleCode"));
        cr.close();
        return vehicleCode;
    }


}

