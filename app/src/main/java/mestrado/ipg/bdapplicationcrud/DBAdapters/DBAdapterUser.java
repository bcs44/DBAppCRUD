package mestrado.ipg.bdapplicationcrud.DBAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import mestrado.ipg.bdapplicationcrud.DBHelpers.DbHelperUser;
import mestrado.ipg.bdapplicationcrud.User;


public class DBAdapterUser {
    private SQLiteDatabase database;
    private DbHelperUser dbHelperUser;
    private String[] allColumnsUser = { DbHelperUser.ID, DbHelperUser.USERNAME, DbHelperUser.API_KEY, DbHelperUser.PASSWORD};

    public DBAdapterUser(Context context) {
        dbHelperUser = new DbHelperUser(context);
    }

    public void open() throws SQLException {
        database = dbHelperUser.getWritableDatabase();
    }

    public void close() {
        dbHelperUser.close();
    }


    private User cursorToUser(Cursor cursor) {

        User user = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3));
        return user;
    }

    public User createUser(String username, String api_key, String password) {
        ContentValues values = new ContentValues();
        values.put(dbHelperUser.USERNAME, username);
        values.put(dbHelperUser.API_KEY,api_key);
        values.put(dbHelperUser.PASSWORD,password);


        long insertId = database.insert(dbHelperUser.TABLE_NAME, null, values);
        // To show how to query
        Cursor cursor = database.query(dbHelperUser.TABLE_NAME, allColumnsUser, dbHelperUser.ID + " = " + insertId, null,null, null, null);
        cursor.moveToFirst();
        return cursorToUser(cursor);
    }


}