package com.jurnalit.perpustakaanku;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jurnalit.perpustakaanku.Database.BookModel;


import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BookAdapter extends ArrayAdapter<BookModel> {
    private Context mcontext;
    private HashMap<Integer, Boolean> mSelection = new HashMap<>();
    RelativeLayout relativeLayout;
    public BookAdapter(@NonNull Context context, @NonNull List<BookModel> objects) {
        super(context, R.layout.adapter_book_list, objects);
        mcontext = context;
    }

    public void setNewSelection(int position, boolean value){
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position){
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition(int position){
        return mSelection.keySet();
    }

    public void removeSelection(int position){
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection(){
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = null;



        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from((getContext()));
            listItemView = inflater.inflate(R.layout.adapter_book_list, parent, false);
        } else {
            listItemView = convertView;
        }


        TextView judulBuku = listItemView.findViewById(R.id.tv_title_book);
        TextView kategoriBuku = listItemView.findViewById(R.id.tv_book_category);
        TextView penerbit = listItemView.findViewById(R.id.tv_book_publisher);
        RatingBar ratingBuku = listItemView.findViewById(R.id.rating_book_list);
        View kodeWarna = listItemView.findViewById(R.id.view_color_code);
        relativeLayout = listItemView.findViewById(R.id.rl_adapter);

        if (isPositionChecked(position)){
            relativeLayout.setBackgroundColor(Color.HSVToColor(30, new float[]{248, 99, 56}));
        } else {
            relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        BookModel book = getItem(position);
        judulBuku.setText(book.getJudulBuku());
        kategoriBuku.setText(book.getKategori());
        penerbit.setText(book.getPenerbit());
        ratingBuku.setRating(Float.parseFloat(book.getKualitas()));
        kodeWarna.setBackgroundColor(book.getKodeWarna());

        return listItemView;
    }
}
