package hcmute.edu.vn.mssv18110344.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110344.bean.User;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "storeManager";
    private static final int DATABASE_VERSION = 1;

    //Table names
    private static final String TABLE_USERS = "users";

    //Common column names
    private static final String KEY_ID = "id";

    //USERS table - common column names
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_SEX = "sex";
    private static final String KEY_DATE_OF_BIRTH = "date_of_birth";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_AVATAR = "avatar";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_FULL_NAME + " TEXT, "
                + KEY_SEX + " TEXT, "
                + KEY_DATE_OF_BIRTH + " TEXT, "
                + KEY_PHONE + " TEXT, "
                + KEY_EMAIL + " TEXT, "
                + KEY_PASSWORD + " TEXT, "
                + KEY_AVATAR + " BLOB)";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String script = "DROP TABLE IF EXISTS " + TABLE_USERS;
        db.execSQL(script);
        onCreate(db);
    }

    //Queries of USERS table
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, user.getId());
        values.put(KEY_FULL_NAME, user.getFullName());
        values.put(KEY_SEX, user.getSex());
        values.put(KEY_DATE_OF_BIRTH, user.getDateOfBirth());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_AVATAR, user.getAvatar());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUserByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_PHONE + " = ?", new String[] {String.valueOf(phone)}, null, null, null);
        cursor.moveToFirst();
        User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),  cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getBlob(7));
        return user;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_EMAIL + " = ?", new String[] {String.valueOf(email)}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),  cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getBlob(7));
            return user;
        }
        return null;
    }

    public boolean phoneExisted(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_PHONE + " = ?", new String[] {String.valueOf(phone)}, null, null, null);
        return cursor.moveToFirst();
    }

    public boolean emailExisted(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_EMAIL + " = ?", new String[] {String.valueOf(email)}, null, null, null);
        return cursor.moveToFirst();
    }

    public int generateId() {
        String script = "SELECT * FROM " + TABLE_USERS + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_ID)) + 1;
        return 1;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String script = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),  cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getBlob(7));
            userList.add(user);
            cursor.moveToNext();
        }
        return userList;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FULL_NAME, user.getFullName());
        values.put(KEY_SEX, user.getSex());
        values.put(KEY_DATE_OF_BIRTH, user.getDateOfBirth());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_AVATAR, user.getAvatar());

        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] {String.valueOf(user.getId())});
        db.close();
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?", new String[] {String.valueOf(userId)});
        db.close();
    }
}
