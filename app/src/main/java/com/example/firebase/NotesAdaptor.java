package com.example.firebase;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class NotesAdaptor extends FirebaseRecyclerAdapter<Note, NotesAdaptor.NotesViewHolder> {
    Context parent;
    public NotesAdaptor(@NonNull FirebaseRecyclerOptions<Note> options, Context context) {
        super(options);
        parent=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull Note model) {


        holder.tvTitle.setText(model.getTitle());
        holder.tvDescription.setText(model.getDescription());
        holder.tvTimeStamp.setText(model.getTimeStamp());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=LayoutInflater.from(parent)
                        .inflate(R.layout.new_note_design,null);


                TextInputEditText etTitle=view.findViewById(R.id.etTitle);
                TextInputEditText etDescription=view.findViewById(R.id.etDescription);
                TextView timeStamp=view.findViewById(R.id.timeStamp);

                etTitle.setText(model.getTitle());
                etDescription.setText(model.getDescription());

                SimpleDateFormat formattor= new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss" );
                Date date=new Date();
                timeStamp.setText(formattor.format(date));
                AlertDialog.Builder updateNote=new AlertDialog.Builder(parent)
                        .setTitle("Update Note")
                        .setView(view)
                        .setPositiveButton("update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String Title=etTitle.getText().toString().trim();
                                String Description=etDescription.getText().toString().trim();
                                String Time=timeStamp.getText().toString().trim();

                                HashMap<String,Object> data=new HashMap<>();
                                data.put("Title",Title);
                                data.put("Description",Description);
                                data.put("timeStamp",timeStamp.getText().toString());

                                getRef(position)
                                        .updateChildren(data)
                                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void unused) {
                                              Toast.makeText(parent, "Updated", Toast.LENGTH_SHORT).show();
                                          }
                                      })
                                      .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(parent, e.getMessage(), Toast.LENGTH_SHORT).show();
                                          }
                                      });
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                updateNote.create();
                updateNote.show();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder delete = new AlertDialog.Builder(parent)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int adapterPosition = holder.getAdapterPosition();
                                if (adapterPosition != RecyclerView.NO_POSITION) {
                                    getSnapshots().getSnapshot(adapterPosition)
                                            .getRef()
                                            .removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(parent, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(parent, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(parent, "Invalid position", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Your cancel logic here
                            }
                        });
                delete.create().show();
            }
        });

//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder delete=new AlertDialog.Builder(parent)
//                        .setTitle("Confirmation")
//                                .setMessage("Do yoy really want to delete?")
//                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                getRef(position)
//                                                        .removeValue()
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void unused) {
//                                                                Toast.makeText(parent, "Deleted Successfully", Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        })
//                                                        .addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                                Toast.makeText(parent, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        });
//                                            }
//                                        })
//                                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//
//                                                    }
//                                                });
//                                delete.create();
//                                delete.show();
//
//
//            }
//        });
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent,false);
        return new NotesViewHolder(v);
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDescription,tvTimeStamp;
        ImageView btnEdit,btnDelete;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvTimeStamp=itemView.findViewById(R.id.tvTimeStamp);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            btnDelete=itemView.findViewById(R.id.btnDelete);
        }


    }
}
