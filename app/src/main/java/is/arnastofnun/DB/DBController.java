package is.arnastofnun.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import is.arnastofnun.parser.Block;
import is.arnastofnun.parser.SubBlock;
import is.arnastofnun.parser.Tables;
import is.arnastofnun.parser.WordResult;

/**
 * @author Jón Friðrik
 * @since 14.02.15
 * @version 1.0
 *
 * DBController is the controller for the apps database.
 * It controlls the database by implementing the fetch and
 * insert commands for the database.
 *
 */
public class DBController {

    private Context context = null;
    private SQLiteDatabase dB = null;
    private DBHelper dbHelper = null;

    /**
     * @param context the context the DBController is being used in.
     */
    public DBController(Context context){
        this.context = context;
    }

    private DBController open() throws SQLException {
        dbHelper = new DBHelper(context);
        dB = dbHelper.getWritableDatabase();
        return this;
    }

    private void close(Cursor cursor) {
        dbHelper.close();
        if(cursor != null) {
            cursor.close();
        }
    }

    /**
     * Increments the column which represents the wordtype in statistcs table.
     *
     * @param column the column to increment
     */
    public void insertStats(String column){
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String myQuery = "SELECT * FROM " + DBHelper.TABLE_STATISTICS;

        Cursor cursor = dB.rawQuery(myQuery, null);
        cursor.moveToFirst();
        ContentValues statsContent = new ContentValues();
        if(cursor != null && cursor.getCount() == 0) {
            statsContent.put(DBHelper.STAT_NO, 0);
            statsContent.put(DBHelper.STAT_LO, 0);
            statsContent.put(DBHelper.STAT_SO, 0);
            statsContent.put(DBHelper.STAT_TO, 0);
            statsContent.put(DBHelper.STAT_FN, 0);
            statsContent.put(DBHelper.STAT_OTHER, 0);
            dB.insert(DBHelper.TABLE_STATISTICS, null, statsContent);
            statsContent.clear();
        }

        String columnName = column.toLowerCase();
        String foundCol = "Annað"; //initially set to other and changed if column is found
        for(int i = 0 ; i < cursor.getColumnNames().length; i++) {
            String col = cursor.getColumnNames()[i].toLowerCase();
            if(columnName.contains(col)){
                foundCol = cursor.getColumnNames()[i];
                break;
            }
        }

        int currentValue = fetchStatsForColumn(foundCol);

        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        statsContent.put(foundCol, currentValue + 1);
        dB.update(DBHelper.TABLE_STATISTICS, statsContent, null, null);
        close(cursor);
    }

    /**
     * Inserts the wordresult into the db.
     *
     * @param wordResult the wordresult
     */
    public void insert(WordResult wordResult) {
        if(!dbContains(wordResult.getTitle())) {
            int rows = getTableSize(DBHelper.TABLE_WORDRESULT);
            if(rows >=  DBHelper.MAX_SIZE)  {
                removeOldest();
            }
            insertWordResult(wordResult);
        }
    }

    private void insertWordResult(WordResult result) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ContentValues wordResultContent = new ContentValues();

        wordResultContent.put(DBHelper.TYPE, result.getSearchWord());
        wordResultContent.put(DBHelper.TYPE, result.getDescription());
        wordResultContent.put(DBHelper.TITLE, result.getTitle());
        wordResultContent.put(DBHelper.NOTE, result.getWarning());
        wordResultContent.put(DBHelper.DATE, new Date().getTime());
        dB.insert(DBHelper.TABLE_WORDRESULT, null, wordResultContent);

        int wordResultID = fetchMaxId(DBHelper.WORDID, DBHelper.TABLE_WORDRESULT);
        dbHelper.close();

        for(Block block : result.getBlocks()) {
            insertBlocks(block, wordResultID);
        }
        close(null);
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
     * @param title the title to be fetched
     * @return the first occurance for the title in the table
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
        newWordResult = new WordResult();
        newWordResult.setDescription(cursor.getString(1));
        newWordResult.setTitle(cursor.getString(2));
        newWordResult.setWarning(cursor.getString(3));
        //newWordResult = new WordResult(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        newWordResult.setBlocks(fetchBlocks(cursor));


        updateDate(title, cursor);
        close(cursor);
        return newWordResult;
    }

    /**
     *
     * @return all the titles of the words in the database.
     */
    public ArrayList<String> fetchAllWords() {
        ArrayList<String> words= new ArrayList<>();
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String myQuery = "SELECT * FROM " +
                DBHelper.TABLE_WORDRESULT +
                " ORDER BY " + DBHelper.DATE + " DESC";

        Cursor cursor = dB.rawQuery(myQuery, null);

        int iTitle = cursor.getColumnIndex(DBHelper.TITLE);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String tmp = cursor.getString(iTitle);
            words.add(tmp);
        }

        close(cursor);
        return words;
    }

    /**
     * Get a cursor pointing at the statistics table
     *
     * @return the cursor pointing at the only row in Statistics table.
     */
    private int fetchStatsForColumn(String column){
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String myQuery =
                "SELECT " + column + " FROM " + DBHelper.TABLE_STATISTICS;

        Cursor cursor = dB.rawQuery(myQuery, null);
        cursor.moveToFirst();

        int currentValue = cursor.getInt(0);
        close(cursor);
        return currentValue;
    }

    /**
     * Returns a HashMap of:
     * key = the columnName (e.g. "Nafnorð")
     * value = Integer representing the number of searches for that columnName
     * @return a hashmap of all the stats in the database.
     */
    public HashMap<String, Integer> fetchAllStats(){
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String myQuery =
                "SELECT * FROM " + DBHelper.TABLE_STATISTICS;

        Cursor cursor = dB.rawQuery(myQuery, null);
        cursor.moveToFirst();

        HashMap<String, Integer> stats = new HashMap<String, Integer>();

        if(cursor.getCount() != 0) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                stats.put(cursor.getColumnName(i), cursor.getInt(i));
            }
        }

        close(cursor);
        return stats;
    }

    /**
     *
     * @param title of the word
     * @return title of the word is in the table, else null
     */
    public String fetchObeygjanlegt(String title) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = null;

        final String myQuery =
                "SELECT " + DBHelper.WORDID + " FROM " + DBHelper.TABLE_OBEYGJANLEG + " WHERE " +  DBHelper.WORDID + " = '" + title + "'";

        Cursor cursor = dB.rawQuery(myQuery, null);
        cursor.moveToFirst();

        if(cursor.getCount() != 0) {
            result = cursor.getString(0);
        }
        close(cursor);
        return result;
    }

    private ArrayList<Block> fetchBlocks(Cursor cursor) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        int blockID = -1;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (blockID != cursor.getInt(6)) {
                blockID = cursor.getInt(6);
                blocks.add(new Block(cursor.getString(7), fetchSubBlocks(cursor, cursor.getInt(6))));
            }
        }
        return blocks;
    }

    private ArrayList<SubBlock> fetchSubBlocks(Cursor cursor, int blockID) {
        ArrayList<SubBlock> subBlocks = new ArrayList<SubBlock>();
        int subBlockID = -1;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if(cursor.getInt(9) != subBlockID && cursor.getInt(8) == blockID){
                subBlockID = cursor.getInt(9);
                subBlocks.add(new SubBlock(cursor.getString(10), fetchTables(cursor, cursor.getInt(9))));
            }
        }
        return subBlocks;
    }

    private ArrayList<Tables> fetchTables(Cursor cursor, int subBlockID) {
        ArrayList<Tables> tables = new ArrayList<Tables>();
        int tableID = -1;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if(cursor.getInt(12) != tableID && cursor.getInt(11) == subBlockID) {
                tableID = cursor.getInt(12);
                tables.add(new Tables(cursor.getString(13), stringToArr(cursor.getString(14)),
                        stringToArr(cursor.getString(15)), new ArrayList<String>(Arrays.asList(stringToArr(cursor.getString(16))))));
            }
        }
        return tables;
    }

    /**
     *
     * @param column the column to sort
     * @param table the table to sort from
     * @return the highest integer value in the column
     */
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

    /**
     *
     * @param column the column to sort
     * @param table the table to sort from
     * @return the lowest integer value in the column.
     */
    private int fetchMinId(String column, String table) {
        int id = 0;
        final String MY_QUERY = "SELECT MIN("+ column +") FROM " + table;
        Cursor mCursor = dB.rawQuery(MY_QUERY, null);
        try {
            if (mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                id = mCursor.getInt(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        mCursor.close();
        return id;
    }

    /**
     *
     * @param wordTitle the title of the WordResult
     * @return true if db contains word, else false
     */
    private boolean dbContains(String wordTitle){
        boolean contains = false;

        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String myQuery =
                "SELECT * FROM wordresult " +
                        "WHERE " + DBHelper.TITLE + " = '"+ wordTitle +"'";

        Cursor cursor = dB.rawQuery(myQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();

            if(cursor.getCount() > 0){
                contains = true;
                updateDate(wordTitle, cursor);

                close(cursor);
                return contains;
            }
        }

        close(cursor);
        return contains;
    }

    /**
     * updates the date of the word.
     *
     * @param wordTitle the title of the word
     * @param cursor the cursor containing the row to alter
     */
    private void updateDate(String wordTitle, Cursor cursor) {
        String type = cursor.getString(1);
        String title = cursor.getString(2);
        String note = cursor.getString(3);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TYPE, type);
        contentValues.put(DBHelper.TITLE, title);
        contentValues.put(DBHelper.NOTE, note);
        contentValues.put(DBHelper.DATE, new Date().getTime());

        dB.update(DBHelper.TABLE_WORDRESULT, contentValues, DBHelper.TITLE + "='" + wordTitle+"'", null);
    }

    /**
     *
     * @param table the name of the table
     * @return the size of the table
     */
    private int getTableSize(String table) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String myQuery =
                "SELECT COUNT(*) FROM " + table;

        Cursor cursor = dB.rawQuery(myQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        int value = cursor.getInt(0);
        close(cursor);
        return value;
    }

    /**
     * Removes the oldest word in the table
     */
    private void removeOldest(){
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int wordID = fetchMinId(DBHelper.WORDID, DBHelper.TABLE_WORDRESULT);
        dB.delete(DBHelper.TABLE_WORDRESULT, DBHelper.WORDID + "=" + wordID, null);
        close(null);
    }

    /**
     *
     * @param arr the array of words
     * @return a string in the form word1&word2$word3
     */
    private String arrToString(Object[] arr){
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            result = result + "&" + arr[i];
        }
        return result;
    }

    /**
     *
     * @param s a string in the form word1&word2$word3
     * @return an array containing the words
     */
    private String[] stringToArr(String s) {
        if (s.startsWith("&")) {
            s = s.substring(1, s.length());
        }
        String[] arr = s.split("&+");
        return arr;
    }
}
