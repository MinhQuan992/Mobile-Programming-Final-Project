package hcmute.edu.vn.mssv18110344.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110344.bean.Address;
import hcmute.edu.vn.mssv18110344.bean.Product;
import hcmute.edu.vn.mssv18110344.bean.AdministrativeUnit;
import hcmute.edu.vn.mssv18110344.bean.User;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static String TAG = "DatabaseHandler";
    private static SQLiteDatabase mDataBase;
    private final Context mContext;
    private static final String DATABASE_NAME = "MinistopManager.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "";

    //Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_PROVINCES = "provinces";
    private static final String TABLE_DISTRICTS = "districts";
    private static final String TABLE_WARDS = "wards";
    private static final String TABLE_ADDRESSES = "addresses";

    //Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PICTURE = "picture";

    //USERS table - common column names
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_SEX = "sex";
    private static final String KEY_DATE_OF_BIRTH = "date_of_birth";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_AVATAR = "avatar";

    //PRODUCTS table - common column names
    private static final String KEY_PRICE = "price";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";

    //DISTRICTS and WARDS table - common column names
    private static final String KEY_PROVINCE_ID = "province_id";
    private static final String KEY_DISTRICT_ID = "district_id";

    //ADDRESSES table - common column names
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_WARD = "ward";
    private static final String KEY_STREET = "street";
    private static final String KEY_USER_ID = "user_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    private boolean checkDataBase() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public void createDataBase() {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DATABASE_PATH + DATABASE_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

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
        User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),  cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        return user;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_EMAIL + " = ?", new String[] {String.valueOf(email)}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),  cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
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
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),  cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
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

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String script = "SELECT * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
            productList.add(product);
            cursor.moveToNext();
        }

        return productList;
    }

    public List<Product> getProducts(int category) {
        List<Product> productList = new ArrayList<>();
        String script = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_CATEGORY + " = " + category;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
            productList.add(product);
            cursor.moveToNext();
        }

        return productList;
    }

    public List<AdministrativeUnit> getProvinces() {
        List<AdministrativeUnit> provinceList = new ArrayList<>();
        String script = "SELECT * FROM " + TABLE_PROVINCES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            AdministrativeUnit province = new AdministrativeUnit(cursor.getString(0), cursor.getString(1), cursor.getString(2), null);
            provinceList.add(province);
            cursor.moveToNext();
        }

        return  provinceList;
    }

    public String getProvinceName(String id) {
        String script = "SELECT * FROM " + TABLE_PROVINCES + " WHERE " + KEY_ID + " = '" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public List<AdministrativeUnit> getDistricts(String provinceId) {
        List<AdministrativeUnit> districtList = new ArrayList<>();
        String script = "SELECT * FROM " + TABLE_DISTRICTS + " WHERE " + KEY_PROVINCE_ID + " = '" + provinceId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            AdministrativeUnit district = new AdministrativeUnit(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            districtList.add(district);
            cursor.moveToNext();
        }

        return  districtList;
    }

    public String getDistrictName(String id) {
        String script = "SELECT * FROM " + TABLE_DISTRICTS + " WHERE " + KEY_ID + " = '" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public List<AdministrativeUnit> getWards(String districtId) {
        List<AdministrativeUnit> wardList = new ArrayList<>();
        String script = "SELECT * FROM " + TABLE_WARDS + " WHERE " + KEY_DISTRICT_ID + " = '" + districtId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            AdministrativeUnit ward = new AdministrativeUnit(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            wardList.add(ward);
            cursor.moveToNext();
        }

        return  wardList;
    }

    public String getWardName(String id) {
        String script = "SELECT * FROM " + TABLE_WARDS + " WHERE " + KEY_ID + " = '" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public List<Address> getAddresses(int userId) {
        List<Address> addressList = new ArrayList<>();
        String script = "SELECT * FROM " + TABLE_ADDRESSES + " WHERE " + KEY_USER_ID + " = " + String.valueOf(userId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Address address = new Address(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7));
                addressList.add(address);
                cursor.moveToNext();
            }
            return addressList;
        }

        return null;
    }

    public void addAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, address.getId());
        values.put(KEY_NAME, address.getName());
        values.put(KEY_PHONE, address.getPhone());
        values.put(KEY_PROVINCE, address.getProvinceId());
        values.put(KEY_DISTRICT, address.getDistrictId());
        values.put(KEY_WARD, address.getWardId());
        values.put(KEY_STREET, address.getStreet());
        values.put(KEY_USER_ID, address.getUserId());

        db.insert(TABLE_ADDRESSES, null, values);
        db.close();
    }

    public void updateAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, address.getName());
        values.put(KEY_PHONE, address.getPhone());
        values.put(KEY_PROVINCE, address.getProvinceId());
        values.put(KEY_DISTRICT, address.getDistrictId());
        values.put(KEY_WARD, address.getWardId());
        values.put(KEY_STREET, address.getStreet());

        db.update(TABLE_ADDRESSES, values, KEY_ID + " = ?", new String[] {String.valueOf(address.getId())});
        db.close();
    }

    public int generateAddressId() {
        String script = "SELECT * FROM " + TABLE_ADDRESSES + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_ID)) + 1;
        return 1;
    }

    public void deleteAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADDRESSES, KEY_ID + " = ?", new String[] {String.valueOf(id)});
        db.close();
    }
}
