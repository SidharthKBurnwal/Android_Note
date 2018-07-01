package com.sidharth.android.navigationdrawer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sidharth.android.navigationdrawer.R;
import com.sidharth.android.navigationdrawer.controller.DatabaseHelper;

class UpdateNoteActivity extends AppCompatActivity{
    EditText upNoteTitle;
    EditText upNoteContent;
    Button btnUpdate, btnDelete;
    DatabaseHelper db ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_update);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        upNoteTitle = findViewById(R.id.dialog_uptitle);
        upNoteContent = findViewById(R.id.updatenote);
        db = new DatabaseHelper(this);
        final int pos = Integer.parseInt(getIntent().getStringExtra("position"));
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(upNoteContent.getText().toString())) {
                    Toast.makeText(UpdateNoteActivity.this, "Enter Updated note!", Toast.LENGTH_SHORT).show();
                } else {
                    final String content = upNoteContent.getText().toString();
                    Log.d("Content inside button", content + "");
                    db.update(upNoteTitle.getText().toString(), content);
                    Intent i = new Intent(UpdateNoteActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /// db.delete(pos);
            }
        });
    }
}
