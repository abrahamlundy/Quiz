package id.go.codingschool.quiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mTransfer:
              //  Intent intentTransfer = new Intent(this, TransferActivity.class);
                //startActivity(intentTransfer);
                return true;
            case R.id.mTopUp:
                //Intent intentTopUp = new Intent(this, TopUpActivity.class);
                //startActivity(intentTopUp);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class AmbilData extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strUrl) {

            Log.v("yw","mulai ambil data");
            String hasil="";
            InputStream inStream=null;
            int len=500;  //ini adalah besaran untuk buffernya

            try{
                URL url = new URL(strUrl[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //timeout
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.setRequestMethod("GET");
                conn.connect();

                int response = conn.getResponseCode();
                inStream = conn.getInputStream();

                //konversi stream ke string
                Reader r = null;
                r = new InputStreamReader(inStream,"UTF-8");
                char[] buffer = new char[len];
                r.read(buffer);
                hasil= new String(buffer);

            }catch(MalformedURLException e){
                e.printStackTrace();

            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(inStream!= null){
                    try{
                        inStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            return hasil;
        }

        protected void onPostExecute(String result){
            //tvHasil.setText(result);
            isi=result;
            try {
                jsonObj= new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                lat = jsonObj.getString("latitude");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                lon = jsonObj.getString("longitude");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tvHasil.setText(lat+"and"+lon);
        }
    }
}
