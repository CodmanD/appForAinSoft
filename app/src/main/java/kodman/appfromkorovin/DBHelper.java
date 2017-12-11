package kodman.appfromkorovin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 12/10/2017.
 */

public class DBHelper extends SQLiteOpenHelper
{
    final static String TAG="===MySQLite==";

    private final static String dbName="DB_Products";
    private final static int dbVersion=1;

    public final static String colProductName="name";
    public final static String colProductPrice="price";
    public final static String colProductId="_id";
    public final static String tblNameProducts="Products";

    public DBHelper(Context context)
    {
        super(context,DBHelper.dbName,null,DBHelper.dbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG, "onCreate:" + db.getPath());
        /**
         * Создание таблиц
         */
        String query="CREATE TABLE Products("+
                "_id integer not null primary key autoincrement,"+
                "name text,"+
                "price real)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        Log.d(TAG, "onUpgrade:" + db.getPath() + ";  oldVersion :" + oldVersion + "; newVersion:" + newVersion);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        Log.d(TAG,"onDowngrade:"+db.getPath()+";  oldVersion :"+oldVersion+"; newVersion:"+newVersion);
    }
    @Override
    public void onOpen(SQLiteDatabase db)
    {
        Log.d(TAG,"onOpen:"+db.getPath());
    }
}
