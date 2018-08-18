package com.jurnalit.perpustakaanku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.jurnalit.perpustakaanku.Database.BookModel;
import com.jurnalit.perpustakaanku.Database.BooksDataSource;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    List<BookModel> bookModels;
    BooksDataSource dataSource = new BooksDataSource(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ListView listView = findViewById(R.id.list_test);
        bookModels = dataSource.getAllData();
        BookAdapter adapter = new BookAdapter(this, bookModels);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
}
