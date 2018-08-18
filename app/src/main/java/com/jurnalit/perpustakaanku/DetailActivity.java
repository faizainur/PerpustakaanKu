package com.jurnalit.perpustakaanku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.jurnalit.perpustakaanku.Database.BookModel;
import com.jurnalit.perpustakaanku.Database.BooksDataSource;


public class DetailActivity extends AppCompatActivity {

    BookModel book;
    BooksDataSource dataSource = new BooksDataSource(this);

    TextView judulBuku;
    TextView isbn;
    TextView penerbit;
    TextView tahunTerbit;
    TextView kategori;
    TextView jumlah;
    TextView rangkuman;
    RatingBar kualitas;
    LinearLayout colorCodeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Long id = getIntent().getLongExtra("id", 0);
        Log.d("Data id's ", "the data id's " + id );
        book = dataSource.getBook(id);

        judulBuku = findViewById(R.id.tv_detail_judul_buku);
        isbn = findViewById(R.id.tv_detail_isbn_buku);
        tahunTerbit = findViewById(R.id.tv_detail_tahun_terbit_buku);
        kategori = findViewById(R.id.tv_detail_kategori_buku);
        penerbit = findViewById(R.id.tv_detail_penerbit_buku);
        jumlah = findViewById(R.id.tv_detail_jumlah_buku);
        rangkuman = findViewById(R.id.tv_detail_rangkuman_buku);
        kualitas = findViewById(R.id.rating_book_details);
        colorCodeBox = findViewById(R.id.linear_layout_kode_warna);

        judulBuku.setText(book.getJudulBuku());
        isbn.setText(book.getIsbn());
        tahunTerbit.setText(book.getTahunTerbit());
        kategori.setText(book.getKategori());
        penerbit.setText(book.getPenerbit());
        jumlah.setText(book.getJumlah());
        rangkuman.setText(book.getRangkuman());
        kualitas.setRating(Float.parseFloat(book.getKualitas()));
        colorCodeBox.setBackgroundColor(book.getKodeWarna());
    }
}
