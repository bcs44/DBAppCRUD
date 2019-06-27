package mestrado.ipg.condomastercrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DBAdapter {
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = { DbHelper.ID, DbHelper.USER_ID, DbHelper.MEETING_DATE, DbHelper.PLACE_ID, DbHelper.DESCRIPTION,  DbHelper.TITLE};


    public DBAdapter(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Assembleia createAssembleia(String USER_ID, String MEETING_DATE, String PLACE_ID, String DESCRIPTION, String TITLE) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.USER_ID, USER_ID);
        values.put(dbHelper.MEETING_DATE,MEETING_DATE);
        values.put(dbHelper.PLACE_ID,PLACE_ID);
        values.put(dbHelper.DESCRIPTION,DESCRIPTION);
        values.put(dbHelper.TITLE,TITLE);


        long insertId = database.insert(dbHelper.TABLE_NAME, null, values);
        // To show how to query
        Cursor cursor = database.query(dbHelper.TABLE_NAME, allColumns, dbHelper.ID + " = " + insertId, null,null, null, null);
        cursor.moveToFirst();
        return cursorToContacto(cursor);
    }

    public void EliminaAssembleia (int idAssembleia){
        //database.delete(DB.TABLE_NAME, "id=?", new String [] {Integer.toString(idContacto)});
        database.delete(DbHelper.TABLE_NAME, DbHelper.ID + " = " + idAssembleia, null);
    }

    private Assembleia cursorToContacto(Cursor cursor) {
        Assembleia assembleia = new Assembleia(cursor.getLong(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return assembleia;
    }

    public Cursor getAssembleias(){
        Cursor cursor = database.rawQuery("select _id, _user_id,_meeting_date,_place_id,_description,_title from assembleia2", null);
        return cursor;
    }
    public Assembleia getAssembleia (int idAssembleia){
        Cursor cursor = database.query(dbHelper.TABLE_NAME, allColumns, dbHelper.ID + " = " + idAssembleia, null,null, null, null);
        cursor.moveToFirst();
        return cursorToContacto(cursor);
    }


    public void EditaAssembleia (String USER_ID, String MEETING_DATE, String PLACE_ID, String DESCRIPTION, String TITLE, int idAssembleia){
        ContentValues values = new ContentValues();
        values.put(dbHelper.USER_ID, USER_ID);
        values.put(dbHelper.MEETING_DATE,MEETING_DATE);
        values.put(dbHelper.PLACE_ID,PLACE_ID);
        values.put(dbHelper.DESCRIPTION,DESCRIPTION);
        values.put(dbHelper.TITLE,TITLE);

        database.update(DbHelper.TABLE_NAME, values, DbHelper.ID + " = " + idAssembleia, null);
    }


}
