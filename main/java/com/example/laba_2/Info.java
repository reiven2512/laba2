package com.example.laba_2;

import android.graphics.Bitmap;

public class Info {
    private String nm;
    private String gf;
    private String ht;
    private Bitmap bt;
    public Info(String nm, String gf)
    {
        this.nm = nm;
        this.gf = gf;
        this.bt = null;
    }
    public Info(String nm, String gf, String ht)
    {
        this.nm = nm;
        this.gf = gf;
        this.ht = ht;
        this.bt = null;
    }
    public String getNm()
    {
        return nm;
    }
    public String getGf()
    {
        return gf;
    }
    public String getHt()
    {
        return ht;
    }
    public Bitmap getBt()
    {
        return bt;
    }
    public void setBt(Bitmap bt)
    {
        this.bt = bt;
    }

}
