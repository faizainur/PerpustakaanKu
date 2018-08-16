package com.jurnalit.perpustakaanku.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BooksDataSource {
    DbHelper dbHelper;
    SQLiteDatabase database;
    BookModel book = new BookModel();
    public BooksDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public boolean addBook(BookModel book){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("judul_buku", book.getJudulBuku());
        contentValues.put("isbn", book.getIsbn());
        contentValues.put("tahun_terbit", book.getTahunTerbit());
        contentValues.put("penerbit", book.getPenerbit());
        contentValues.put("kategori", book.getKategori());
        contentValues.put("jumlah", book.getJumlah());
        contentValues.put("kode_warna", book.getKodeWarna());
        contentValues.put("kualitas", book.getKualitas());
        contentValues.put("rangkuman", book.getRangkuman());

        long id = database.insert("books", null, contentValues);
        close();
        return id > 0;
    }

    public List<BookModel> getAllData(){
        open();
        List<BookModel> books = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * from books", new String[]{});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            book = fetchRow(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return books;
    }

    public BookModel fetchRow(Cursor cursor){

        book.setId(cursor.getLong(0));
        book.setJudulBuku(cursor.getString(1));
        book.setIsbn(cursor.getString(2));
        book.setTahunTerbit(cursor.getString(3));
        book.setPenerbit(cursor.getString(4));
        book.setKategori(cursor.getString(5));
        book.setJumlah(cursor.getString(6));
        book.setKodeWarna(cursor.getInt(7));
        book.setKualitas(cursor.getString(8));
        book.setRangkuman(cursor.getString(9));

        return book;
    }
    public void clearData(){
        open();
        database.execSQL("DELETE FROM books");
        close();
    }

    public BookModel getBook(long id) {
        open();
        Cursor cursor = database.rawQuery("SELECT * FROM books WHERE id=?", new String[]{Long.toString(id)});
        cursor.moveToFirst();

        book = fetchRow(cursor);
        cursor.close();
        close();

        return book;
    }

    public void removeBook(long id){
        open();
        database.delete("books", "id=?", new String[]{Long.toString(id)});
        close();
    }
}
