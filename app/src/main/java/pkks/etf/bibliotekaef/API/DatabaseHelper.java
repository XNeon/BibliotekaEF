package pkks.etf.bibliotekaef.API;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_BOOKS = "books";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_ISBN = "isbn";
    public static final String COL_DESC = "description";
    public static final String COL_CIMAGE = "cover_image";
    public static final String COL_DATE = "release_date";
    public static final String COL_PCOUNT = "page_count";
    public static final String COL_STATUS = "status";

    private static final String DATABASE_NAME = "biblioteka.db";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_BOOKS + "(" + COL_ID
            + " integer primary key autoincrement, " + COL_TITLE
            + " varchar(255) not null, " + COL_AUTHOR
            + " varchar(255) not null, " + COL_ISBN
            + " varchar(255) not null, " + COL_DESC
            + " text not null, " + COL_PCOUNT
            + " integer not null, " + COL_DATE
            + " long not null, " + COL_CIMAGE
            + " text not null, " + COL_STATUS
            + " integer not null);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }
}
