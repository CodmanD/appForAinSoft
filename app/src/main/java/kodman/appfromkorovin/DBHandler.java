package kodman.appfromkorovin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by User on 12/10/2017.
 */

public class DBHandler {
    private Context mContext;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBHandler(Context context) {
        mContext = context;
    }


    public long insertProductToDB(int id, String name, float price) {
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("_id", id);
        cv.put("name", name);
        cv.put("price", price);
        long count=db.insert("Products", null, cv);
        dbHelper.close();
        Log.d("INSERT","writeToDB "+cv);
        return count;
    }

    public long updatePrice(String id,String price)
    {
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        ContentValues row= new ContentValues();
        row.put("price", price);
        long count=db.update(DBHelper.tblNameProducts,row,"_id="+id,null);
        db.close();
        dbHelper.close();
        return count;
    }

    public Cursor readAllProductsFromDB() {
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Products ", null);
    }

    public void clearTable() {
        dbHelper = new DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Products");
        db.close();
        dbHelper.close();
    }

    public void closeDB() {
        db.close();
        dbHelper.close();
    }
}
