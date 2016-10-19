package com.baifendian.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.roaringbitmap.RoaringBitmap;

public class BitmapHandler {

    public static RoaringBitmap byteStreamToBitmap(InputStream inputStream) {
        GZIPInputStream gzipIn = null;
        ObjectInputStream is = null;
        try {
            gzipIn = new GZIPInputStream(inputStream);
            is = new ObjectInputStream(gzipIn);
            RoaringBitmap bitmap = (RoaringBitmap) is.readObject();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                gzipIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static RoaringBitmap byteArrayToBitmap(byte[] bytearray) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytearray);
        GZIPInputStream gzipIn = null;
        ObjectInputStream is = null;
        try {
            gzipIn = new GZIPInputStream(bais);
            is = new ObjectInputStream(gzipIn);
            RoaringBitmap bitmap = (RoaringBitmap) is.readObject();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                gzipIn.close();
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] bitmapToByteArray(RoaringBitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        ObjectOutputStream dout = null;
        try {
            gzip = new GZIPOutputStream(baos);
            dout = new ObjectOutputStream(gzip);
            dout.writeObject(bitmap);
            gzip.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                dout.close();
                gzip.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static RoaringBitmap union(List<RoaringBitmap> bitmapList) {
        if (bitmapList == null || bitmapList.isEmpty()) {
            return new RoaringBitmap();
        }

        RoaringBitmap result = new RoaringBitmap();

        for (RoaringBitmap bitmap : bitmapList) {
            result = RoaringBitmap.or(bitmap, result);
        }
        return result;
    }

    public static RoaringBitmap join(List<RoaringBitmap> bitmapList) {

        if (bitmapList == null || bitmapList.isEmpty()) {
            return new RoaringBitmap();
        }

        RoaringBitmap result = bitmapList.get(0);

        for (int i = 1; i < bitmapList.size(); i++) {
            result = RoaringBitmap.and(result, bitmapList.get(i));
        }

        return result;
    }
}
