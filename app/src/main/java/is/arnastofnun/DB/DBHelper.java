package is.arnastofnun.DB;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Jón Friðrik
 * @since 14.02.15
 * @version 1.0
 *
 *
 * A helper class for the DBController. The DBHelper creates the DB the
 * first time it is constructed. It also reconstructs the DB if it has
 * been updated (In future versions the update function would be used to
 * migrate data from old tables to new one and then delete the old tables).
 * It also contains useful  static variables which are constantly being
 * used in the DB, such as the CREATE queries the table and column names
 * and the max size of the DB.
 *
 */
public class DBHelper extends SQLiteOpenHelper{

    //Context
    private Context context;

    // Database Information
    private static final String DB_NAME = "BEYGDU.DB";

    // database version
    private static final int DB_VERSION = 1;

    // database max size
    static final int MAX_SIZE = 10;

    // Table Names
    public static final String TABLE_WORDRESULT = "wordresult";
    public static final String TABLE_BLOCK = "block";
    public static final String TABLE_SUBBLOCK = "subblock";
    public static final String TABLE_TABLES = "tables";
    public static final String TABLE_STATISTICS = "statistics";
    public static final String TABLE_OBEYGJANLEG = "obeygjanleg";
    public static final String TABLE_COMPARE = "compare";
    public static final String TABLE_COMPARETABLES = "compareTables";

    // Table columns
    public static final String WORDID = "wordid";
    public static final String BLOCKID = "blockid";
    public static final String SUBBLOCKID = "subblockid";
    public static final String TABLEID = "tableid";

    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String NOTE = "note";
    public static final String COLHEADERS = "colheaders";
    public static final String ROWHEADERS = "rowheaders";
    public static final String CONTENT = "content";

    public static final String STAT_NO = "Nafnorð";
    public static final String STAT_LO = "Lýsingarorð";
    public static final String STAT_SO = "Sagnorð";
    public static final String STAT_TO = "Töluorð";
    public static final String STAT_FN = "Fornöfn";
    public static final String STAT_OTHER = "Annað"; //tmp before implementing table which can add new columns to db


    // Creating table queries
    private static final String CREATE_WORDRESULT_TABLE =
            "CREATE TABLE " + TABLE_WORDRESULT + " (" +
                    WORDID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TYPE + " TEXT NOT NULL, " +
                    TITLE + " TEXT NOT NULL, " +
                    NOTE + " TEXT, " +
                    DATE + " DATE" +
                    ");";
    private static final String CREATE_BLOCK_TABLE =
            "CREATE TABLE " + TABLE_BLOCK + " (" +
                    WORDID + " INT , " +
                    BLOCKID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT, " +
                    "FOREIGN KEY(" + DBHelper.WORDID + ") " +
                    "REFERENCES " + DBHelper.TABLE_WORDRESULT + "("+ DBHelper.WORDID + ") " +
                    "ON DELETE CASCADE " +
                    ");";
    private static final String CREATE_SUBBLOCK_TABLE =
            "CREATE TABLE " + TABLE_SUBBLOCK + " (" +
                    BLOCKID + " INT, " +
                    SUBBLOCKID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT, " +
                    "FOREIGN KEY(" + DBHelper.BLOCKID + ")" +
                    "REFERENCES " + DBHelper.TABLE_BLOCK + "("+ DBHelper.BLOCKID + ") " +
                    "ON DELETE CASCADE " +
                    ");";
    private static final String CREATE_TABLES_TABLE =
            "CREATE TABLE " + TABLE_TABLES + " (" +
                    SUBBLOCKID + " INT , " +
                    TABLEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT , " +
                    COLHEADERS + " TEXT , " +
                    ROWHEADERS + " TEXT , " +
                    CONTENT + " TEXT, " +
                    "FOREIGN KEY(" + DBHelper.SUBBLOCKID + ")" +
                    "REFERENCES " + DBHelper.TABLE_SUBBLOCK + "("+ DBHelper.SUBBLOCKID + ") " +
                    "ON DELETE CASCADE " +
                    ");";
    private static final String CREATE_STATISTICS_TABLE =
            "CREATE TABLE " + TABLE_STATISTICS + " (" +
                    STAT_NO + " INTEGER, " +
                    STAT_SO + " INTEGER, " +
                    STAT_LO + " INTEGER, " +
                    STAT_TO + " INTEGER, " +
                    STAT_FN + " INTEGER, " +
                    STAT_OTHER + " INTEGER " +
                    ");";

    private static final String CREATE_OBEYGJANLEG_TABLE =
            "CREATE TABLE " + TABLE_OBEYGJANLEG + " (" +
                    WORDID + " TEXT" +
                    ");";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
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
        db.execSQL(CREATE_STATISTICS_TABLE);
        db.execSQL(CREATE_OBEYGJANLEG_TABLE);
        insertIntoObeygjanlegTable(db);
    }

    private void insertIntoObeygjanlegTable(SQLiteDatabase db) {
        db.beginTransaction();
        try{
            AssetManager am = context.getAssets();
            InputStream is = am.open("obeygjanleg.txt");
            String line = null;
            String sql = "INSERT INTO " + TABLE_OBEYGJANLEG + " ( " + WORDID + " ) VALUES (?)";

            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            SQLiteStatement stmt;
            while((line = in.readLine()) != null) {
                stmt = db.compileStatement(sql);
                stmt.bindString(1, line);
                stmt.execute();
                stmt.clearBindings();
            }

            db.setTransactionSuccessful();
        }catch(Exception e){
            //end the transaction on error too when doing exception handling
            db.endTransaction();
            Log.v("", e.toString());
        }
        db.endTransaction();
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBEYGJANLEG);
        onCreate(db);
    }
}
