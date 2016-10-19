package com.baifendian.calculate.client;

import org.roaringbitmap.RoaringBitmap;

/**
 * Created by crazyy on 16/9/30.
 */
public class Configuration {

    private RoaringBitmap bitmap;
    private int allNum;

    public Configuration(RoaringBitmap bitmap, int allNum) {
        this.bitmap = bitmap;
        this.allNum = allNum;
    }

    public RoaringBitmap getBitmap() {
        return this.bitmap;
    }

    public int getAllNum() {
        return this.allNum;
    }
}
