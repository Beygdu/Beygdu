package is.arnastofnun.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Jón Friðrik
 * @since 14.02.15
 * @version 0.1
 *
 */
public class DBHelper extends SQLiteOpenHelper{

    // Database Information
    static final String DB_NAME = "BEYGDU.DB";

    // database version
    static final int DB_VERSION = 1;

    // Table Names
    public static final String TABLE_WORDRESULT = "wordresult";
    public static final String TABLE_BLOCK = "block";
    public static final String TABLE_SUBBLOCK = "subblock";
    public static final String TABLE_TABLES = "tables";


    // Table columns
    public static final String WORDID = "wordid";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String NOTE = "note";
    public static final String COLHEADERS = "colheaders";
    public static final String ROWHEADERS = "rowheaders";
    public static final String CONTENT = "content";

    // Creating table queries
    private static final String CREATE_WORDRESULT_TABLE =
            "CREATE TABLE " + TABLE_WORDRESULT + " (" +
                    WORDID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TYPE + " TEXT NOT NULL, " +
                    TITLE + " TEXT NOT NULL, " +
                    NOTE + " TEXT" +
                    ");";
    private static final String CREATE_BLOCK_TABLE =
            "CREATE TABLE " + TABLE_BLOCK + " (" +
                    WORDID + " INT , " +
                    TITLE + " TEXT " +
                    ");";
    private static final String CREATE_SUBBLOCK_TABLE =
            "CREATE TABLE " + TABLE_SUBBLOCK + " (" +
                    WORDID + " INT, " +
                    TITLE + " TEXT" +
                    ");";
    private static final String CREATE_TABLES_TABLE =
            "CREATE TABLE " + TABLE_TABLES + " (" +
                    WORDID + " INT , " +
                    TITLE + " TEXT , " +
                    COLHEADERS + " TEXT , " +
                    ROWHEADERS + " TEXT , " +
                    CONTENT + " TEXT" +
                    ");";


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
        db.execSQL(CREATE_WORDRESULT_TABLE);
        db.execSQL(CREATE_BLOCK_TABLE);
        db.execSQL(CREATE_SUBBLOCK_TABLE);
        db.execSQL(CREATE_TABLES_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDRESULT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBBLOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
        onCreate(db);
    }
}
