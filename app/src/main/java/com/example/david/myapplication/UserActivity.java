package com.example.david.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by david on 08/03/2018.
 */

public class UserActivity extends AppCompatActivity {
    private Button btn_commencer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);



        btn_commencer = (Button) findViewById(R.id.button_commencer);
        btn_commencer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                EditText user= (EditText)findViewById (R.id.userName);
                Intent i = new Intent(UserActivity.this, MainActivity.class);
                String userValeur = user.getText().toString();
                i.putExtra("user", userValeur);
                startActivity(i);
            }
        });
    }
}
