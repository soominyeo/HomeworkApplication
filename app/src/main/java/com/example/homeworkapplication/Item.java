package com.example.homeworkapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigInteger;

public class Item {
    private static final String ITEM_SUBDIRECTORY = "items";
    private Long id = null;
    private Bitmap image;
    private String name;

    public Item(Bitmap image, String name) {
        this.image = image;
        this.name = name;
    }

    public Item(Bitmap image, String name, long id) {
        this(image, name);
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Bitmap bytesToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        return baos.toByteArray();
    }

    public static void saveImage(Context context, long id, Bitmap image) {
        try {
            File basedir = getBaseDirectory(context);
            basedir.mkdirs();
            String encoded = Base64.encodeToString(BigInteger.valueOf(id).toByteArray(), Base64.NO_WRAP);
            File file = new File(basedir, encoded + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(Item.bitmapToBytes(image));
            fos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap readImage(Context context, long id) {
        Bitmap bitmap = null;
        try{
            String encoded = Base64.encodeToString(BigInteger.valueOf(id).toByteArray(), Base64.NO_WRAP);
            File file = new File(getBaseDirectory(context), encoded + ".png");
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            bitmap = Item.bytesToBitmap(bytes);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void deleteImage(Context context, long id) {
        try{
            String encoded = Base64.encodeToString(BigInteger.valueOf(id).toByteArray(), Base64.NO_WRAP);
            File file = new File(getBaseDirectory(context), encoded + ".png");
            if(file.exists()) {
                file.delete();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static File getBaseDirectory(Context context) {
        return new File(context.getFilesDir(), ITEM_SUBDIRECTORY);
    }
}
