package com.skinod.tzzo.skinod.datasave;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CdufSplite {

	/**
	 * 对数据库的增删改查操作。
	 *
	 */

	private DatabaseHelper dbHelper;

	public CdufSplite(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public void add(ContentBean mad) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constant.TYPE, mad.getType());
		values.put(Constant.DTEXT, mad.getText());
		values.put(Constant.EMOTION, mad.getEmotion());
		values.put(Constant.CODE, mad.getCode());
		values.put(Constant.VIEWFLAG, mad.getViewFlag());
		db.insert(Constant.CHAT_TABLE_NAME, null, values);
	}

	public void deleteAll() {
		dbHelper.getWritableDatabase().execSQL("DELETE FROM " + Constant.CHAT_TABLE_NAME);
	}

/*	public void delete(int type) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(Constant.ACHIEVEMENT_TABLE_NAME, Constant.TYPE + "=?",
				new String[] { String.valueOf(type) });
	}*/

/*	public void updata(ContentBean mad) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constant.DTEXT, mad.getText());
		values.put(Constant.EMOTION, mad.getEmotion()+"");
		values.put(Constant.CODE, mad.getCode()+"");
		values.put(Constant.VIEWFLAG, mad.getViewFlag());
		db.update(Constant.ACHIEVEMENT_TABLE_NAME, values,
				Constant.TYPE + "=?",
				new String[] { String.valueOf(mad.getType()) });
	}*/
	
/*
	public void updataBytype(ContentBean mad,int type) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constant.DTEXT, mad.getText());
		values.put(Constant.EMOTION, mad.getEmotion()+"");
		values.put(Constant.CODE, mad.getCode()+"");
		values.put(Constant.VIEWFLAG, mad.getViewFlag());
		db.update(Constant.CHAT_TABLE_NAME, values,
				Constant.TYPE + "=?",
				new String[] { String.valueOf(type) });
	}
*/

	public ArrayList<ContentBean> findall() {
		ArrayList<ContentBean> myList=new ArrayList<>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(Constant.CHAT_TABLE_NAME, null, null,
				null, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				ContentBean myachi = new ContentBean();
			//	int ID = cursor.getInt(cursor.getColumnIndex(Constant.ID));
				int type = cursor.getInt(cursor.getColumnIndex(Constant.TYPE));
				String text = cursor.getString(cursor.getColumnIndex(Constant.DTEXT));
				int emotion = cursor.getInt(cursor.getColumnIndex(Constant.EMOTION));
				int code = cursor.getInt(cursor.getColumnIndex(Constant.CODE));
				String viewflag = cursor.getString(cursor.getColumnIndex(Constant.VIEWFLAG));
				myachi.setType(type);
				myachi.setText(text);
				myachi.setEmotion(emotion);
				myachi.setCode(code);
				myachi.setViewFlag(viewflag);
				myList.add(myachi);
			}
		}
		if(cursor!=null)cursor.close();
		return myList;
	}

/*	public ContentBean findByType(int type) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(Constant.ACHIEVEMENT_TABLE_NAME, null,
				Constant.TYPE + "=?", new String[] { String.valueOf(type) },
				null, null, null);
		ContentBean myachi = null;
		if (cursor != null && cursor.moveToFirst()) {
			myachi = new ContentBean();
			int ID = cursor.getInt(cursor.getColumnIndex(Constant.ID));
			String SERIANO = cursor.getString(cursor
					.getColumnIndex(Constant.SERIANO));
			int DEGREE = cursor.getInt(cursor.getColumnIndex(Constant.DEGREE));
			int USERID = cursor.getInt(cursor.getColumnIndex(Constant.USERID));
			int TYPE = cursor.getInt(cursor.getColumnIndex(Constant.TYPE));
			long REACHDATE = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(Constant.REACHDATE)));
			long CREATEDATE = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(Constant.CAREATEDATE)));
			long UPDATEDATE = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(Constant.UPDATEDATE)));
			int ISREACH = cursor
					.getInt(cursor.getColumnIndex(Constant.ISREACH));

			myachi.setId(ID);
			myachi.setSerialNo(SERIANO);
			myachi.setDegree(DEGREE);
			myachi.setUserId(USERID);
			myachi.setType(TYPE);
			myachi.setReachDate(REACHDATE);
			myachi.setCreateDate(CREATEDATE);
			myachi.setUpdateDate(UPDATEDATE);
			myachi.setIsReach(ISREACH);
		}
		// sxf add
		cursor.close();
		return myachi;
	}*/
}
