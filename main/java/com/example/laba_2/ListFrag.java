package com.example.laba_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import java.util.List;

public class ListFrag extends ListFragment {
    private IntegerAdapter intAdapter;
    List<Info> data;
    public ListFrag(List<Info> data){
        this.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listview, container, false);
    }

    public static String tag() {
        return ListFrag.class.getSimpleName();
    }

    public void onListItemClick(ListView l, View v, int position, long id){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Frag frag = Frag.newInstance(data, position);
        ft.replace(R.id.fragment_container, frag);
        ft.addToBackStack(null);
        ft.commit();
    }
}
