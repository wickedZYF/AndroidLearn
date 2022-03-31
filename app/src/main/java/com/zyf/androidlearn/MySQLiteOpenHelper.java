package com.zyf.androidlearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.zyf.androidlearn.Bean.User;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import static com.zyf.androidlearn.MainActivity.LoginUser;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME ="mySQLite.db";
    public static final String TABLE_NAME_USER ="user";

    public static final String CREATE_TABLE_SQL="create table "+TABLE_NAME_USER+" (id integer primary key autoincrement, name text ,password text,cardId text)";

    public MySQLiteOpenHelper(Context context){
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 给数据库添加用户
     * @param user
     * @return
     */
    public long insertData(User user){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",user.getName());
        values.put("password",user.getPwd());
        values.put("cardId",user.getCardId());

        return db.insert(TABLE_NAME_USER,null,values);
    }

    /**
     * 查询数据库name列是有重复用户名
     * @param name
     * @return
     */
    public Cursor queryFromDbByName(String name){
        //调用DBOpenHelper
        SQLiteDatabase db=getWritableDatabase();
        //根据画面上输入的账号去数据库中进行查询
        Cursor cursor=db.query(TABLE_NAME_USER,new String[]{"name"},"name like?",new String[]{name},null,null,null);
       return cursor;
    }

    /**
     * 查询数据库cardId列是有重复d的8位uid
     * @param cardId
     * @return
     */
    public Cursor queryFromDbByCardId(String cardId){
        //调用DBOpenHelper
        SQLiteDatabase db=getWritableDatabase();
        //根据画面上输入的账号去数据库中进行查询
        Cursor cursor=db.query(TABLE_NAME_USER,new String[]{"cardId"},"name like?",new String[]{cardId},null,null,null);
        return cursor;
    }

    /**
     * 查询数据库name和password列用于登录
     * @param password
     * @return
     */
    public Cursor queryFromDbByPassword(String password){
        //调用DBOpenHelper
        SQLiteDatabase db=getWritableDatabase();
        //根据画面上输入的账号去数据库中进行查询
        Cursor cursor=db.query(TABLE_NAME_USER,new String[]{"password"},"password like?",new String[]{password},null,null,null);
        return cursor;
    }

    /**
     * 显示登录用户对应数据库uid
     */
    public List<User> queryFromDbByCardId2(String name){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME_USER,null,"name like ?",new String[]{name},null,null,null);
        List<User> userList=new ArrayList<>();
        if (cursor!=null){
        while (cursor.moveToNext()){
                String uid=cursor.getString(cursor.getColumnIndex("cardId"));
                String name1=cursor.getString(cursor.getColumnIndex("name"));
                User user=new User();
                user.setName(name1);
               user.setCardId(uid);

               userList.add(user);
        }
            cursor.close();
        }
        return userList;
    }

    /**
     * 修改姓名
     * @param user
     * @return
     */
    public  int updateDataName(User user){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",user.getName());

        return db.update(TABLE_NAME_USER,values,"name like ?",new String[]{LoginUser});
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    public  int updateDataPassword(User user){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("password",user.getPwd());

        return db.update(TABLE_NAME_USER,values,"name like ?",new String[]{LoginUser});
    }

}
