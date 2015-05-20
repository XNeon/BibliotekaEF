package pkks.etf.bibliotekaef;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pkks.etf.bibliotekaef.API.BooksDataSource;
import pkks.etf.bibliotekaef.adapters.ListAdapter;
import pkks.etf.bibliotekaef.types.BookEntry;


public class BibliotekaEF extends ActionBarActivity implements ListAdapter.BookListInterface,
        android.support.v7.widget.SearchView.OnQueryTextListener {
    public static final int REQUEST_CODE_BOOK_INPUT = 100;
    public static final int REQUEST_CODE_BOOK_EDIT = 101;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == REQUEST_CODE_BOOK_INPUT && resultCode == 1 ) {
            BookEntry entry = (BookEntry)data.getSerializableExtra("book");

            BooksDataSource source = new BooksDataSource(this);
            source.open();
            source.insertBook(entry);
            source.close();

            fillList();
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } else if ( requestCode == REQUEST_CODE_BOOK_EDIT && resultCode == 1 ) {
            BookEntry entry = (BookEntry)data.getSerializableExtra("book");

            BooksDataSource source = new BooksDataSource(this);
            source.open();
            source.updateBook(entry);
            source.close();

            fillList();
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void initMainList() {
        RecyclerView mainList = (RecyclerView)findViewById(R.id.mainList);
        mainList.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mainList.setLayoutManager(mLayoutManager);

        fillList();
    }

    public void fillList() {
        RecyclerView mainList = (RecyclerView)findViewById(R.id.mainList);

        BooksDataSource source = new BooksDataSource(this);
        source.open();

        ListAdapter mAdapter = new ListAdapter(this, source.getAllBooks(), this);
        mainList.setAdapter(mAdapter);

        source.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteka_ef);

        initMainList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_biblioteka_e, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fillList();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void newEntryRequested() {
        Intent intent = new Intent(this, BookInputActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BOOK_INPUT);

        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
    }

    public void showChangeStatusDialog(final BookEntry entry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_status));

        builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        new String[]{getString(R.string.status_not_read), getString(R.string.status_read), getString(R.string.status_reading)}),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //not_read
                            entry.status = BookEntry.STATUS_NOT_READ;
                        } else if (which == 1) {
                            //read
                            entry.status = BookEntry.STATUS_READ;
                        } else if (which == 2) {
                            //reading
                            entry.status = BookEntry.STATUS_READING;
                        }

                        BooksDataSource source = new BooksDataSource(BibliotekaEF.this);
                        source.open();
                        source.updateBook(entry);
                        source.close();

                        fillList();
                    }
                });
        builder.setPositiveButton(getString(R.string.cancel), null);
        builder.create().show();
    }

    @Override
    public void bookSelected(ListAdapter adapter, final BookEntry entry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(entry.title);

        builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        new String[]{getString(R.string.edit_status), getString(R.string.edit), getString(R.string.delete_entry)}),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //edit status
                            showChangeStatusDialog(entry);
                        } else if (which == 1) {
                            //edit
                            Intent intent = new Intent(BibliotekaEF.this, BookInputActivity.class);
                            intent.putExtra("book", entry);
                            intent.putExtra("requestCode", REQUEST_CODE_BOOK_EDIT);
                            startActivityForResult(intent, REQUEST_CODE_BOOK_EDIT);

                            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                        } else if (which == 2) {
                            //delete
                            BooksDataSource source = new BooksDataSource(BibliotekaEF.this);
                            source.open();
                            source.deleteBook(entry.id);
                            source.close();

                            fillList();
                        }
                    }
                });
        builder.setPositiveButton(getString(R.string.cancel), null);
        builder.create().show();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        BooksDataSource source = new BooksDataSource(this);
        source.open();
        List<BookEntry> result = new ArrayList<>();
        for ( BookEntry entry : source.getAllBooks() ) {
            if ( entry.title.toUpperCase().contains(s.toUpperCase()) ) {
                result.add(entry);
            } else if ( entry.author.toUpperCase().contains(s.toUpperCase()) ) {
                result.add(entry);
            } else if ( entry.description.toUpperCase().contains(s.toUpperCase()) ) {
                result.add(entry);
            }
        }
        source.close();

        RecyclerView mainList = (RecyclerView)findViewById(R.id.mainList);
        ListAdapter mAdapter = new ListAdapter(this, result, this, false);
        mainList.setAdapter(mAdapter);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
