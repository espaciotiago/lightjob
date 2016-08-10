package utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by smartin on 12/04/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //----------------------------------------------------------------------------------------------------------------
    //Informacion de la bd
    //----------------------------------------------------------------------------------------------------------------
    private static final String LOG="DatabaseHelper";
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="Lightjob";
    //----------------------------------------------------------------------------------------------------------------
    //Nombres de las tablas
    //----------------------------------------------------------------------------------------------------------------
    public static final String TABLE_USER="User";

    //----------------------------------------------------------------------------------------------------------------
    //Sentencias de creacion de tablas
    //----------------------------------------------------------------------------------------------------------------

    public static final String CREATE_TABLE_USER="CREATE TABLE "
            + TABLE_USER + "(userMail TEXT, password TEXT,active BOOLEAN)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(LOG, " Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CREATE_TABLE_USER);
    }

    //----------------------------------------------------------------------------------------------------------------
    //Metodos de manipulacion de la bd
    //----------------------------------------------------------------------------------------------------------------

    public String userExist(){
        String user=null;
        SQLiteDatabase db=this.getReadableDatabase();
        String selectQuery="SELECT * FROM "+ TABLE_USER;
        Cursor c = db.rawQuery(selectQuery, null);
        try {
            if (c != null) {
                c.moveToFirst();
                String userMail = c.getString(c.getColumnIndex("userMail"));
                String password = c.getString(c.getColumnIndex("password"));
                String active = c.getString(c.getColumnIndex("active"));
                user=userMail+" "+password;
            }
            return user;
        }catch (Exception e){
            return null;
        }
    }

    public String createUser(String userMail,String password){

        String existe=userExist();
        if(existe==null) {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("userMail", userMail);
            values.put("password", password);
            values.put("active", true);

            long i = db.insert(TABLE_USER, null, values);
            return "" + i;
        }else{
            return "Ya en bd";
        }
    }

    public boolean updatePassUser(String userMail,String password){
        boolean updated =false;

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
        db.update(TABLE_USER, values, "userMail = ?", new String[]{userMail});
        updated=true;
        return updated;
    }

    public void deleteUsers(){
        SQLiteDatabase db=this.getReadableDatabase();
        db.delete(TABLE_USER,null,null);
    }
}
