package in.kuari.trackall.databases;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.kuari.trackall.bean.SearchHistory;

/**
 * Created by root on 1/31/16.
 */
public class SQLiteDBHandler extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="track_all";
    private static final int DB_VERSION=3;

    private static final String TABLE_NAME="search_history";
    private static final String SEARCH_ID="_id";
    private static final String TRACK_ID="track_id";
    private static final String COMPANY_NAME="comp_name";
    private static final String COURIER_ID="url";
    private static final String TIMING="time";


    private static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+
            "("+SEARCH_ID + " integer primary key autoincrement,"+
                TRACK_ID+" varchar(20),"+
                COMPANY_NAME+" varchar(20),"+
                COURIER_ID+" varchar(200)," +
                TIMING+" DATE DEFAULT CURRENT_DATE)";//Change on upgrade for alteration..not deletion
private SQLiteDatabase db;


    public SQLiteDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

 public boolean   addSearch(SearchHistory searchHistory){
     db=this.getWritableDatabase();
     SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
     String date=sdf.format(new Date());

     ContentValues values=new ContentValues();
     values.put(TRACK_ID,searchHistory.getTrackId());
     values.put(COMPANY_NAME,searchHistory.getName());
     values.put(COURIER_ID,searchHistory.getCourierID());
     values.put(TIMING,date);
     db.insert(TABLE_NAME,null,values);
db.close();
return true;
 }
    public boolean   deleteHistory(String id){
        db=this.getReadableDatabase();

          int count=db.delete(TABLE_NAME,SEARCH_ID+"="+id,null);
        db.close();
        if(count>0)
         return true;
        return false;
    }
   public List<SearchHistory> getAllSearches(){
       List<SearchHistory> searchHistories=new ArrayList<>();
       db=this.getReadableDatabase();

       Cursor cursor=db.query(TABLE_NAME,new String[]{SEARCH_ID,TRACK_ID,COMPANY_NAME,COURIER_ID,TIMING},null,null,null,null,null);
    while (cursor.moveToNext()){
    SearchHistory hist=new SearchHistory();

    hist.setId(Long.parseLong(cursor.getString(0)));
    hist.setTrackId(cursor.getString(1));
    hist.setName(cursor.getString(2));
    hist.setCourierID(cursor.getString(3));
    hist.setTime(cursor.getString(4));
       // Log.d("hist",hist.toString());
    searchHistories.add(hist);
}db.close();
       return searchHistories;
    }
}
