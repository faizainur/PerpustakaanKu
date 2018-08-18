package com.jurnalit.perpustakaanku;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
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
    MenuItem menuItem;
    private List<Long> itemIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = findViewById(R.id.lv_books_list);

        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
        getData();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(multiChoiceModeListener);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
        listView.setOnItemClickListener(itemClickListener);
    }

    private boolean getData(){
        booksList.clear();
        booksList.addAll(dataSource.getAllData());
        adapter = new BookAdapter(ListActivity.this, booksList);
        listView.setAdapter(adapter);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView)
                menu.findItem(R.id.menu_search).getActionView();
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
                if (clearData()){
                    Toast.makeText(ListActivity.this, "Clear data successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListActivity.this, "Unable to clear data", Toast.LENGTH_SHORT).show();
                }
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

    private AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
        private boolean editMenuIsVisibled;
        private int nr;
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            if (checked){
                nr++;
                adapter.setNewSelection(position, checked);
                itemIds.add(id);
            } else {
                nr--;
                adapter.removeSelection(position);
                itemIds.remove(itemIds.indexOf(id));
            }

            if (nr > 1){
                mode.getMenu().findItem(R.id.menu_edit).setVisible(false);
            } else {
                mode.getMenu().findItem(R.id.menu_edit).setVisible(true);
            }
            Log.d("Position", "Item selected position" + itemIds);
            mode.setTitle(nr + " selected");
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            nr = 0;
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_context, menu);
            menuItem = findViewById(R.id.menu_edit);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int idEditVal = (int)(long)itemIds.get(0);
            BookModel bookModel = booksList.get(idEditVal);
            switch (item.getItemId()){
                case R.id.menu_edit :
                    Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                    intent.putExtra("id", bookModel.getId());
                    startActivity(intent);
                    mode.finish();
                    Toast.makeText(ListActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_remove :
                    // IN PROGRESS
//                    for (int i = 0; i < itemIds.size(); i++){
//                        BookModel book = booksList.get(0);
//                        removeData(book.getId());
//                        int idRemoveVal = (int)(long)itemIds.get(i);
//                        if (i == 0){
//                            BookModel book = booksList.get(idRemoveVal);
//                            removeData(book.getId());
//                        } else if (i >= 1){
//                            BookModel book = booksList.get(idRemoveVal-idRemoveVal);
//                            removeData(book.getId());
//                        }
//                    }
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
        }
    };

    private boolean clearData(){
        dataSource.clearData();
        if(getData()){
            return true;
        }
        return false;
    }

    private void removeData(long id){
        dataSource.removeBook(id);
        getData();
    }
}
