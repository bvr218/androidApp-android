package com.example.fututel;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;


public class misTrabajos1 extends AppCompatActivity {
    private String nombres [] = new String[0];
    private String CC [] = new String[0];
    private String fecha [] = new String[0];
    private String comentarios [] = new String[0];
    private String idagendador [] = new String[0];
    private String zonas [] = new String[0];
    private String DatosM[] = new String[0];



    private String Url="http://agenda.fututel.com/yotas/";
    private ListView lv1;
    private TextView datosclientes;
    private Button Bsalir;
    private Button generar;
    private String iduser;
    private String nameuser;
    private Spinner spinner;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_trabajos1);

        iduser = getIntent().getStringExtra("idt");
        nameuser = getIntent().getStringExtra("nombreT");
        Bsalir = (Button) findViewById(R.id.botondesalida2);
        generar = (Button) findViewById(R.id.reporte);
        lv1 = (ListView) findViewById(R.id.tv1);
        datosclientes = (TextView) findViewById(R.id.datoscliente);
        spinner = (Spinner) findViewById(R.id.spin);
        String [] opciones = {"instalaciones","traslados","migraciones","retiros","soportes","reinstalacion","reubicacion","plan dual"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(misTrabajos1.this, android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(adapter);



        Bsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                misTrabajos1.this.finish();

            }
        });
        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email();

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int itemSeleccionado, long l){
                conectar();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

    }

    public void conectar() {

        datosclientes.setText("");

        String Url1 = Url + "verejecuciones.php?idtecnico=" + iduser+"&tipo="+spinner.getSelectedItem().toString().trim();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Url1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                nombres = new String[response.length()];
                CC = new String[response.length()];
                fecha = new String[response.length()];
                comentarios = new String[response.length()];
                idagendador = new String[response.length()];
                DatosM = new String[response.length()];
                zonas = new String[response.length()];
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        nombres[i] = jsonObject.getString("nombrecliente");
                        CC[i] = jsonObject.getString("cedula");
                        fecha[i] = jsonObject.getString("fecha_instalacion");
                        comentarios[i] = jsonObject.getString("comentarios");
                        idagendador[i] = jsonObject.getString("idagendador");
                        zonas[i] = jsonObject.getString("zona");
                        DatosM[i] = "NOMBRE DE CLIENTE:  " + nombres[i] + "\n" + "FECHA DE REALIZACION:  " + fecha[i] + "\n" + "ZONA:  " + zonas[i];


                    } catch (JSONException e) {

                    }
                }

                ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(misTrabajos1.this, R.layout.list_item_agendas, DatosM);
                lv1.setAdapter(adapter4);


                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        datosclientes.setText("nombre: " + nombres[i] + "\n" + "fecha de la instalacion instalacion: " + fecha[i] + "\n" + "CC: " + CC[i] + "\n" + "comentarios de la instalacion: " + comentarios[i]);

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(misTrabajos1.this, "no se obtuvo una respuesta", Toast.LENGTH_SHORT).show();
                lv1.setAdapter(null);


            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }
    public void email(){
        String Url2 = "http://internet2.fututel.com/html/agenda/enviarinforme.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url2+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(misTrabajos1.this, "se genero el informe, revise su correo",Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(misTrabajos1.this, "no se pudo generar"+error,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("idtecnico",iduser);
                return parametros;

            }
        };
        requestQueue= Volley.newRequestQueue(misTrabajos1.this);
        requestQueue.add(stringRequest);
    }
}