package com.skinod.tzzo.skinod.datasave;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.skinod.tzzo.skinod.utils.LogUtil;


public class DatabaseHelper extends SQLiteOpenHelper{  
	
    private static final int VERSION = 1;  
    private static final String SWORD="SWORD";

    private DatabaseHelper(Context context, String name, CursorFactory factory,
            int version) {  
        super(context, name, factory, version);  
          
    }  
    public DatabaseHelper(Context context,String name){ 
        this(context,name,VERSION);  
    }  
    private DatabaseHelper(Context context,String name,int version){
        this(context, name,null,version);  
    }

    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.d("create a Local Database");
       // String sql = "create table chatting(id int primary key auto_increment,type int,text varchar(250),emotion int,code int,viewFlag varchar(50))";
        StringBuffer buffer=new StringBuffer();
        buffer.append("CREATE TABLE chatting (id INTEGER PRIMARY KEY,")
                .append("type int,")
                .append("text varchar(232),")
                .append("emotion int,")
                .append("code int,")
                .append("viewflag varchar(13))");
        db.execSQL(buffer.toString());
	}


}
