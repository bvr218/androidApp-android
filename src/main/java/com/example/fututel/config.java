package com.example.fututel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class config extends AppCompatActivity {
    private Button baceptar, bcancelar;
    private String Url="http://agenda.fututel.com/yotas/";
    private  String iduser;
    private  String contrasena;
    private EditText newpass, pass, confirpass;


    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);
        baceptar = (Button) findViewById(R.id.buttonaceptar);
        bcancelar = (Button) findViewById(R.id.buttoncancelar);
        iduser = getIntent().getStringExtra("idt");
        contrasena = getIntent().getStringExtra("contrasena");

        newpass = (EditText) findViewById(R.id.newpass);
        pass = (EditText) findViewById(R.id.pass);
        confirpass = (EditText) findViewById(R.id.confirmpass);
    }
    public void aceptar(View view){

        if(contrasena.equals(getMD5(pass.getText().toString().trim()))){
            if(newpass.getText().toString().trim().equals(confirpass.getText().toString().trim())){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Url+"cambiarcontraseña.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(config.this, "contraseña modificada",Toast.LENGTH_SHORT).show();
                        config.this.finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(config.this, "la contraseña no se pudo modificar",Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parametros=new HashMap<String,String>();
                        parametros.put("idtecnico",iduser);
                        parametros.put("contrasena",getMD5(newpass.getText().toString().trim()));
                        return parametros;

                    }
                };
                requestQueue= Volley.newRequestQueue(config.this);
                requestQueue.add(stringRequest);
            }
            else{
                Toast.makeText(config.this, "las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(config.this, "contraseña incorrecta",Toast.LENGTH_SHORT).show();
        }


    }
    public void cancelar(View view){
        config.this.finish();
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