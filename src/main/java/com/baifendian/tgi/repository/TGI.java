package com.baifendian.tgi.repository;

import org.roaringbitmap.RoaringBitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by crazyy on 16/9/30.
 */
public class TGI implements Serializable {

    private int id;
    private String name;
    private String expression;
    private int bitmapsize;
    private RoaringBitmap bitmap;
    private String tgi;
    private Date createtime;
    private Date updatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getBitmapsize() {
        return bitmapsize;
    }

    public void setBitmapsize(int bitmapsize) {
        this.bitmapsize = bitmapsize;
    }

    public RoaringBitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(RoaringBitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTgi() {
        return tgi;
    }

    public void setTgi(String tgi) {
        this.tgi = tgi;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
