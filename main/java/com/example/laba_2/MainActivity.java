package com.example.laba_2;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainActivity extends AppCompatActivity {
    public static List<Info> data;
    ListFrag lf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        data = new ArrayList<Info>();
        download();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntegerAdapter intAdapter = new IntegerAdapter(this, R.layout.list_item, data);
        lf = new ListFrag(data);
        lf.setListAdapter(intAdapter);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, lf);
        ft.commit();
    }
    public void download()
    {
        Thread thread = new Thread() {
            public void run() {
                try {
                    URL url = new URL("https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/data/techs.ruleset.json");
                    ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                    FileOutputStream fos = openFileOutput("file.json", Context.MODE_PRIVATE);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    rbc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileReader fr = new FileReader("/data/user/0/com.example.laba_2/files/file.json");
                    JSONParser parser = new JSONParser();
                    JSONArray ja = (JSONArray) parser.parse(fr);
                    for (Object o : ja) {
                        JSONObject technology = (JSONObject) o;
                        if (technology.get("name") == null) continue;
                        String nm = (String) technology.get("name");
                        String gf = (String) technology.get("graphic");
                        if (technology.get("helptext") == null) {
                            data.add(new Info(nm, gf));
                        }
                        else {
                            String hp = (String) technology.get("helptext");
                            data.add(new Info(nm, gf, hp));
                        }
                    }
                } catch (ParseException d) {
                    d.printStackTrace();
                } catch (IOException d) {
                    d.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}