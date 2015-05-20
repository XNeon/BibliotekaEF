package pkks.etf.bibliotekaef;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pkks.etf.bibliotekaef.API.JSON;
import pkks.etf.bibliotekaef.types.BookEntry;
import pkks.etf.bibliotekaef.util.BitmapUtils;


public class BookInputActivity extends ActionBarActivity implements com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener {
    private static final int REQUEST_CODE_SCANNER = 5125;
    private static final int REQUEST_CODE_FILE_BROWSER = 5130;
    private static final int REQUEST_CODE_TAKE_PICTURE = 5150;

    BookEntry currentEntry;

    EditText etName;
    EditText etAuthor;
    EditText etISBN;
    EditText etPCount;
    EditText etDate;
    EditText etDesc;

    public void backPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Potvrdi");
        builder.setMessage("Prekinuti unos?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(-1);
                finish();
                overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
            }
        });
        builder.setNegativeButton("Ne", null);
        builder.create().show();
    }

    public void queryISBN(String ISBN) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage("Pribavljam podatke...");
        progressDialog.show();

        new BookFetcher() {
            @Override
            public void onPostExecute(BookEntry result) {
                if ( result != null ) {
                    if ( result.title != null && result.title.length() > 0 )
                        etName.setText(result.title);

                    if ( result.author != null && result.author.length() > 0 )
                        etAuthor.setText(result.author);

                    if ( result.description != null && result.description.length() > 0 )
                        etDesc.setText(result.description);

                    if ( result.pageCount > 0 )
                        etPCount.setText(result.pageCount + "");

                    progressDialog.dismiss();
                } else {
                    Toast.makeText(BookInputActivity.this, "Neuspjesno!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        }.execute(ISBN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if ( requestCode == REQUEST_CODE_SCANNER && resultCode == 1 ) {
            currentEntry.ISBN = data.getStringExtra("ISBN");
            etISBN.setText(currentEntry.ISBN);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Pretraziti google Books servis za ostale podatke?");
            builder.setNegativeButton("Ne", null);
            builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryISBN(currentEntry.ISBN);
                        }
                    });
                }
            });
            builder.create().show();
        } else if ( requestCode == REQUEST_CODE_FILE_BROWSER && resultCode == Activity.RESULT_OK ) {

            try {
                final ImageView ivAddPic = ((ImageView)findViewById(R.id.ivAddPic));
                ivAddPic.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromStream(new BufferedInputStream(getContentResolver().openInputStream(Uri.parse(data.getDataString()))),
                                    ivAddPic.getWidth(),
                                    ivAddPic.getHeight());

                            ivAddPic.setImageBitmap(bitmap);
                            ivAddPic.startAnimation(AnimationUtils.loadAnimation(BookInputActivity.this, R.anim.abc_fade_in));
                            findViewById(R.id.tvPicPrompt).setVisibility(View.GONE);

                            File outDir = new File(Environment.getExternalStorageDirectory() + "/bibliotekaef/covers/");
                            outDir.mkdirs();

                            String fName = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(new Date()) + ".jpg";
                            File outFile = new File(outDir.getAbsolutePath(), fName);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(outFile));

                            currentEntry.coverImage = new File(outFile.getAbsolutePath());
                        } catch (Exception er) {
                            Toast.makeText(BookInputActivity.this, "Neuspjesno ucitavanje!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                ivAddPic.invalidate();

            } catch ( Exception er ) {
                Toast.makeText(this, "Neuspjesno ucitavanje!",Toast.LENGTH_LONG).show();
            }
        } else if ( requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == Activity.RESULT_OK ) {
            try {
                File outDir = new File(Environment.getExternalStorageDirectory() + "/bibliotekaef/covers/");
                outDir.mkdirs();

                String fName = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(new Date()) + ".jpg";
                File outFile = new File(outDir.getAbsolutePath(), fName);
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(outFile));
                ((ImageView)findViewById(R.id.ivAddPic)).setImageBitmap(photo);

                currentEntry.coverImage = new File(outFile.getAbsolutePath());
            } catch ( Exception er ) {
                Toast.makeText(this, "Neuspjesno ucitavanje!",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        backPrompt();
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        readData();
        out.putSerializable("currentEntry", currentEntry);
    }

    public void readData() {
        currentEntry.author = etAuthor.getText().toString();
        currentEntry.title = etName.getText().toString();
        currentEntry.ISBN = etISBN.getText().toString();
        currentEntry.description = etDesc.getText().toString();
        try {
            currentEntry.pageCount = Integer.parseInt(etPCount.getText().toString());
        } catch ( Exception er ) {
            currentEntry.pageCount = 0;
        }
        try {
            currentEntry.setDateFromString(etDate.getText().toString());
        } catch ( Exception er ) {
            //ilegalan text u editText-u
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_input);
        findViewById(R.id.tvFocusEater).requestFocus();

        etAuthor = (EditText)findViewById(R.id.etAuthor);
        etDate   = (EditText)findViewById(R.id.etDate);
        etDesc   = (EditText)findViewById(R.id.etDesc);
        etISBN   = (EditText)findViewById(R.id.etISBN);
        etPCount = (EditText)findViewById(R.id.etPageCount);
        etName   = (EditText)findViewById(R.id.etName);

        if ( savedInstanceState != null )
            currentEntry = (BookEntry)savedInstanceState.getSerializable("currentEntry");
        else if ( getIntent().getSerializableExtra("book") != null ) {
            currentEntry = (BookEntry)getIntent().getSerializableExtra("book");
        }

        if ( currentEntry == null ) {
            currentEntry = new BookEntry();

            adjustEditText(etAuthor);
            adjustEditText(etDate);
            adjustEditText(etDesc);
            adjustEditText(etISBN);
            adjustEditText(etPCount);
            adjustEditText(etName);
        } else {
            if ( currentEntry.coverImage != null && currentEntry.coverImage.exists() ) {
                final ImageView ivAddPic = (ImageView)findViewById(R.id.ivAddPic);
                ivAddPic.post(new Runnable() {
                    @Override
                    public void run() {
                        ivAddPic.setImageBitmap(
                                BitmapUtils.decodeSampledBitmapFromPath(currentEntry.coverImage.getAbsolutePath(),
                                        ivAddPic.getWidth(),
                                        ivAddPic.getHeight()));

                        findViewById(R.id.tvPicPrompt).setVisibility(View.GONE);
                        ivAddPic.startAnimation(AnimationUtils.loadAnimation(BookInputActivity.this, R.anim.abc_fade_in));
                    }
                });
                ivAddPic.invalidate();
            }

            if ( currentEntry.author != null && currentEntry.author.trim().length() > 0 )
                etAuthor.setText(currentEntry.author);
            else
                adjustEditText(etAuthor);

            if ( currentEntry.getFormattedDate() != null  )
                etDate.setText(currentEntry.getFormattedDate());
            else
                adjustEditText(etDate);

            if ( currentEntry.description != null && currentEntry.description.trim().length() > 0 )
                etDesc.setText(currentEntry.description);
            else
                adjustEditText(etDesc);

            if ( currentEntry.ISBN != null && currentEntry.ISBN.trim().length() > 0 )
                etISBN.setText(currentEntry.ISBN);
            else
                adjustEditText(etISBN);

            if (currentEntry.pageCount != 0 )
                etPCount.setText(currentEntry.pageCount + "");
            else
                adjustEditText(etPCount);

            if ( currentEntry.title != null && currentEntry.title.trim().length() > 0 )
                etName.setText(currentEntry.title);
            else
                adjustEditText(etName);
        }


        findViewById(R.id.ivAddPic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookInputActivity.this);
                        builder.setTitle("Izaberi izvor:");
                        builder.setAdapter(new ArrayAdapter<>(BookInputActivity.this, android.R.layout.simple_list_item_1, new String[]{"Sa uredjaja", "Uslikaj"}), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent;
                                    if (Build.VERSION.SDK_INT < 19){
                                        intent = new Intent();
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        intent.setType("*/*");
                                        startActivityForResult(intent, REQUEST_CODE_FILE_BROWSER);
                                    } else {
                                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.setType("*/*");
                                        startActivityForResult(intent, REQUEST_CODE_FILE_BROWSER);
                                    }
                                } else if (which == 1) {
                                    Intent cameraIntent = new Intent(
                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);
                                }
                            }
                        });
                        builder.setNegativeButton("Prekid", null);
                        builder.create().show();
                    }
                });
            }
        });


        findViewById(R.id.btISBNScan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BookInputActivity.this, ScannerActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SCANNER);
                    }
                });
            }
        });

        findViewById(R.id.btCal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(BookInputActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
                datePickerDialog.show(getSupportFragmentManager(), "");
            }
        });


        findViewById(R.id.btSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
                if ( currentEntry.coverImage == null ) {
                    Toast.makeText(BookInputActivity.this, "Nije izabrana slika!", Toast.LENGTH_LONG).show();
                    return;
                } else if ( currentEntry.description.trim().length() == 0 ||
                            currentEntry.ISBN.trim().length() == 0 ||
                            currentEntry.author.trim().length() == 0 ||
                            currentEntry.title.trim().length() == 0 ) {

                    Toast.makeText(BookInputActivity.this, "Nisu uneseni svi podaci!", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent data = new Intent();
                if (getIntent().getIntExtra("requestCode", 0) == BibliotekaEF.REQUEST_CODE_BOOK_INPUT) {
                    currentEntry.status = BookEntry.STATUS_NOT_READ;
                }

                data.putExtra("book", currentEntry);

                setResult(1, data);
                finish();
                overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
            }
        });
    }

    public void adjustEditText(EditText editText) {
        editText.setTextColor(Color.GRAY);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            private boolean first = true;
            private String standardText = "";

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ( first && hasFocus ) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                    standardText = ((EditText)v).getText().toString();
                    ((EditText) v).setText("");
                    ((EditText) v).setTextColor(Color.BLACK);
                    first = false;
                } else if ( !hasFocus ) {
                    if ( ((EditText)v).getText().toString().trim().length() == 0 ) {
                        ((EditText)v).setText(standardText);
                        ((EditText) v).setTextColor(Color.GRAY);
                        first = true;
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            backPrompt();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.DAY_OF_MONTH, d);

        currentEntry.releaseDate = calendar.getTime();

        etDate.setText(currentEntry.getFormattedDate());
    }


    public class BookFetcher extends AsyncTask<String, Integer, BookEntry> {
        @Override
        protected BookEntry doInBackground(String... params) {
            if ( params.length == 0 ) return null;


            return JSON.FetchBookInfo(params[0]);
        }
    }

}
