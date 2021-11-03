package com.example.homeworkapplication;

import android.content.Context;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class User {
    private static final String USER_SUBDIRECTORY = "users";
    private String id;
    private String password;
    private String name;
    private String phone;
    private String address;
    private boolean accepted;

    public User(JSONObject jsonObject) {
        this(jsonObject.optString("id", ""),
                jsonObject.optString("password"),
                jsonObject.optString("name", ""),
                jsonObject.optString("phone", ""),
                jsonObject.optString("address", ""),
                jsonObject.optBoolean("accepted", false)
        );
    }

    public User(Bundle bundle) {
        this(bundle.getString("id", ""),
                bundle.getString("password", ""),
                bundle.getString("name", ""),
                bundle.getString("phone", ""),
                bundle.getString("address", ""),
                bundle.getBoolean("accepted", false)
        );
    }

    public User(String id, String password, String name, String phone, String address, Boolean accepted) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.accepted = accepted;
    }

    public static User getUser(Context context, String id) throws UserDoesNotExistsException {
        User user;
        if (doesIDExist(context, id)) {
            FileInputStream fis;
            try {
                File file = new File(getBaseDirectory(context), id + ".json");
                fis = new FileInputStream(file);
                byte[] data = new byte[fis.available()];
                fis.read(data);
                user = new User(new JSONObject(new String(data, StandardCharsets.UTF_8)));
            } catch (Exception e) {
                e.printStackTrace();
                user = null;
            }
        } else {
            throw new UserDoesNotExistsException();
        }

        return user;
    }

    public static boolean saveUser(Context context, User user) {
        boolean result;
        if (user.id.isEmpty() || user.password.isEmpty() || user.password.length() < 4) {
            return false;
        }

        try {
            JSONObject obj = user.toJSON();
            File baseDir = getBaseDirectory(context);
            baseDir.mkdirs();
            File file = new File(baseDir, user.getId() + ".json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(obj.toString().getBytes(StandardCharsets.UTF_8));
            fos.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static File getBaseDirectory(Context context) {
        return new File(context.getFilesDir(), USER_SUBDIRECTORY);
    }

    public static boolean doesIDExist(Context context, String id) {
        return new File(getBaseDirectory(context), id + ".json").exists();
    }

    public static boolean authenticate(Context context, String id, String password) throws UserDoesNotExistsException {
        User user = getUser(context, id);
        return password.equals(user.password);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("password", password);
        jsonObject.put("name", name);
        jsonObject.put("phone", phone);
        jsonObject.put("address", address);
        jsonObject.put("accepted", accepted);
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /* Exceptions */
    public static class UserDoesNotExistsException extends Exception {
    }
}