package com.example.listviewcustom;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonAsyncTask extends AsyncTask<String, Void, String> {

    private JsonAsyncTaskListener listener;

    public JsonAsyncTask(JsonAsyncTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];
        String result = "";

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Kết nối và nhận dữ liệu JSON
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            result = stringBuilder.toString();

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("tui");

            listener.onJsonAsyncTaskComplete(jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface JsonAsyncTaskListener {
        void onJsonAsyncTaskComplete(String result);
    }
}