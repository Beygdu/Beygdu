package is.arnastofnun.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Jón Friðrik
 * @since 14.02.15
 * @version 1.0
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
    // id's
    public static final String WORDID = "wordid";
    public static final String BLOCKID = "blockid";
    public static final String SUBBLOCKID = "subblockid";
    public static final String TABLEID = "tableid";

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
                    BLOCKID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT " +
                    ");";
    private static final String CREATE_SUBBLOCK_TABLE =
            "CREATE TABLE " + TABLE_SUBBLOCK + " (" +
                    BLOCKID + " INT, " +
                    SUBBLOCKID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT" +
                    ");";
    private static final String CREATE_TABLES_TABLE =
            "CREATE TABLE " + TABLE_TABLES + " (" +
                    SUBBLOCKID + " INT , " +
                    TABLEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

    /**
     *
     * @param db the database
     *           The db will be cleared.
     */
    public void clearTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDRESULT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBBLOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
        db.execSQL(CREATE_WORDRESULT_TABLE);
        db.execSQL(CREATE_BLOCK_TABLE);
        db.execSQL(CREATE_SUBBLOCK_TABLE);
        db.execSQL(CREATE_TABLES_TABLE);
    }
}
