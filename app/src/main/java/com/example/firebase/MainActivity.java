package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton BtnAddNote;

    RecyclerView rvNotes;
    NotesAdaptor myAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
//        FirebaseDatabase.getInstance().getReference()
//                .child("Notes")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot)
//                    {
//                        String result ="";
//                        for (DataSnapshot sShot: snapshot.getChildren()){
//                            result=result
//                                    +"Title" +sShot.child("Title").getValue(String.class)
//                                    +"\nDescription"+sShot.child("Description").getValue(String.class)
//                                    +"\nTimeStamp"+sShot.child("timeStamp").getValue(String.class)
//                                    +"\n\n";
//                        }
//                        tvResult.setText(result);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        BtnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.new_note_design,null);
                TextView timeStamp=v.findViewById(R.id.timeStamp);
                TextInputEditText etTitle=v.findViewById(R.id.etTitle);
                TextInputEditText etDescription=v.findViewById(R.id.etDescription);

                SimpleDateFormat formattor= new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss" );
                Date date=new Date();
                timeStamp.setText(formattor.format(date));
                AlertDialog.Builder addNoteDialog=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add New Note")
                        .setView(v)
                        .setPositiveButton("save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    String Title=etTitle.getText().toString().trim();
                                    String Description=etDescription.getText().toString().trim();

                                HashMap<String,Object>data=new HashMap<>();
                                data.put("Title",Title);
                                data.put("Description",Description);
                                data.put("timeStamp",timeStamp.getText().toString());
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Notes")
                                        .push()
                                        .setValue(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(MainActivity.this, "Note Ceated", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                addNoteDialog.create();
                addNoteDialog.show();
            }
        });
    }

    private void init(){
        BtnAddNote=findViewById(R.id.BtnAddNote);
        rvNotes=findViewById(R.id.rvNotes);


    }

    @Override
    protected void onStart() {
        super.onStart();Query query = FirebaseDatabase.getInstance().getReference().child("Notes");
        FirebaseRecyclerOptions<Note> options =
                new FirebaseRecyclerOptions.Builder<Note>()
                        .setQuery(query, Note.class)
                        .build();
        myAdaptor = new NotesAdaptor(options,this);
        rvNotes.setAdapter(myAdaptor);
        myAdaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdaptor.stopListening();
    }
}