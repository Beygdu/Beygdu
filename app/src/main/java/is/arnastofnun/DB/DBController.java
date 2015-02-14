package is.arnastofnun.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * @author Jón Friðrik
 * @since 14.02.15
 * @version 0.1
 *
 */
public class DBController {

    Context myContext = null;
    SQLiteDatabase dB = null;

    public DBController(Context context){
        this.myContext = context;
    }

    public void open() throws SQLException{

    }

    public void close() {

    }

    public void insert() {

    }

    public void delete() {

    }
}
