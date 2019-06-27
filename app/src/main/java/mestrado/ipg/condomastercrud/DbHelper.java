package mestrado.ipg.condomastercrud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "assembleia.db";
    public static final String TABLE_NAME = "assembleia2";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    public static final String USER_ID = "_user_id";
    public static final String MEETING_DATE = "_meeting_date";
    public static final String PLACE_ID = "_place_id";
    public static final String DESCRIPTION = "_description";
    public static final String TITLE = "_title";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "( "
            + ID    + " integer primary key autoincrement, "
            + USER_ID       + " text not null, "
            + MEETING_DATE  + " text not null, "
            + PLACE_ID      + " text not null,"
            + DESCRIPTION      + " text not null,"
            + TITLE      + " text not null"
            + ") ";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}