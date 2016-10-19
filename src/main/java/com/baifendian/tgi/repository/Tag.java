package com.baifendian.tgi.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.roaringbitmap.RoaringBitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by crazyy on 16/9/28.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_NULL)
public class Tag implements Serializable {

    private String name;
    private RoaringBitmap bitmap;
    private long bitmapSize;
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoaringBitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(RoaringBitmap bitmap) {
        this.bitmap = bitmap;
    }

    public long getBitmapSize() {
        return bitmapSize;
    }

    public void setBitmapSize(long bitmapSize) {
        this.bitmapSize = bitmapSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
