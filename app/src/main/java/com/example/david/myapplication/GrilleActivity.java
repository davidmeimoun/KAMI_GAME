package com.example.david.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by david on 07/02/2018.
 */

public class GrilleActivity extends AppCompatActivity {

    private GrilleView grille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String stage = b.getString("stage");
        String level = b.getString("level");
        String user = b.getString("user");
        try {
            InputStream is = getClass().getResourceAsStream("/puzzles/" + level + "/" + stage + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("PuzzleData");

            Node node = nList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element element2 = (Element) node;
                String width = element2.getAttribute("width");
                String height = element2.getAttribute("height");
                String colours = element2.getAttribute("colours");
                String numColours = element2.getAttribute("numColours");
                String gold = element2.getAttribute("gold");
                String silver = element2.getAttribute("silver");
                String bronze = element2.getAttribute("bronze");
                grille = new GrilleView(this, width, height, colours, numColours, gold, silver, bronze, stage, level, user);
                setContentView(grille);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
