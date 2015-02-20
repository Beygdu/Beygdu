package is.arnastofnun.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.SubBlock;
import is.arnastofnun.parser.Tables;
import is.arnastofnun.parser.WordResult;
import is.arnastofnun.utils.TableFragment;

/**
 * @author Jón Friðrik
 * @since 14.02.15
 * @version 0.1
 *
 */
public class DBController {

    private Context context = null;
    private SQLiteDatabase dB = null;
    private DBHelper dbHelper = null;

    String[] wordResultColumns = new String[] {DBHelper.WORDID, DBHelper.TYPE, DBHelper.TITLE, DBHelper.NOTE };
    String[] blockColumns = new String[] {DBHelper.WORDID, DBHelper.BLOCKID, DBHelper.TITLE};
    String[] subBlockColumns = new String[] {DBHelper.BLOCKID, DBHelper.SUBBLOCKID, DBHelper.TITLE};
    String[] tableColumns = new String[] {DBHelper.SUBBLOCKID, DBHelper.TABLEID, DBHelper.TITLE, DBHelper.COLHEADERS, DBHelper.ROWHEADERS, DBHelper.CONTENT };


    public DBController(Context context){
        this.context = context;
    }

    public DBController open() throws SQLException {
        dbHelper = new DBHelper(context);
        dB = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(WordResult result) {
        insertWordResult(result);
    }

    private void insertWordResult(WordResult result) {
        //Generate Content for all tables
        ContentValues wordResultContent = new ContentValues();
        wordResultContent.put(DBHelper.TYPE, result.getType());
        wordResultContent.put(DBHelper.TITLE, result.getTitle());
        wordResultContent.put(DBHelper.NOTE, result.getNote());
        dB.insert(DBHelper.TABLE_WORDRESULT, null, wordResultContent);

        int id = fetchMaxId(DBHelper.WORDID, DBHelper.TABLE_WORDRESULT);
        dbHelper.close();

        for(Block block : result.getBlocks()) {
            insertBlocks(block, id);
        }
    }

    private void insertBlocks(Block block, int wordId) {
        dB = dbHelper.getWritableDatabase();

        ContentValues blockContent = new ContentValues();
        blockContent.put(DBHelper.WORDID, wordId);
        blockContent.put(DBHelper.TITLE, block.getTitle());
        dB.insert(DBHelper.TABLE_BLOCK, null, blockContent);

        int blockid = fetchMaxId(DBHelper.BLOCKID, DBHelper.TABLE_BLOCK);
        dbHelper.close();
        for(SubBlock sb : block.getBlocks()) {
            insertSubBlock(sb, blockid);
        }
    }

    private void insertSubBlock(SubBlock sb, int subBlockId) {
        dB = dbHelper.getWritableDatabase();

        ContentValues subBlockContent = new ContentValues();
        subBlockContent.put(DBHelper.BLOCKID, subBlockId);
        subBlockContent.put(DBHelper.TITLE, sb.getTitle());
        dB.insert(DBHelper.TABLE_SUBBLOCK, null, subBlockContent);

        int blockid = fetchMaxId(DBHelper.SUBBLOCKID, DBHelper.TABLE_SUBBLOCK);
        dbHelper.close();

        for(Tables table: sb.getTables()){
            insertTable(table, subBlockId);
        }
    }

    private void insertTable(Tables table, int id) {
        dB = dbHelper.getWritableDatabase();

        ContentValues tableContent = new ContentValues();
        tableContent.put(DBHelper.SUBBLOCKID, id);
        tableContent.put(DBHelper.TITLE, table.getTitle());
        tableContent.put(DBHelper.COLHEADERS, arrToString(table.getColumnNames()));
        tableContent.put(DBHelper.ROWHEADERS, arrToString(table.getRowNames()));
        tableContent.put(DBHelper.CONTENT, arrToString(table.getContent().toArray()));
        dB.insert(DBHelper.TABLE_TABLES, null, tableContent);

        dbHelper.close();
    }

    /**
     *
     * @param wordTitle the title of the WordResult
     * @return true if db contains word, else false
     */
    private boolean dbContains(String wordTitle){
        boolean contains = false;
        return contains;
    }


    /**
     *
     * @param title the title to be fetched
     * @return the first occurance WordResult for the title in the table
     */
    public WordResult fetch(String title) {
        WordResult newWordResult;
        ArrayList<Block> blocks = new ArrayList<Block>();
        ArrayList<SubBlock> subBlocks = new ArrayList<SubBlock>();
        ArrayList<Tables> tables = new ArrayList<Tables>();

        //fetch id of word;
        int wordid = fetchWordId(title);

        Cursor cursor = dB.query(DBHelper.TABLE_WORDRESULT, wordResultColumns, DBHelper.TITLE + " = " + title, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return null;
    }

    public ArrayList<String> fetchAllWords() {
        ArrayList<String> words= new ArrayList<String>();
        if (!dB.isOpen()) {
            try {
                open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        final String MY_QUERY = "SELECT * FROM " + DBHelper.TABLE_WORDRESULT;
        Cursor cursor = dB.rawQuery(MY_QUERY, null);

        int iTitle = cursor.getColumnIndex(DBHelper.TITLE);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String tmp = cursor.getString(iTitle);
            words.add(tmp);
        }

        close();
        return words;
    }

    /**
     *
     * @param word the title of the WordResult
     * @return the id of the word in the database.
     */
    private int fetchWordId(String word) {
        int id = 0;
        final String MY_QUERY = "SELECT " +DBHelper.WORDID +" FROM " + DBHelper.TABLE_WORDRESULT + " WHERE " + DBHelper.TITLE + " = word";
        Cursor mCursor = dB.rawQuery(MY_QUERY, null);
        try {
            if (mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                id = mCursor.getInt(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    private int fetchMaxId(String column, String table) {
        int id = 0;
        final String MY_QUERY = "SELECT MAX("+ column +") FROM " + table;
        Cursor mCursor = dB.rawQuery(MY_QUERY, null);
        try {
            if (mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                id = mCursor.getInt(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    private String arrToString(Object[] arr){
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            result = result + "&" + arr[i];
        }
        return result;
    }

    private String[] stringToArr(String s) {
        return s.split("&+");
    }
}
