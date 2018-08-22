package com.jurnalit.perpustakaanku;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
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
    String kategori = "";
    String tahunTerbitSelected;
    String penerbitSelected;
    ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
    int thisYear;
    long id;
    String kualitas;
    ArrayList<String> years = new ArrayList<>();
    ArrayAdapter adapterTahun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        id = getIntent().getLongExtra("id", 0);



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
        thisYear = Calendar.getInstance().get(Calendar.YEAR);

        for(int i = 1900; i <= thisYear; i++){
            if(i == 1900){
                years.add("Pilih Tahun Terbit");
                years.add(Integer.toString(i));
            }
            years.add(Integer.toString(i));
        }
        adapterTahun = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item , years);
        tahunTerbit.setAdapter(adapterTahun);

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

        if (id > 0 ){
            book = dataSource.getBook(id);
            judulBuku.setText(book.getJudulBuku());
            isbn.setText(book.getIsbn());
            kodeWarna.setText(Integer.toHexString(book.getKodeWarna()));
            colorVal = book.getKodeWarna();
            rangkuman.setText(book.getRangkuman());

            String kategoriVal = book.getKategori();
            switch (kategoriVal){
                case "Komputer" :
                    rbKategoriKomputer.setChecked(true);
                    break;
                case "Agama" :
                    rbKategoriAgama.setChecked(true);
                    break;
                case "Novel" :
                    rbKategoriNovel.setChecked(true);
                    break;
                default :
                    rbKategoriLain.setChecked(true);
                    kategoriLain.setText(kategoriVal);
            }

            rbKualitas.setRating(Float.parseFloat(book.getKualitas()));
            sbJumlah.setProgress(Integer.parseInt(book.getJumlah()));

            String yearVal = book.getTahunTerbit();
            tahunTerbit.setSelection(adapterTahun.getPosition(yearVal));
            ArrayAdapter<String> bookPublisher = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.publisher));
            penerbit.setSelection(bookPublisher.getPosition(book.getPenerbit()));
        }
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
        tahunTerbitSelected = tahunTerbit.getSelectedItem().toString();
        penerbitSelected = penerbit.getSelectedItem().toString();

        book.setJudulBuku(judulBuku.getText().toString().trim());
        book.setIsbn(isbn.getText().toString().trim());
        book.setTahunTerbit(tahunTerbitSelected);
        book.setPenerbit(penerbitSelected);
        book.setRangkuman(rangkuman.getText().toString().trim());
        book.setKodeWarna(colorVal);
        book.setJumlah(jumlah.getText().toString());

        float kualitasVal = rbKualitas.getRating();
        kualitas = Float.toString(kualitasVal);
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

        boolean validateSuccessful = validateForm(book);
        if (validateSuccessful){
            boolean getSuccess;
            if (id > 0){
                getSuccess = dataSource.updateData(book);
                if (getSuccess){
                    Toast.makeText(this, "Update data successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Unable to update data", Toast.LENGTH_SHORT).show();
                }
            } else {
                getSuccess = dataSource.addBook(book);
                if (getSuccess){
                    Toast.makeText(this, "Adding data successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Unable to add data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    @Override
    public void onColorSelected(int dialogId, int color) {
        kodeWarna.setText(Integer.toHexString(color));
        colorVal = color;
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    private boolean validateForm(BookModel bookModel){
        
        if (bookModel.getJudulBuku().isEmpty()){
            judulBuku.setError("This field is required");
            judulBuku.requestFocus();
            return false;
        }
        if (bookModel.getIsbn().isEmpty()){
            isbn.setError("This field is required");
            isbn.requestFocus();
            return false;
        }
        if (tahunTerbitSelected.isEmpty() || tahunTerbitSelected.equals("Pilih Tahun Terbit")){
            Toast.makeText(this, "All field are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (penerbitSelected.isEmpty() || penerbitSelected.equals("Pilih Penerbit")){
            Toast.makeText(this, "Choose the book publisher", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (kategori.isEmpty()){
            Toast.makeText(this, "Choose the book category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bookModel.getJumlah().isEmpty()){
            jumlah.setError("This field is required");
            return false;
        }
        if (kodeWarna.getText().toString().isEmpty()){
            kodeWarna.setError("This field is required");
            kodeWarna.requestFocus();
            return false;
        }
        return true;
    }
}
