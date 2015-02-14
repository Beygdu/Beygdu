package is.arnastofnun.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Jón Friðrik
 * @since 14.02.15
 * @version 0.1
 *
 */
public class DBHelper extends SQLiteOpenHelper{

    // Table Name
    public static final String TABLE_NAME = "Cache";

    // Table columns
    public static final String ID = "id";
    public static final String WORD = "word";
    public static final String DESC = "description";

    // Database Information
    static final String DB_NAME = "BEYGDU.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TODO_SUBJECT + " TEXT NOT NULL, " + TODO_DESC + " TEXT);";

    public DBHelper(Context context) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
