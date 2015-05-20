package pkks.etf.bibliotekaef.types;

import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pkks.etf.bibliotekaef.R;

/**
 * Created by Adnan on 16.5.2015.
 */
public class BookEntry implements Serializable {
    public long id;
    public int status;


    public String title;
    public String author;
    public String ISBN;
    public String description;

    public File   coverImage;

    public Date releaseDate;

    public int pageCount;


    private static final String DATE_FORMAT = "dd-MM-yyyy";
    public void setDateFromString(String string) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        releaseDate = format.parse(string);
    }

    public String getFormattedDate() {
        if ( releaseDate == null ) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return formatter.format(releaseDate);
    }

    public String getStatus(Context context) {
        switch ( status ) {
            case STATUS_NOT_READ:
                return context.getString(R.string.status_not_read);
            case STATUS_READ:
                return context.getString(R.string.status_read);
            case STATUS_READING:
                return context.getString(R.string.status_reading);
            default:
                return "Nepoznat status!";
        }
    }

    public static final int STATUS_READ = 0;
    public static final int STATUS_NOT_READ = 1;
    public static final int STATUS_READING = 2;
}
