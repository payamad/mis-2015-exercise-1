package mmbuw.com.brokenproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import mmbuw.com.brokenproject.R;

public class AnotherBrokenActivity extends Activity {

    private EditText editUrl;
    private TextView txtFetchedResult;
    private WebView webFetchedResult;
    private Switch switchView;
    private String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_broken);

        Intent intent = getIntent();
        String message = intent.getStringExtra(BrokenActivity.EXTRA_MESSAGE);
        //What happens here? What is this? It feels like this is wrong.
        //Maybe the weird programmer who wrote this forgot to do something?

        //The programmer forgot to define EXTRA_MESSAGE as a public static string in broken activity


        editUrl = (EditText) findViewById(R.id.editUrl);
        webFetchedResult = (WebView)findViewById(R.id.webFetchedResult);
        txtFetchedResult = (TextView)findViewById(R.id.txtFetchedResult);
        switchView = (Switch) findViewById(R.id.btnSwitchView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.another_broken, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // use onSaveInstanceState and onRestoreInstanceState to keep result of webview and textview after screen rotation
    @Override
    protected void onRestoreInstanceState(Bundle extra) {
        super.onRestoreInstanceState(extra);
        result = extra.getString("result");
        setResultView(result);
    }

    @Override
    protected void onSaveInstanceState(Bundle extra) {
        result = txtFetchedResult.getText().toString();
        super.onSaveInstanceState(extra);
        extra.putString("result", result);
    }


    //use onPause and onResult to keep state of activity after minimizing and maximizing of activity
    @Override
    public void onPause() {
        super.onPause();
        result = txtFetchedResult.getText().toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        switchView(switchView);
        setResultView(result);
    }

    // this method is used to set result of fetch request
    private void setResultView(String text) {
        String mimeType = "text/html";
        String encoding = "utf-8";
        //Write request url in html format
        webFetchedResult.loadData(text, mimeType, encoding);
        //Write request url in tex format
        txtFetchedResult.setText(text);
    }

    // this method is used to show a toast if fetched request encountered by an exception
    private void showError(String errorText){
        Toast.makeText(getApplicationContext(), (String)errorText,
                Toast.LENGTH_LONG).show();
    }

    // this method is used to switch from web view to text view
    public void switchView(View view){
        boolean switchOn = ((Switch) view).isChecked();
        if(switchOn){
            webFetchedResult.setVisibility(View.INVISIBLE);
            txtFetchedResult.setVisibility(View.VISIBLE);
        }
        else{
            webFetchedResult.setVisibility(View.VISIBLE);
            txtFetchedResult.setVisibility(View.INVISIBLE);
        }
    }

    public void fetchHTML(View view) throws IOException {

        //According to the exercise, you will need to add a button and an EditText first.
        //Then, use this function to call your http requests
        //Following hints:
        //Android might not enjoy if you do Networking on the main thread, but who am I to judge?
        //An app might not be allowed to access the internet without the right (*hinthint*) permissions
        //Below, you find a staring point for your HTTP Requests - this code is in the wrong place and lacks the allowance to do what it wants
        //It will crash if you just un-comment it.

        /*
        Beginning of helper code for HTTP Request.

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(new HttpGet("http://lmgtfy.com/?q=android+ansync+task HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(new HttpGet("http://lmgtfy.com/?q=android+ansync+task"));
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            response.getEntity().writeTo(outStream);
            String responseAsString = outStream.toString();
             System.out.println("Response string: "+responseAsString);
        }else {
            //Well, this didn't work.
            response.getEntity().getContent().close();
            throw new IOException(status.getReasonPhrase());
        }"));
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            response.getEntity().writeTo(outStream);
            String responseAsString = outStream.toString();
             System.out.println("Response string: "+responseAsString);
        }else {
            //Well, this didn't work.
            response.getEntity().getContent().close();
            throw new IOException(status.getReasonPhrase());
        }

          End of helper code!

                  */

        String url = editUrl.getText().toString();
        //Fetch requested url with use of AsyncTask
        //AsyncTask is used here in order to do a task in background
        new NetworkAccess().execute(url);


    }

    // We NetworkAccess to do the network job in the background
    private class NetworkAccess extends AsyncTask<String, Void, String> {

        private String responseAsString;
        private String errorText = "";
        @Override
        protected String doInBackground(String... args) {
            errorText = "";
            HttpClient client = new DefaultHttpClient();
            try {
                HttpResponse response = client.execute(new HttpGet(args[0]));
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    response.getEntity().writeTo(outStream);
                    responseAsString = outStream.toString();
                    System.out.println("Response string: "+responseAsString);


                }else {
                    //Well, this didn't work.
                    response.getEntity().getContent().close();
                    errorText = status.getReasonPhrase();
                    throw new IOException(status.getReasonPhrase());
                }

            } catch (MalformedURLException e) {
                errorText = "URL: is a malformed URL " + e.toString();
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                errorText = "URL: Unsupported Encoding Exception " + e.toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                errorText = "URL: Client Protocol Exception " + e.toString();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                errorText = "URL: Socket Timeout Exception " + e.toString();
            } catch (ConnectTimeoutException e) {
                e.printStackTrace();
                errorText = "URL: Connect Timeout Exception " + e.toString();
            } catch (IOException e) {
                errorText = "URL: IO Exception " + e.toString();
                e.printStackTrace();
            } catch (Exception e){
                errorText = e.toString();//"Unknown exception";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if(errorText.isEmpty())
                setResultView(responseAsString); // we fetched url successfully, so we show the result
            else
                showError(errorText); // we faced an error, so we will show a toast

        }



    }
}
