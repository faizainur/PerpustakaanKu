package com.jurnalit.perpustakaanku;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jurnalit.perpustakaanku.Database.BookModel;
import com.jurnalit.perpustakaanku.Database.BooksDataSource;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    BookAdapter adapter;
    BooksDataSource dataSource = new BooksDataSource(this);
    List<BookModel> books = new ArrayList<>();
    BookModel book;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        listView = findViewById(R.id.list_search_result);

        listView.setOnItemClickListener(itemClickListener);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            CharSequence title = "Searching for " + "'" + query + "'";
            setTitle(title);

            books = dataSource.search(query);
            adapter = new BookAdapter(this, books);
            listView.setAdapter(adapter);
        }
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
}
