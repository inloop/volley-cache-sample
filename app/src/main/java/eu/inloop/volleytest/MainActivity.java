package eu.inloop.volleytest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import eu.inloop.volleytest.api.ApiService;
import eu.inloop.volleytest.api.CustomCacheStringRequest;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(android.R.id.text1);

        ApiService apiService = ApiService.getInstance(this);

        CustomCacheStringRequest stringRequest = new CustomCacheStringRequest("http://www.dsl.sk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                textView.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley-test", error.getLocalizedMessage());
            }
        });
        apiService.enqueueRequest(stringRequest);

    }

}
