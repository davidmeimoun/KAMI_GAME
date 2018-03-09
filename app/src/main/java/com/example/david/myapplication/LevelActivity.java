package com.example.david.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by david on 05/02/2018.
 */

public class LevelActivity extends AppCompatActivity {
    String level;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String level = b.getString("level");
        String user = b.getString("user");
        this.level = level;
        this.user = user;
        ajoutDesInfosDansBoutons(level);


    }

    private void ajoutDesInfosDansBoutons(final String level) {
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
        buttonContainer.removeAllViews();
        try {

            InputStream is = getClass().getResourceAsStream("/puzzles/" + level + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("Puzzle");
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    final Element element2 = (Element) node;
                    Button button = new Button(this);
                    String tab[] = new String[2];

                    if (element2.getAttribute("name").contains("_")) {
                        tab = element2.getAttribute("name").split("_");
                        boolean isNiveauValide = lireDansBaseDeDonnee(user, level, element2.getAttribute("name"));
                        if (isNiveauValide)
                            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dimmedgrey));
                        button.setText(tab[1]);// + " - " + isNiveauValide );

                    } else {
                        tab[0] = element2.getAttribute("name");
                        boolean isNiveauValide = lireDansBaseDeDonnee(user, level, element2.getAttribute("name"));
                        if (isNiveauValide)
                            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dimmedgrey));
                        button.setText(tab[0]);//+ " - " + isNiveauValide);
                    }

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(LevelActivity.this, GrilleActivity.class);
                            i.putExtra("stage", element2.getAttribute("name"));
                            i.putExtra("level", level);
                            i.putExtra("user", user);
                            startActivity(i);
                        }
                    });
                    buttonContainer.addView(button);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private boolean lireDansBaseDeDonnee(String utilisateur, String level, String stage) {
        String path = getApplicationContext().getFilesDir().getPath() + "/" + "db.csv";

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
                if (data[0].equals(utilisateur))
                    if (data[1].equals(stage))
                        if (data[2].equals(level))
                            return true;

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
        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        ajoutDesInfosDansBoutons(this.level);
    }
}
