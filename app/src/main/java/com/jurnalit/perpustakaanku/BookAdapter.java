package com.jurnalit.perpustakaanku;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jurnalit.perpustakaanku.Database.BookModel;


import java.util.List;

public class BookAdapter extends ArrayAdapter<BookModel> {
    public BookAdapter(@NonNull Context context, @NonNull List<BookModel> objects) {
        super(context, R.layout.adapter_book_list, objects);
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



        BookModel book = getItem(position);
        judulBuku.setText(book.getJudulBuku());
        kategoriBuku.setText(book.getKategori());
        penerbit.setText(book.getPenerbit());
        ratingBuku.setRating(Float.parseFloat(book.getKualitas()));
        kodeWarna.setBackgroundColor(book.getKodeWarna());

        return listItemView;
    }
}
