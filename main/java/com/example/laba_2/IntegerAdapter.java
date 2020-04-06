package com.example.laba_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class IntegerAdapter extends ArrayAdapter<Info> {
    private LayoutInflater inflater;
    private int layout;
    private List<Info> data;
    public IntegerAdapter(Context context, int resource, List<Info> data) {
        super(context, resource, data);
        this.data = data;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);
        ImageView flagView = (ImageView) view.findViewById(R.id.flag);
        TextView nameView = (TextView) view.findViewById(R.id.name);
        nameView.setText(data.get(position).getNm());
        if(data.get(position).getBt() == null)
        {
            new MyAsyn(flagView).execute(position);
        }
        else
        {
            flagView.setImageBitmap(data.get(position).getBt());
        }
        return view;
    }
    class MyAsyn extends AsyncTask<Integer, Void, Bitmap>
    {
        ImageView image;
        Integer pos;
        public MyAsyn(ImageView image){
            this.image = image;
        }
        @Override
        protected Bitmap doInBackground(Integer... poss) {
            Bitmap tmp = null;
            pos = poss[0];
            String str = "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/images/tech/" + data.get(pos).getGf();
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
            data.get(pos).setBt(tmp);
            return tmp;
        }
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }
}
