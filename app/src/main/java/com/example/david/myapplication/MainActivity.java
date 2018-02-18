package com.example.david.myapplication;
/**
 * Created by david on 05/02/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kami);

        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
        try {
            InputStream is = getClass().getResourceAsStream("/puzzles/CompletionData.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("Stage");
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    final Element element2 = (Element) node;
                    Button button = new Button(this);
                    button.setText(element2.getAttribute("name"));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MainActivity.this, LevelActivity.class);
                            i.putExtra("level", "Stage" + element2.getAttribute("name"));
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
}
