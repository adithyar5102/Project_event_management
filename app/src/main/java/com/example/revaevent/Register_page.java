package com.example.revaevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Register_page extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText transaction;
    EditText phoneno;
    TextView url;
    TextView onsuccess;
    Button register;
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference ref_std;
    FirebaseAuth mAuth;
    String uid;
    String event;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        event=getIntent().getStringExtra("event_name");
        String url1=getIntent().getStringExtra("event_url");
        name=findViewById(R.id.register_page_name);
        email=findViewById(R.id.register_page_semester);
        transaction=findViewById(R.id.register_page_transaction);
        phoneno=findViewById(R.id.register_page_branch);
        url=findViewById(R.id.transaction_url);
        onsuccess=findViewById(R.id.register_page_onsuccess);
        register=findViewById(R.id.register_page_register);
        url.setText("Pay To: "+url1);
        Log.d("Reva_Event_workflow", "register page");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty() || phoneno.getText().toString().isEmpty() || email.getText().toString().isEmpty() || transaction.getText().toString().isEmpty() || phoneno.getText().toString().length()!=10){
                    Toast.makeText(Register_page.this, "Please Enter All Details Properly", Toast.LENGTH_SHORT).show();
                    return;
                }
                database=FirebaseDatabase.getInstance();
                database.getReference("Event_details").child(event).child("limit").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("Reva_Event_workflow", "Check limit");
                            String lim=String.valueOf(task.getResult().getValue());
                            lim=lim.substring(1);
                            int ilim=Integer.parseInt(lim);
                            if(ilim==0){
                                Toast.makeText(Register_page.this, "Sorry Registration is closed", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Register_page.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }else{
                                HashMap temp=new HashMap();
                                temp.put("limit","n"+(ilim-1));
                                database.getReference("Event_details").child(event).updateChildren(temp);
                            }
                        }
                    }
                });
                ref=database.getReference("registration");
                ref_std=database.getReference("student_registered");
                Log.d("Reva_Event_workflow", "Set data in cloud");
                ref.child(event).child(uid).setValue(new Registered_student_details(name.getText().toString(),phoneno.getText().toString(), email.getText().toString(),transaction.getText().toString()));
                ref_std.child(uid).child(event).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(Register_page.this,MainActivity.class);
                            Toast.makeText(Register_page.this, "Registered", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                        else{
                            Intent intent=new Intent(Register_page.this,MainActivity.class);
                            Toast.makeText(Register_page.this, "Unable to Registered", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}