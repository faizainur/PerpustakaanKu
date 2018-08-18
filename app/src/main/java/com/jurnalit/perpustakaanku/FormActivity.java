package com.jurnalit.perpustakaanku;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;
import com.jurnalit.perpustakaanku.Database.BookModel;
import com.jurnalit.perpustakaanku.Database.BooksDataSource;

import java.util.ArrayList;
import java.util.Calendar;


public class FormActivity extends AppCompatActivity implements ColorPickerDialogListener{

    private static final int DIALOG_ID = 0;
    int colorVal;
    EditText judulBuku;
    EditText isbn;
    EditText kodeWarna;
    EditText rangkuman;
    EditText kategoriLain;
    TextView jumlah;
    Spinner tahunTerbit;
    Spinner penerbit;
    RadioButton rbKategoriNovel;
    RadioButton rbKategoriKomputer;
    RadioButton rbKategoriAgama;
    RadioButton rbKategoriLain;
    RatingBar rbKualitas;
    SeekBar sbJumlah;
    Button button;
    BooksDataSource dataSource = new BooksDataSource(this);
    BookModel book = new BookModel();
    String kategori;
    ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
    int thisYear;
    ArrayList<String> years = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        thisYear = Calendar.getInstance().get(Calendar.YEAR);

        judulBuku = findViewById(R.id.et_book_title);
        isbn = findViewById(R.id.et_isbn);
        kodeWarna = findViewById(R.id.et_color_code);
        rangkuman = findViewById(R.id.et_rangkuman);
        kategoriLain = findViewById(R.id.et_lain_lain);
        tahunTerbit = findViewById(R.id.spinner_release_year);
        penerbit = findViewById(R.id.spinner_publisher);
        rbKategoriKomputer = findViewById(R.id.rb_buku_komputer);
        rbKategoriLain = findViewById(R.id.rb_lain_lain);
        rbKategoriNovel = findViewById(R.id.rb_buku_novel);
        rbKategoriAgama = findViewById(R.id.rb_buku_agama);
        rbKualitas = findViewById(R.id.rating_book_form);
        sbJumlah = findViewById(R.id.sb_jumlah);
        jumlah = findViewById(R.id.tv_seek_value);

        sbJumlah.setOnSeekBarChangeListener(jumlahListener);


        rbKategoriLain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    kategoriLain.setEnabled(true);
                } else {
                    kategoriLain.setEnabled(false);
                }
            }
        });
        kodeWarna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerDialog.newBuilder()
                        .setColor(Color.BLUE)
                        .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                        .setColorShape(1)
                        .setDialogTitle(R.string.cp_title)
                        .setAllowCustom(true)
                        .setDialogId(DIALOG_ID)
                        .show(FormActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_save :
                simpan();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private SeekBar.OnSeekBarChangeListener jumlahListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            jumlah.setText(Integer.toString(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void simpan(){
        book.setJudulBuku(judulBuku.getText().toString().trim());
        book.setIsbn(isbn.getText().toString().trim());
        book.setTahunTerbit(tahunTerbit.getSelectedItem().toString());
        book.setPenerbit(penerbit.getSelectedItem().toString());
        book.setRangkuman(rangkuman.getText().toString().trim());
        book.setKodeWarna(colorVal);
        book.setJumlah(jumlah.getText().toString());

        float kualitasVal = rbKualitas.getRating();
        String kualitas = Float.toString(kualitasVal);
        book.setKualitas(kualitas);



        if (rbKategoriAgama.isChecked()){
            kategori = "Agama";
        } else if (rbKategoriKomputer.isChecked()){
            kategori = "Komputer";
        } else if (rbKategoriNovel.isChecked()){
            kategori = "Novel";
        } else if (kategoriLain.isEnabled()){
            kategori = kategoriLain.getText().toString().trim();
        }
        book.setKategori(kategori);

        boolean getSuccess = dataSource.addBook(book);
        if (getSuccess){
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm(){


        return true;
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        kodeWarna.setText(Integer.toHexString(color));
        colorVal = color;
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
