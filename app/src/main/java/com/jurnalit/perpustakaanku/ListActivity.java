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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.jurnalit.perpustakaanku.Database.BookModel;
import com.jurnalit.perpustakaanku.Database.BooksDataSource;

import java.lang.reflect.GenericArrayType;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashSet;
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
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
        getData();
        listView.setOnItemClickListener(itemClickListener);
        registerForContextMenu(listView);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
    private void getData(){
        booksList.clear();
        booksList.addAll(dataSource.getAllData());
        HashSet<BookModel> hashSet = new HashSet<>();
        hashSet.addAll(booksList);
        booksList.clear();
        booksList.addAll(hashSet);
        adapter = new BookAdapter(ListActivity.this, booksList);
        listView.setAdapter(adapter);
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
    private ListView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            BookModel book = adapter.getItem(position);
            intent.putExtra("id", book.getId());
            startActivity(intent);
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        BookModel book = booksList.get(menuInfo.position);
        switch (item.getItemId()){
            case R.id.menu_edit :
                Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                intent.putExtra("id", book.getId());
                startActivity(intent);
                break;
            case R.id.menu_remove :
                dataSource.removeBook(book.getId());
                booksList.remove(menuInfo.position);
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
