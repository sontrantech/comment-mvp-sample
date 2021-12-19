package sontran.geocomply.homeassignment.comment;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonFileUtils {

    private final Context appContext;
    private final Gson gson = new GsonBuilder().create();

    public JsonFileUtils(@NonNull Context appContext) {
        this.appContext = appContext;
    }

    public String dataFromJsonFile(@NonNull String fileName) {
        String json;
        try {
            InputStream is = appContext.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            // remove extra white spaces
            JsonElement el = JsonParser.parseString(json);
            return gson.toJson(el);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
