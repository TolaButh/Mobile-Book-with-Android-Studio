package tolabuth.mobilebook.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tolabuth.mobilebook.model.Contact;

public class SQLiteHelper extends SQLiteOpenHelper {
    private Context context;
    private static String DATABASE_NAME="mobilebooks_db";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "contacts";
    private static String COLUMN_ID = "id";
    private static String COLUMN_NAME = "name";
    private  static String COLUMN_MOBILE = "mobile";
    private  static String COLUMN_IMAGE = "image";


    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                +" (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_MOBILE+" TEXT NOT NULL,"
                + COLUMN_IMAGE + " TEXT NOT NULL"
                +  ") ";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
     db.execSQL(sql);


    }
    public boolean insert(Contact contact){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_MOBILE, contact.getMobile());
        values.put(COLUMN_IMAGE, contact.getImage());
        long row = db.insert(TABLE_NAME,null,values);
        if (row>0)
            result = true;
        db.close();
        return  result;
    }
    public boolean update(Contact contact){
        boolean result =false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_MOBILE, contact.getMobile());
        values.put(COLUMN_IMAGE,contact.getImage());

        int row = db.update(TABLE_NAME, values, COLUMN_ID +  " = ?", new String[]{String.valueOf(contact.getId())});
        if (row>0)
            result = true;
        db.close();
        return result;
    }
    public  boolean delete(int id){
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        int row = db.delete(TABLE_NAME, COLUMN_ID  + " = ?", new String[]{String.valueOf(id)});
        if (row>0)
            result = true;
        db.close();
        return result;
    }
    public void delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM "+ TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }
    public List<Contact> select(){
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(sql,null);
        if ((cursor.moveToFirst())){
            do{
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String mobile =cursor.getString(2);
            String image = cursor.getString(3);
            Contact contact = new Contact(id,name,mobile,image);
            contacts.add(contact);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;

    }
    public List<Contact> selectByName(String s){
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+ TABLE_NAME + " WHERE name LIKE '%" + s + "%'";

        Cursor cursor = db.rawQuery(sql, null);
        if ((cursor.moveToFirst())){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String mobile =cursor.getString(2);
                String image = cursor.getString(3);


                Contact contact = new Contact(id,name,mobile,image);
                contacts.add(contact);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }
    public Contact selectById(int ids){
        Contact contact = new Contact();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+ TABLE_NAME + " WHERE id = " + ids;
        Cursor cursor = db.rawQuery(sql, null);
        if ((cursor.moveToFirst())){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String mobile = cursor.getString(2);
            String image = cursor.getString(3);

            contact.setId(id);
            contact.setName(name);
            contact.setMobile(mobile);
            contact.setImage(image);

        }
        cursor.close();
        db.close();
        return  contact;
    }

}

