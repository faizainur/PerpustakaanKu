package com.jurnalit.perpustakaanku;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
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
    FloatingActionButton floatEdit;
    FloatingActionButton floatClear;
    private ArrayList<BookModel> selectedItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.lv_books_list);
        floatEdit = findViewById(R.id.floating_item_edit);
        floatClear = findViewById(R.id.floating_item_clear);
        floatEdit.setOnClickListener(addFABListener);
        floatClear.setOnClickListener(clearDataFABListener);
        

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(multiChoiceModeListener);
        listView.setOnItemClickListener(itemClickListener);

    }

    @Override
    protected void onResume() {
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
        getData();
        super.onResume();
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
        private int nr;
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            if (checked){
                nr++;
                adapter.setNewSelection(position, checked);
                BookModel bookModel = booksList.get((int)id);
                selectedItem.add(bookModel);
            } else {
                nr--;
                adapter.removeSelection(position);
                BookModel bookModel = booksList.get((int)id);
                selectedItem.remove(selectedItem.indexOf(bookModel));
            }

            if (nr > 1){
                mode.getMenu().findItem(R.id.menu_edit).setVisible(false);
            } else {
                mode.getMenu().findItem(R.id.menu_edit).setVisible(true);
            }
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

            switch (item.getItemId()){
                case R.id.menu_edit :
                    BookModel bookModel = selectedItem.get(0);
                    Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                    intent.putExtra("id", bookModel.getId());
                    startActivity(intent);
                    mode.finish();
                    return true;
                case R.id.menu_remove :
                    for (int i = 0; i < selectedItem.size(); i++){
                        BookModel selectedBook = selectedItem.get(i);
                        removeData(selectedBook.getId());
                    }
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

    private FloatingActionButton.OnClickListener addFABListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FormActivity.class);
            startActivity(intent);
        }
    };

    private FloatingActionButton.OnClickListener clearDataFABListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (clearData()){
                Toast.makeText(ListActivity.this, "Clear data successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ListActivity.this, "Unable to clear data", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
