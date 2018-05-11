package com.example.user.springbootwebserviceexample_java;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editIdBook;
    EditText editNameBook;
    EditText editAuthorBook;
    TextView resultResponse;
    Button addBtn, deleteBtn, updateBtn;
    String txtName, txtAuthor, txtId;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editIdBook = findViewById(R.id.inputIdBook);
        editNameBook = findViewById(R.id.inputNameBook);
        editAuthorBook = findViewById(R.id.inputAuthorBook);
        resultResponse = findViewById(R.id.resultResponse);
        addBtn = findViewById(R.id.addButton);
        deleteBtn = findViewById(R.id.deleteButton);
        updateBtn = findViewById(R.id.updateButton);
        recyclerView = findViewById(R.id.recycler_view);
        new getBook().execute();

        addBtn.setOnClickListener(new View.OnClickListener()
        {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    txtName = editNameBook.getText().toString();
                    txtAuthor = editAuthorBook.getText().toString();
                    if(TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtAuthor))
                    {
                        editNameBook.setError("Input name of book");
                        editAuthorBook.setError("Input author of book");
                    }
                    else{
                            String[] bookData = {txtName, txtAuthor};
                            new addBook().execute(bookData);
                    }
                }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                txtName = editNameBook.getText().toString();
                if(TextUtils.isEmpty(txtName))
                {
                    editNameBook.setError("Input name of book");
                }
                else{
                    new deleteBook().execute(txtName);
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                txtId = editIdBook.getText().toString();
                txtAuthor = editAuthorBook.getText().toString();
                txtName = editNameBook.getText().toString();
                if(TextUtils.isEmpty(txtId) || TextUtils.isEmpty(txtAuthor) || TextUtils.isEmpty(txtName))
                {
                    editIdBook.setError("Input id of book");
                    editNameBook.setError("Input name of book");
                    editAuthorBook.setError("Input author of book");
                }
                else{
                    String[] bookData = {txtId, txtName, txtAuthor};
                    new updateBook().execute(bookData);
                }
            }
        });
    }

    private class getBook extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected String doInBackground(Void ... params)
        {
            URL url;
            HttpURLConnection urlConnection = null;
            String line;
            String JSONResult = "";
            try
            {
                url = new URL(getResources().getString(R.string.ip_address)+"allBook");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                //InputStream decode urlConnection to Byte format
                InputStream Byte = urlConnection.getInputStream();
                //InputStreamReader decode Byte format to Char format
                InputStreamReader Char = new InputStreamReader(Byte);
                BufferedReader reader = new BufferedReader(Char);
                StringBuilder builder = new StringBuilder();

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
            try
            {
                JSONArray bookArray = new JSONArray(result);

                Book[] book = new Book[bookArray.length()];
                for(int i=0; i< bookArray.length(); i++)
                {
                    JSONObject bookObj = bookArray.getJSONObject(i);
                    book[i] = new Book(Integer.parseInt(bookObj.getString("id")), bookObj.getString("name"), bookObj.getString("author"));
                    bookList.add(book[i]);
                }
                bookAdapter = new BookAdapter(bookList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(bookAdapter);
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }

    private class addBook extends AsyncTask<String[], Void, String>
    {
        public int code;
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected String doInBackground(String[]... params)
        {
            URL url;
            HttpURLConnection urlConnection = null;
            String line;
            String JSONResult = "";
            String[] parameter = params[0];
            String urlParameters  = "name=" + parameter[0] + "&author=" + parameter[1];
            byte[] postData       = urlParameters.getBytes(Charset.forName("UTF-8"));
            int    postDataLength = postData.length;
            try
            {
                url = new URL(getResources().getString(R.string.ip_address)+"addBook");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                urlConnection.getOutputStream().write(postData);
                code = urlConnection.getResponseCode();

                //InputStream decode urlConnection to Byte format
                InputStream Byte = urlConnection.getInputStream();
                //InputStreamReader decode Byte format to Char format
                InputStreamReader Char = new InputStreamReader(Byte);
                BufferedReader reader = new BufferedReader(Char);
                StringBuilder builder = new StringBuilder();

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
            resultResponse.setText("Number of row was updated: " + result + "\n" + "Response code:  " + code);
            recyclerView.setAdapter(null);
            bookAdapter.notifyDataSetChanged();
            bookList.clear();
            new getBook().execute();
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }

    private class deleteBook extends AsyncTask<String, Void, String>
    {
        public int code;
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected String doInBackground(String... params)
        {
            URL url;
            HttpURLConnection urlConnection = null;
            String line;
            String name = params[0];
            String JSONResult = "";
            String urlParameters  = "name=" + name;
            try
            {
                url = new URL(getResources().getString(R.string.ip_address) + "deleteBook" +"?"+urlParameters);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("charset", "utf-8");

                code = urlConnection.getResponseCode();

                //InputStream decode urlConnection to Byte format
                InputStream Byte = urlConnection.getInputStream();
                //InputStreamReader decode Byte format to Char format
                InputStreamReader Char = new InputStreamReader(Byte);
                BufferedReader reader = new BufferedReader(Char);
                StringBuilder builder = new StringBuilder();

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
            resultResponse.setText("Number of row was updated: " + result + "\n" + "Response code:  " + code);
            recyclerView.setAdapter(null);
            bookAdapter.notifyDataSetChanged();
            bookList.clear();
            new getBook().execute();
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }

    private class updateBook extends AsyncTask<String[], Void, String>
    {
        public int code;
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected String doInBackground(String[]... params)
        {
            URL url;
            HttpURLConnection urlConnection = null;
            String line;
            String JSONResult = "";
            String[] parameter = params[0];
            JSONObject bookJson = new JSONObject();

            try
            {
                bookJson.put("id" , parameter[0].toString());
                bookJson.put("name", parameter[1].toString());
                bookJson.put("author", parameter[2].toString());
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try
            {
                url = new URL(getResources().getString(R.string.ip_address) + "updateBook");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(bookJson.toString());
                wr.flush();
                wr.close();

                code = urlConnection.getResponseCode();

                //InputStream decode urlConnection to Byte format
                InputStream Byte = urlConnection.getInputStream();
                //InputStreamReader decode Byte format to Char format
                InputStreamReader Char = new InputStreamReader(Byte);
                BufferedReader reader = new BufferedReader(Char);
                StringBuilder builder = new StringBuilder();

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
            resultResponse.setText("Number of row was updated: " + result + "\n" + "Response code:  " + code);
            recyclerView.setAdapter(null);
            bookAdapter.notifyDataSetChanged();
            bookList.clear();
            new getBook().execute();
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }

    }
}
