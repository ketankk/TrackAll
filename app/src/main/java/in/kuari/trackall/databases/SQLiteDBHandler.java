package in.kuari.trackall.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import in.kuari.trackall.bean.BookMark;

/**
 * Created by root on 1/31/16.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="track_all";
    private static final int DB_VERSION=7;

    private static final String TABLE_NAME="search_history";
    private static final String SEARCH_ID="_id";
    private static final String TRACK_ID="track_id";
    private static final String COMPANY_NAME="comp_name";
    private static final String COURIER_ID="url";
    private static final String DATE="date";
    private static final String RATING="rating";
    //Added in DB_VERSION 7
    private static final String B_TYPE="b_type";
    private static final String BM_TAG = "tag";

    private static final String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+
            "("+SEARCH_ID + " integer primary key autoincrement,"+
                TRACK_ID+" varchar(20),"+
                COMPANY_NAME+" varchar(20),"+
                COURIER_ID+" varchar(200)," +
                RATING+" REAL, "+
                DATE+" DATE DEFAULT CURRENT_DATE,"+
                BM_TAG+" varchar(50),"+
                B_TYPE +" integer constraint typeN default(1))";//Change on upgrade for alteration..not deletion
private SQLiteDatabase db;
private Context context;

    public SQLiteDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     //   Log.d("create",CREATE_TABLE);
db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion>5) {
            switch (oldVersion) {
                case 6:
                db.execSQL("ALTER TABLE " + TABLE_NAME +
                        " ADD COLUMN " + B_TYPE + " integer constraint typeN default(1) " +
                        "ADD COLUMN " + BM_TAG + " varchar(50) ");
                    break;
            }
        }

        else{
try {
    db.execSQL("ALTER TABLE " + TABLE_NAME
            + " ADD COLUMN " + RATING + " REAL,"
            + " ADD COLUMN " + DATE + " DATE DEFAULT CURRENT_DATE,"
            + " ADD COLUMN " + B_TYPE +" integer  constraint typeN default(1)"
            + " ADD COLUMN " + BM_TAG +" varchar(50)");
}catch (SQLiteException e1) {
    try {
        db.execSQL("ALTER TABLE " + TABLE_NAME
                + " ADD COLUMN " + DATE + " DATE DEFAULT CURRENT_DATE,"
                + " ADD COLUMN " + B_TYPE +" integer  constraint typeN default(1)"
                + " ADD COLUMN " + BM_TAG +" varchar(50)");
    } catch (SQLiteException e2) {

    }
}
}
        onCreate(db);
    }

 public boolean addSearch(BookMark bookMark){
     if(!ifBookMarkExist(bookMark)) {
         db = this.getWritableDatabase();
         SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
         String date = sdf.format(new Date());

         ContentValues values = new ContentValues();
         values.put(TRACK_ID, bookMark.getTrackId().toUpperCase());
         values.put(COMPANY_NAME, bookMark.getName());
         values.put(COURIER_ID, bookMark.getCourierID());
         values.put(RATING, bookMark.getRating());
         values.put(DATE, date);
         values.put(B_TYPE,bookMark.getbType());
         values.put(BM_TAG,bookMark.getBmTag());
Log.d("ll",bookMark.toString());
         db.insert(TABLE_NAME, null, values);
         db.close();
         return true;
     }
     return false;
 }
    public boolean   updateBookmark(BookMark bookMark){
        db=this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRACK_ID, bookMark.getTrackId().toUpperCase());
        values.put(COMPANY_NAME, bookMark.getName());
        values.put(COURIER_ID, bookMark.getCourierID());
        values.put(RATING, bookMark.getRating());
        values.put(B_TYPE,bookMark.getbType());
        values.put(BM_TAG,bookMark.getBmTag());
        Log.d("BM",bookMark.toString());

        int count=db.update(TABLE_NAME,values,SEARCH_ID+"="+bookMark.getId(),null);
        Log.d("BM",bookMark.toString());
        db.close();
        if(count>0)
            return true;
        return false;
    }
    public boolean   deleteBookmark(String id){
        db=this.getReadableDatabase();

          int count=db.delete(TABLE_NAME,SEARCH_ID+"="+id,null);
        db.close();
        if(count>0)
         return true;
        return false;
    }
   public List<BookMark> getAllBookMarks(){
       List<BookMark> searchHistories=new ArrayList<>();
       db=this.getReadableDatabase();

       Cursor cursor=db.query(TABLE_NAME,new String[]{SEARCH_ID,TRACK_ID,COMPANY_NAME,COURIER_ID,DATE,RATING,B_TYPE,BM_TAG},null,null,null,null,null);
    while (cursor.moveToNext()){
    BookMark hist=new BookMark();

    hist.setId(Long.parseLong(cursor.getString(0)));
    hist.setTrackId(cursor.getString(1));
    hist.setName(cursor.getString(2));
    hist.setCourierID(cursor.getString(3));
    hist.setTime(cursor.getString(4));
        hist.setBmTag(cursor.getString(7));
        /*Database before version 6 */
      if(cursor.getString(6)==null)
          hist.setbType(1);
        else
    hist.setbType(Integer.parseInt(cursor.getString(6)));
                /*Database before version 6 */

        float r=cursor.getFloat(5);
        hist.setRating("1.3");
      // Log.d("hist",hist.toString());
    searchHistories.add(hist);
}db.close();
       Collections.reverse(searchHistories);
       return searchHistories;
    }
    private   boolean ifBookMarkExist(BookMark bookMark){
        db=this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME+" WHERE "+TRACK_ID+"='"+bookMark.getTrackId().toUpperCase()+"' and "+COURIER_ID+"='"+bookMark.getCourierID()+"'";

        Cursor cursor=db.rawQuery(query,null);
       // Cursor cursor=db.query(TABLE_NAME, new String[]{SEARCH_ID}, TRACK_ID+"='"+bookMark.getTrackId()+"'", null, null, null, null);

        Log.d("qry",query+cursor.getCount());
        while (cursor.moveToNext()){
           // Log.d("dbb",cursor.getColumnName(0));
            return true;
        }db.close();
        return false;
    }
    public List<BookMark> getAllLatestBookMarks(){
        List<BookMark> bookMarks=getAllBookMarks();

        Date date=new Date();
      //  Log.d("date",date.toString());
        return bookMarks;
    }
}
