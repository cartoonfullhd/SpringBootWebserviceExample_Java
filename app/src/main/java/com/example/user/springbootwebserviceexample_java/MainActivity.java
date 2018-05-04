package com.example.user.springbootwebserviceexample_java;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new getBook().execute();
    }

    private class getBook extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void ... params)
        {
            URL url;
            HttpURLConnection urlConnection = null;
            String line;
            String JSONResult = "";
            try
            {
                url = new URL("http://192.168.3.108:8080/books");
                urlConnection = (HttpURLConnection) url.openConnection();
                //InputStream decode urlConnection to Byte format
                InputStream Byte = urlConnection.getInputStream();
                //InputStreamReader decode Byte format to Char format
                InputStreamReader Char = new InputStreamReader(Byte);
                BufferedReader reader = new BufferedReader(Char);
                StringBuilder builder = new StringBuilder();
//                while ((reader.s()) != null)
//                {
//                    StringBuffer dataResponse = new StringBuffer();
//                    dataResponse.append(reader.readLine());
//                    JSONResult = dataResponse.toString();
//                }
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                reader.close();
                JSONResult = builder.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(urlConnection != null)
                {
                    urlConnection.disconnect();
                }
            }
            return JSONResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            List<String> listBookId = new ArrayList<>();
            List<String> listBookName = new ArrayList<>();
            List<String> listBookAuthor = new ArrayList<>();
            try
            {
                JSONArray bookArray = new JSONArray(result);
                for(int i=0; i< bookArray.length(); i++)
                {
                    JSONObject bookObj = bookArray.getJSONObject(i);
                    listBookId.add(bookObj.getString("id"));
                    listBookName.add(bookObj.getString("name"));
                    listBookAuthor.add(bookObj.getString("author"));
                }

                TextView bookId1 = findViewById(R.id.bookId1);
                TextView bookId2 = findViewById(R.id.bookId2);
                TextView bookId3 = findViewById(R.id.bookId3);
                TextView bookName1 = findViewById(R.id.bookName1);
                TextView bookName2 = findViewById(R.id.bookName2);
                TextView bookName3 = findViewById(R.id.bookName3);
                TextView bookAuthor1 = findViewById(R.id.bookAuthor1);
                TextView bookAuthor2 = findViewById(R.id.bookAuthor2);
                TextView bookAuthor3 = findViewById(R.id.bookAuthor3);

                bookId1.setText(listBookId.get(0).toString());
                bookId2.setText(listBookId.get(1).toString());
                bookId3.setText(listBookId.get(2).toString());
                bookName1.setText(listBookName.get(0).toString());
                bookName2.setText(listBookName.get(1).toString());
                bookName3.setText(listBookName.get(2).toString());
                bookAuthor1.setText(listBookAuthor.get(0).toString());
                bookAuthor2.setText(listBookAuthor.get(1).toString());
                bookAuthor3.setText(listBookAuthor.get(2).toString());
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }

}
