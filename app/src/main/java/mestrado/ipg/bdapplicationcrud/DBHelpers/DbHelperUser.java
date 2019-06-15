package mestrado.ipg.bdapplicationcrud.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelperUser extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    public static final String TABLE_NAME = "users2";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    public static final String USERNAME = "username";
    public static final String API_KEY = "api_key";
    public static final String PASSWORD = "password";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + ID
            + " integer primary key autoincrement, " + USERNAME
            + " text not null, " + API_KEY + " text not null, "
            + PASSWORD+" text not null" + ") ";


    public DbHelperUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelperUser.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
