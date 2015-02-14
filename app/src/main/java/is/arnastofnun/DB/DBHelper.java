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
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WORD + " TEXT NOT NULL, " + DESC + " TEXT);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     *
     * @param db the db
     *
     *           The onCreate() method will be called on first time use of the application.
     *           Here we will construct SQLite database. This method is called only if the
     *           database file is not created before.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }


    /**
     * @param db the db
     * @param oldVersion the old version number
     * @param newVersion the new version number
     *
     *          is only called when the database version is changed. Database version
     *          is an integer value which is specified inside the DBhelper constructor.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
