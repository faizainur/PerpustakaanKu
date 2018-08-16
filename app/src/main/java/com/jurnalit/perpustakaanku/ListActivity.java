package com.jurnalit.perpustakaanku;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.jurnalit.perpustakaanku.Database.BookModel;
import com.jurnalit.perpustakaanku.Database.BooksDataSource;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<BookModel> booksList = new ArrayList<>();
    BookAdapter adapter;
    ListView listView;

    BooksDataSource dataSource = new BooksDataSource(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.lv_books_list);
    }

    @Override
    protected void onResume() {
        booksList = dataSource.getAllData();

        Log.d("Student List", "Book List size" + booksList.size());
        adapter = new BookAdapter(this, booksList);
        listView.setAdapter(adapter);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add :
                Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_clear_data :
                dataSource.clearData();
                booksList.clear();
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
