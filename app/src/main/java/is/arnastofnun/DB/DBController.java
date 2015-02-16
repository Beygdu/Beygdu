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

        //Generate Content for all tables
        ContentValues wordResultContent = new ContentValues();
        wordResultContent.put(DBHelper.TYPE, result.getType());
        wordResultContent.put(DBHelper.TITLE, result.getTitle());
        wordResultContent.put(DBHelper.NOTE, result.getNote());
        dB.insert(DBHelper.TABLE_WORDRESULT, null, wordResultContent);

        int id = fetchMaxId();

        for (Block b : result.getBlocks()) {
            ContentValues blockContent = new ContentValues();
            blockContent.put(DBHelper.WORDID, id);
            blockContent.put(DBHelper.TITLE, b.getTitle());
            dB.insert(DBHelper.TABLE_BLOCK, null, blockContent);

            for (SubBlock sb : b.getBlocks()) {
                ContentValues subBlockContent = new ContentValues();
                subBlockContent.put(DBHelper.WORDID, id);
                subBlockContent.put(DBHelper.TITLE, sb.getTitle());
                dB.insert(DBHelper.TABLE_SUBBLOCK, null, subBlockContent);

                for (Tables table : sb.getTables()) {
                    ContentValues tableContent = new ContentValues();
                    tableContent.put(DBHelper.WORDID, id);
                    tableContent.put(DBHelper.TITLE, table.getTitle());
                    tableContent.put(DBHelper.COLHEADERS, arrToString(table.getColumnNames()));
                    tableContent.put(DBHelper.ROWHEADERS, arrToString(table.getRowNames()));
                    tableContent.put(DBHelper.CONTENT, arrToString(table.getContent().toArray()));
                    dB.insert(DBHelper.TABLE_TABLES, null, tableContent);
                }
            }
        }
    }

    public WordResult fetch(String word) {

        int wordid = -1;
        Object[] result = new Object[wordResultColumns.length];

        Cursor cursor = dB.query(DBHelper.TABLE_WORDRESULT, wordResultColumns, DBHelper.TITLE + " = " + word, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        result[0] = cursor.getInt(0);
        result[1] = cursor.getString(1);
        result[2] = cursor.getString(2);
        result[3] = cursor.getString(3);

        wordid = (Integer) result[0];

        String[] blockColumns = new String[] {DBHelper.WORDID, DBHelper.TYPE, DBHelper.TITLE, DBHelper.NOTE };
        cursor = dB.query(DBHelper.TABLE_BLOCK, wordResultColumns, "wordid = " + wordid, null, null, null, null);
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            wordResultColumns[i] = cursor.getString(i);
        }
        String[] subBlockColumns = new String[] {DBHelper.WORDID, DBHelper.TYPE, DBHelper.TITLE, DBHelper.NOTE };
        String[] tableColumns = new String[] {DBHelper.WORDID, DBHelper.TYPE, DBHelper.TITLE, DBHelper.NOTE };

        WordResult tmp = generateWordResult(wordResultColumns);
        return tmp;
    }

    private int fetchMaxId() {

        int id = 0;
        final String MY_QUERY = "SELECT MAX("+ DBHelper.WORDID +") FROM " + DBHelper.TABLE_WORDRESULT;
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
/*        Cursor cursor = dB.query(DBHelper.TABLE_WORDRESULT, columns, null,
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        //Will only return one int if word is in the table since the title is a primary key of the table,
        // if not in the table return -1;
        int tmp = cursor.getInt(cursor.getColumnIndex(DBHelper.WORDID));
        return tmp;
    }
    */

    private WordResult generateWordResult(String[] columns) {
        WordResult newResult = new WordResult(columns[2], columns[1], null, new ArrayList<ArrayList<String>>());
        return newResult;
    }

    private String arrToString(Object[] arr){
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            result = result + "#" + arr[i];
        }
        return result;
    }

    private String[] stringToArr(String s) {
        return s.split("#+");
    }
}
