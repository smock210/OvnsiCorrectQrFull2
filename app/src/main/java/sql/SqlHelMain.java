package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelMain extends SQLiteOpenHelper {
    public SqlHelMain(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table myqrtable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "valueText text,"
                + "type text" + ");");

        db.execSQL("create table mytagstable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "description text,"
                + "type text" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
