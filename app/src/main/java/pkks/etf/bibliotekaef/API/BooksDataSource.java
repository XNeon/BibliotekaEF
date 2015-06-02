package pkks.etf.bibliotekaef.API;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pkks.etf.bibliotekaef.types.BookEntry;

public class BooksDataSource {
    // Database fields
    private SQLiteDatabase  database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.COL_ID,
            DatabaseHelper.COL_TITLE,
            DatabaseHelper.COL_AUTHOR,
            DatabaseHelper.COL_ISBN,
            DatabaseHelper.COL_DESC,
            DatabaseHelper.COL_CIMAGE,
            DatabaseHelper.COL_DATE,
            DatabaseHelper.COL_PCOUNT,
            DatabaseHelper.COL_STATUS
    };

    public BooksDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void updateBook(BookEntry entry) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITLE, entry.title);
        values.put(DatabaseHelper.COL_AUTHOR, entry.author);
        values.put(DatabaseHelper.COL_CIMAGE, entry.coverImage.getAbsolutePath());
        values.put(DatabaseHelper.COL_DATE, entry.releaseDate.getTime());
        values.put(DatabaseHelper.COL_DESC, entry.description);
        values.put(DatabaseHelper.COL_ISBN, entry.ISBN);
        values.put(DatabaseHelper.COL_PCOUNT, entry.pageCount);
        values.put(DatabaseHelper.COL_STATUS, entry.status);

        database.update(DatabaseHelper.TABLE_BOOKS, values,
                DatabaseHelper.COL_ID + " = " + entry.id, null);
    }

    public void insertBook(BookEntry entry) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITLE, entry.title);
        values.put(DatabaseHelper.COL_AUTHOR, entry.author);
        values.put(DatabaseHelper.COL_CIMAGE, entry.coverImage.getAbsolutePath());
        values.put(DatabaseHelper.COL_DATE, entry.releaseDate.getTime());
        values.put(DatabaseHelper.COL_DESC, entry.description);
        values.put(DatabaseHelper.COL_ISBN, entry.ISBN);
        values.put(DatabaseHelper.COL_PCOUNT, entry.pageCount);
        values.put(DatabaseHelper.COL_STATUS, entry.status);

        long insertId = database.insert(DatabaseHelper.TABLE_BOOKS, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_BOOKS,
                allColumns, DatabaseHelper.COL_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        BookEntry newBook = cursorToBook(cursor);
        cursor.close();

        entry.id = newBook.id;
    }

    public void deleteBook(long id) {

        database.delete(DatabaseHelper.TABLE_BOOKS, DatabaseHelper.COL_ID
                + " = " + id, null);
    }

    public List<BookEntry> getAllBooks() {
        List<BookEntry> comments = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_BOOKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BookEntry entry = cursorToBook(cursor);
            comments.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private BookEntry cursorToBook(Cursor cursor) {
        BookEntry entry = new BookEntry();

        entry.id = cursor.getInt(0);
        entry.title = cursor.getString(1);
        entry.author = cursor.getString(2);
        entry.ISBN = cursor.getString(3);
        entry.description = cursor.getString(4);
        entry.coverImage = new File(cursor.getString(5));
        entry.releaseDate = new Date(cursor.getLong(6));
        entry.pageCount = cursor.getInt(7);
        entry.status = cursor.getInt(8);

        return entry;
    }
}