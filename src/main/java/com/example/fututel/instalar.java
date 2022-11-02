package com.example.fututel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class instalar extends AppCompatActivity {
    private Button instalar;
    private EditText valorT;
    private EditText comentarios;
    private String Url="http://agenda.fututel.com/yotas/";


    private String tipo;
    private String fecha_instalacion;
    private String zona;
    private String idtecnico;
    private String nombrecliente;
    private String ubicacion;
    private String id="0";
    private String idagendador;
    private String idagenda;
    private String nombretecnico;
    private String valor;
    private String cedula;

    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instalar);
        valorT = (EditText) findViewById(R.id.valor);
        comentarios = (EditText) findViewById(R.id.comentarios);
        instalar = (Button) findViewById(R.id.botoninstalar);
        fecha_instalacion = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        idtecnico = getIntent().getStringExtra("idt");
        tipo = getIntent().getStringExtra("tipo");
        valor = getIntent().getStringExtra("valor");
        ubicacion = getIntent().getStringExtra("direccion");
        zona = getIntent().getStringExtra("zona");
        nombretecnico = getIntent().getStringExtra("nombreT");
        nombrecliente = getIntent().getStringExtra("nombrecliente");
        idagendador = getIntent().getStringExtra("idagendador");
        idagenda = getIntent().getStringExtra("idagenda");
        cedula = getIntent().getStringExtra("cedula");

        instalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                instala();
            }
        });

    }

    public void instala(){
        StringRequest stringRequest;
        if(tipo.equals("instalaciones")){
            stringRequest = new StringRequest(Request.Method.POST, Url + "actualizaragenda.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    ejecucion();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(instalar.this, "no se pudo instalar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("id", idagenda);
                    parametros.put("idtecnico", idtecnico);
                    parametros.put("estate", "INSTALADO");
                    parametros.put("fecha_asignacion", "1000-01-01");
                    return parametros;

                }
            };
        }
        else{
            stringRequest = new StringRequest(Request.Method.POST, Url + "actualizaragendasvarias.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    ejecucion();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(instalar.this, "no se pudo ejecutar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("id", idagenda);
                    parametros.put("idtecnico", idtecnico);
                    parametros.put("estate", "INSTALADO");
                    return parametros;

                }
            };

        }
        requestQueue= Volley.newRequestQueue(instalar.this);
        requestQueue.add(stringRequest);
    }
    public void ejecucion(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url+"nuevaejecucion.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(instalar.this, "ejecutado por "+idtecnico+" correctamente"+response,Toast.LENGTH_SHORT).show();
                email();
                instalar.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(instalar.this, "no se pudo ejecutar"+error,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("idtecnico",idtecnico);
                parametros.put("idagenda",idagenda);
                parametros.put("tipo",tipo);
                parametros.put("zona",zona);
                parametros.put("nombrecliente",nombrecliente);
                parametros.put("idagendador",idagendador);
                parametros.put("ubicacion",ubicacion);
                parametros.put("cedula",cedula);
                parametros.put("valor",valor);
                parametros.put("valortecnico",valorT.getText().toString());
                parametros.put("comentarios",comentarios.getText().toString().trim());
                return parametros;

            }
        };
        requestQueue= Volley.newRequestQueue(instalar.this);
        requestQueue.add(stringRequest);

    }
    public void email(){
        String Url2 = "http://internet2.fututel.com/html/agenda/enviaremail2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url2+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                instalar.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(instalar.this, "no se pudo ejecutar"+error,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("nombre",nombretecnico);
                parametros.put("idagenda",idagenda);
                parametros.put("tipo",tipo);
                parametros.put("ubicacion",ubicacion);
                parametros.put("zona",zona);
                parametros.put("idtecnico",idtecnico);
                parametros.put("nombrecliente",nombrecliente);
                parametros.put("cedula",cedula);
                parametros.put("fecha_instalacion",fecha_instalacion);
                parametros.put("comentarios",comentarios.getText().toString().trim());
                return parametros;

            }
        };
        requestQueue= Volley.newRequestQueue(instalar.this);
        requestQueue.add(stringRequest);
    }
}