package is.arnastofnun.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

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
        //Generate Content
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.WORD, result.getTitle());
        contentValue.put(DBHelper.DESC, result.getType());

        //Insert
        dB.insert(DBHelper.TABLE_NAME, null, contentValue);
    }

    public void delete() {

    }

    public WordResult fetch(String word) {
        String[] columns = new String[] { DBHelper.ID, DBHelper.WORD, DBHelper.DESC };
        Cursor cursor = dB.query(DBHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            columns[i] = cursor.getString(i);
        }

        WordResult result = generateWordResult(columns);
        return result;
    }

    private WordResult generateWordResult(String[] columns) {
        WordResult newResult = new WordResult(columns[2], columns[1], null, new ArrayList<ArrayList<String>>());
        return newResult;
    }
}
