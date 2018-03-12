package com.example.hp.myflight;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring EditText
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText e5;
    //Send button
    private Button buttonSend;
    CheckBox cid1,cid2,cid3,cid4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button b5=(Button)findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Initializing the views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        e5=(EditText)findViewById(R.id.e5);
        cid1 = (CheckBox) findViewById(R.id.cid1);
        cid2 = (CheckBox) findViewById(R.id.cid2);
        cid3 = (CheckBox) findViewById(R.id.cid3);
        cid4 = (CheckBox) findViewById(R.id.cid4);
        cid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cid2.isChecked()&&cid1.isChecked())
                    cid2.setChecked(false);
            }
        });
        cid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cid2.isChecked()&&cid1.isChecked())
                    cid1.setChecked(false);
            }
        });
        buttonSend = (Button) findViewById(R.id.buttonSend);

        //Adding click listener
        buttonSend.setOnClickListener(this);
    }


    private void sendEmail() {
        //Getting content for email&database
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String age=e5.getText().toString().trim();
        String data=age+" ";
        if(cid1.isChecked())data+="1";
        else data+="0";
        if(cid3.isChecked())data+="1";
        else data+="0";
        if(cid4.isChecked())data+="1";
        else data+="0";
        final String data2=data;
        SendMail sm = new SendMail(this,email,password,name,data2){
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Showing a success message
                if(flag==1) {
                    Toast.makeText(MainActivity.this, "Confirmation Mail Sent", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putInt("otp",x );
                    bundle.putString("name", name);
                    bundle.putString("pass", password);
                    bundle.putString("data2",data2);
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Sign Up Error!",Toast.LENGTH_LONG).show();
                }
            }

        };
        sm.execute();

    }

    @Override
    public void onClick(View v) {

        try
        {
            EditText e1=(EditText)findViewById(R.id.editTextName);
            EditText e2=(EditText)findViewById(R.id.editTextPassword);
            EditText e3=(EditText)findViewById(R.id.editTextEmail);
            EditText e5=(EditText)findViewById(R.id.e5);
            int age=Integer.parseInt(e5.getText().toString());
            if(e1.getText().toString().equals("")||e2.getText().toString().equals("")||e3.getText().toString().equals(""))
            {
                Toast.makeText(MainActivity.this,"Enter all credentials!",Toast.LENGTH_LONG).show();
            }
            else if(cid2.isChecked()==false&&cid1.isChecked()==false)
            {
                Toast.makeText(MainActivity.this,"Enter your gender!",Toast.LENGTH_LONG).show();
            }
            else if(age<6||age>99)
            {
                Toast.makeText(MainActivity.this,"Age limit to be between 6-99 years!",Toast.LENGTH_LONG).show();
            }
            else if(cid3.isChecked()||cid4.isChecked())
            {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
                alertDialog2.setTitle("Confirm Submission");
                alertDialog2.setMessage("You have selected Pregnant/Disabled. Kindly ensure the details are correct.");

                alertDialog2.setPositiveButton("I confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                go();
                            }
                        });

                alertDialog2.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog2.show();
            }
            else
                go();
            //sendEmail();
        }
        catch(Exception ex)
        {
            System.out.print("Background Task Error!");//Debugging assistance
        }
    }
    void go()
    {

        CheckData ch = new CheckData(this,editTextEmail.getText().toString().trim()){
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Showing a success message
                if(x==1) {
                    Toast.makeText(getApplicationContext(), "Already registered. Try logging in!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    sendEmail();
                }
            }

        };
        ch.execute();
    }
}