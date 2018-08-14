package com.jurnalit.perpustakaanku.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, "perpustakaanku", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE books (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "judul_buku TEXT," +
                "isbn TEXT," +
                "tahun_terbit TEXT," +
                "penerbit TEXT," +
                "kategori TEXT," +
                "jumlah TEXT," +
                "kode_warna TEXT," +
                "kualitas TEXT," +
                "rangkuman TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
