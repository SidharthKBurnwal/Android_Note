package com.sidharth.android.navigationdrawer.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sidharth.android.navigationdrawer.R;
import com.sidharth.android.navigationdrawer.model.Note;

import java.util.List;

//Adapter manages the data model and adapts it to the individual entries in the widget
//Provides the data and responsible for creating the views for the individual entry
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private Context context;
    private List<Note> noteList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titles;
        public TextView content;
        public MyViewHolder(View view){
            super(view);
            titles = view.findViewById(R.id.showtitle);
            content = view.findViewById(R.id.showcontent);
        }
    }
    public NoteAdapter (Context context, List<Note> noteList){
        this.context = context;
        this.noteList = noteList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent,false );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        Log.d("hiiiiiiiiiiiiiiiiiiiiii",note.getId()+""+note.getTitle()+""+note.getContent());
        holder.titles.setText(note.getTitle());
        holder.content.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


}
