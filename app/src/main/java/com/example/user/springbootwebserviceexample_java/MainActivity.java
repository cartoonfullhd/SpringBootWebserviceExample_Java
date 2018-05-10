package com.example.user.springbootwebserviceexample_java;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    EditText editIdBook;
    EditText editNameBook;
    EditText editAuthorBook;
    TextView resultResponse;
    Button addBtn, deleteBtn, updateBtn;
    String txtName, txtAuthor, txtId;

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
                url = new URL(getResources().getString(R.string.ip_address));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
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
//            List<String> listBookId = new ArrayList<>();
//            List<String> listBookName = new ArrayList<>();
//            List<String> listBookAuthor = new ArrayList<>();
            try
            {
                JSONArray bookArray = new JSONArray(result);

//                for(int i=0; i< bookArray.length(); i++)
//                {
//                    JSONObject bookObj = bookArray.getJSONObject(i);
//                    listBookId.add(bookObj.getString("id"));
//                    listBookName.add(bookObj.getString("name"));
//                    listBookAuthor.add(bookObj.getString("author"));
//                }

                Book[] book = new Book[bookArray.length()];
                for(int i=0; i< bookArray.length(); i++)
                {
                    JSONObject bookObj = bookArray.getJSONObject(i);
                    book[i] = new Book(Integer.parseInt(bookObj.getString("id")), bookObj.getString("name"), bookObj.getString("author"));
                }

                /*JSONObject cartoonBookObj = bookArray.getJSONObject(0);
                JSONObject historyBookObj = bookArray.getJSONObject(1);
                JSONObject novalBookObj = bookArray.getJSONObject(2);


                Book cartoon = new Book(Integer.parseInt(cartoonBookObj.getString("id")), cartoonBookObj.getString("name"), cartoonBookObj.getString("author"));
                Book history = new Book(Integer.parseInt(historyBookObj.getString("id")), historyBookObj.getString("name"), historyBookObj.getString("author"));
                Book noval = new Book(Integer.parseInt(novalBookObj.getString("id")), novalBookObj.getString("name"), novalBookObj.getString("author"));

                */
                TextView bookId1 = findViewById(R.id.bookId1);
                TextView bookId2 = findViewById(R.id.bookId2);
                TextView bookId3 = findViewById(R.id.bookId3);
                TextView bookName1 = findViewById(R.id.bookName1);
                TextView bookName2 = findViewById(R.id.bookName2);
                TextView bookName3 = findViewById(R.id.bookName3);
                TextView bookAuthor1 = findViewById(R.id.bookAuthor1);
                TextView bookAuthor2 = findViewById(R.id.bookAuthor2);
                TextView bookAuthor3 = findViewById(R.id.bookAuthor3);
                TextView testCartoonBook = findViewById(R.id.testCartoonBook);

//                bookId1.setText(listBookId.get(0).toString());
//                bookId2.setText(listBookId.get(1).toString());
//                bookId3.setText(listBookId.get(2).toString());
//                bookName1.setText(listBookName.get(0).toString());
//                bookName2.setText(listBookName.get(1).toString());
//                bookName3.setText(listBookName.get(2).toString());
//                bookAuthor1.setText(listBookAuthor.get(0).toString());
//                bookAuthor2.setText(listBookAuthor.get(1).toString());
//                bookAuthor3.setText(listBookAuthor.get(2).toString());

                /*bookId1.setText(String.valueOf(cartoon.getId()));
                bookId2.setText(String.valueOf(history.getId()));
                bookId3.setText(String.valueOf(noval.getId()));
                bookName1.setText(cartoon.getName());
                bookName2.setText(history.getName());
                bookName3.setText(noval.getName());
                bookAuthor1.setText(cartoon.getAuthor());
                bookAuthor2.setText(history.getAuthor());
                bookAuthor3.setText(noval.getAuthor());
                testCartoonBook.setText(cartoon.toString());*/

                bookId1.setText(String.valueOf(book[0].getId()));
                bookId2.setText(String.valueOf(book[1].getId()));
                bookId3.setText(String.valueOf(book[2].getId()));
                bookName1.setText(book[0].getName());
                bookName2.setText(book[1].getName());
                bookName3.setText(book[2].getName());
                bookAuthor1.setText(book[0].getAuthor());
                bookAuthor2.setText(book[1].getAuthor());
                bookAuthor3.setText(book[2].getAuthor());
                testCartoonBook.setText(book[0].toString());

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
                url = new URL(getResources().getString(R.string.ip_address_add));

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
            //int    postDataLength = postData.length;
            try
            {
                url = new URL(getResources().getString(R.string.ip_address_delete)+"?"+urlParameters);

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
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }
}
