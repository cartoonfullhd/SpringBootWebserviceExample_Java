package com.example.user.springbootwebserviceexample_java;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder>
{
    private List<Book> bookList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView idList, nameList, authorList;

        public MyViewHolder(View view)
        {
            super(view);
            idList = view.findViewById(R.id.idList);
            nameList = view.findViewById(R.id.nameList);
            authorList = view.findViewById(R.id.authorList);
        }
    }

    public BookAdapter(List<Book> bookList)
    {
        this.bookList = bookList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Book book = bookList.get(position);
        holder.idList.setText(String.valueOf(book.getId()));
        holder.nameList.setText(book.getName());
        holder.authorList.setText(book.getAuthor());
    }

    @Override
    public int getItemCount()
    {
        return bookList.size();
    }

}
