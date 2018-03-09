package com.example.david.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 08/03/2018.
 */

public class ListUsersActivity extends AppCompatActivity {
    private Button btn_commencer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listusers);


        btn_commencer = (Button) findViewById(R.id.buttonAjouter);
        btn_commencer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListUsersActivity.this, UserActivity.class);

                startActivity(i);
            }
        });
        List<String> listUsers = lireDansBaseDeDonnee();
        //Création de l'adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listUsers);

//Récupération du ListView présent dans notre IHM
        ListView list = (ListView) findViewById(R.id.listView);

//On passe nos données au composant ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectUser = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ListUsersActivity.this, MainActivity.class);
                intent.putExtra("user", selectUser);
                startActivity(intent);
            }
        });
    }

    private List<String> lireDansBaseDeDonnee() {
        String path = getApplicationContext().getFilesDir().getPath() + "/" + "db.csv";
        List<String> listUsers = new ArrayList<String>();
        BufferedReader br = null;
        try {
            InputStream is = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(is));
            ;
            String line = "";
            String cvsSplitBy = ",";
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                if (!listUsers.contains(data[0]))
                    listUsers.add(data[0]);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return listUsers;

    }

}
