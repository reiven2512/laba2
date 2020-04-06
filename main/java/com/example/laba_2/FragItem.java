package com.example.laba_2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.URL;

public class FragItem extends Fragment {
    Info dt;
    public static FragItem newInstance(Info dt) {
        FragItem fragment = new FragItem();
        fragment.dt = dt;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag, container, false);

        ImageView flagView = view.findViewById(R.id.Image);
        TextView textView = view.findViewById(R.id.Title);
        TextView textView_2 = view.findViewById(R.id.Description);
        textView.setText(dt.getNm());
        textView_2.setText(dt.getHt());
        if(dt.getBt() == null)
        {
            new MyAsync(flagView).execute(dt);
        }
        else
        {
            flagView.setImageBitmap(dt.getBt());
        }

        return view;
    }
    class MyAsync extends AsyncTask<Info, Void, Bitmap>
    {
        ImageView image;
        Info dt;
        public MyAsync(ImageView image){
            this.image = image;
        }
        @Override
        protected Bitmap doInBackground(Info... dts) {
            Bitmap tmp = null;
            dt = dts[0];
            String str = "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/images/tech/" + dt.getGf();
            try {
                URL newurl = new URL(str);
                tmp = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            }catch (
                    IOException e) {
                e.printStackTrace();
            }
            if(tmp == null)
            {
                tmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_element);
            }
            dt.setBt(tmp);
            return tmp;
        }
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }
}
