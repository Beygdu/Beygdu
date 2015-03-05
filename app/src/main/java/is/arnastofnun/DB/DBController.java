package is.arnastofnun.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.SubBlock;
import is.arnastofnun.parser.Tables;
import is.arnastofnun.parser.WordResult;

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

        int wordResultID = fetchMaxId(DBHelper.WORDID, DBHelper.TABLE_WORDRESULT);
        dbHelper.close();

        for(Block block : result.getBlocks()) {
            insertBlocks(block, wordResultID);
        }
    }

    private void insertBlocks(Block block, int wordResultId) {
        dB = dbHelper.getWritableDatabase();

        ContentValues blockContent = new ContentValues();
        blockContent.put(DBHelper.WORDID, wordResultId);
        blockContent.put(DBHelper.TITLE, block.getTitle());
        dB.insert(DBHelper.TABLE_BLOCK, null, blockContent);

        int blockid = fetchMaxId(DBHelper.BLOCKID, DBHelper.TABLE_BLOCK);
        dbHelper.close();
        for(SubBlock sb : block.getBlocks()) {
            insertSubBlock(sb, blockid);
        }
    }

    private void insertSubBlock(SubBlock sb, int blockID) {
        dB = dbHelper.getWritableDatabase();

        ContentValues subBlockContent = new ContentValues();
        subBlockContent.put(DBHelper.BLOCKID, blockID);
        subBlockContent.put(DBHelper.TITLE, sb.getTitle());
        dB.insert(DBHelper.TABLE_SUBBLOCK, null, subBlockContent);

        int subBlockID = fetchMaxId(DBHelper.SUBBLOCKID, DBHelper.TABLE_SUBBLOCK);
        dbHelper.close();

        for(Tables table: sb.getTables()){
            insertTable(table, subBlockID);
        }
    }

    private void insertTable(Tables table, int subBlockID) {
        dB = dbHelper.getWritableDatabase();

        ContentValues tableContent = new ContentValues();
        tableContent.put(DBHelper.SUBBLOCKID, subBlockID);
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

        if (!dB.isOpen()) {
            try {
                open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        final String myQuery =
                "SELECT * FROM wordresult " +
                "JOIN block ON wordresult.wordid = block.wordid " +
                "JOIN subblock ON block.blockid = subblock.blockid " +
                "JOIN tables ON subblock.subblockid = tables.subblockid " +
                "WHERE wordresult.title = '"+ title +"'";

        Cursor cursor = dB.rawQuery(myQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        newWordResult = new WordResult(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        newWordResult.setBlocks(fetchBlocks(cursor));

        close();
        return newWordResult;
    }

    private ArrayList<Block> fetchBlocks(Cursor cursor) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        int blockID = -1;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (blockID != cursor.getInt(5)) {
                blockID = cursor.getInt(5);
                blocks.add(new Block(cursor.getString(6), fetchSubBlocks(cursor, cursor.getInt(5))));
            }
        }
        return blocks;
    }

    private ArrayList<SubBlock> fetchSubBlocks(Cursor cursor, int blockID) {
        ArrayList<SubBlock> subBlocks = new ArrayList<SubBlock>();
        int subBlockID = -1;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if(cursor.getInt(8) != subBlockID && cursor.getInt(7) == blockID){
                subBlockID = cursor.getInt(8);
                subBlocks.add(new SubBlock(cursor.getString(9), fetchTables(cursor, cursor.getInt(8))));
            }
        }
        return subBlocks;
    }
    
    private ArrayList<Tables> fetchTables(Cursor cursor, int subBlockID) {
        ArrayList<Tables> tables = new ArrayList<Tables>();
        int tableID = -1;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if(cursor.getInt(11) != tableID && cursor.getInt(10) == subBlockID) {
                tableID = cursor.getInt(11);
                tables.add(new Tables(cursor.getString(12), stringToArr(cursor.getString(13)),
                        stringToArr(cursor.getString(14)), new ArrayList<String>(Arrays.asList(stringToArr(cursor.getString(15))))));
            }
        }
        return tables;
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
        final String myQuery = "SELECT * FROM " + DBHelper.TABLE_WORDRESULT;
        Cursor cursor = dB.rawQuery(myQuery, null);

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
        if (s.startsWith("&")) {
            s = s.substring(1, s.length());
        }
        String[] arr = s.split("&+");
        return arr;
    }
}
