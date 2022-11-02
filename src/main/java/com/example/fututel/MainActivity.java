package com.example.fututel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.lang.ref.ReferenceQueue;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button bingresar, bconfig;
    private EditText user, pass;
    private String name="";
    private String Url= "http://agenda.fututel.com/yotas/";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bingresar = (Button) findViewById(R.id.buttoningresar);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.Password);

        bingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validar(Url);
            }
        });

    }

    private void validar(String Url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url+"ingresar2.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){

                    buscarU(Url+"buscarU.php?nick="+user.getText().toString());


                }else{

                    Toast.makeText(MainActivity.this, "usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("username",user.getText().toString());
                parametros.put("contrasena",getMD5(pass.getText().toString()));
                return parametros;
            }
        };
        requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buscarU(String Url2){


        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Url2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("nombre");
                        String id = jsonObject.getString("id");
                        String contrasena = jsonObject.getString("password");
                        Intent intent = new Intent(MainActivity.this, PagInicio.class);
                        intent.putExtra("nombret",name);
                        intent.putExtra("idt",id);
                        intent.putExtra("contrasena",contrasena);
                        startActivity(intent);
                        MainActivity.this.finish();

                    } catch (JSONException e) {

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }
    public String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}