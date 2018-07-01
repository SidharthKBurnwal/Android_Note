package com.sidharth.android.navigationdrawer.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sidharth.android.navigationdrawer.R;
import com.sidharth.android.navigationdrawer.controller.DatabaseHelper;

public class CreateNoteActivity extends AppCompatActivity {

    EditText noteTitle;
    EditText noteContent;
    Button btnSav;

    DatabaseHelper db ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_dialog);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        noteTitle = (EditText) findViewById(R.id.dialog_title);
        Log.d("noteTitle first",noteTitle+"");

        noteContent = (EditText) findViewById(R.id.note);
        Log.d("noteContent first",noteContent+"");

        btnSav = (Button) findViewById(R.id.btnSave);
        db = new DatabaseHelper(CreateNoteActivity.this);
        btnSav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(noteTitle.getText().toString())) {
                    Toast.makeText(CreateNoteActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                } else {
                    final String title = noteTitle.getText().toString();
                    final String content = noteContent.getText().toString();
                    Log.d("Title inside button", title + "");
                    Log.d("Content inside button", content + "");
                    insert(title, content);
                    Intent i = new Intent(CreateNoteActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
    }

    public void insert(String title, String content){
        Log.d("title before insert ", title);
        Log.d("content before insert ", content);
        long id = db.insertNote(title,content);
        Log.d("return from insert ",id+"");
    }



}
