package com.example.fututel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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

public class misAgendas extends AppCompatActivity {

    private String nombres [] = new String[0];
    private String CC [] = new String[0];
    private String fecha [] = new String[0];
    private String fecha_asignacion [] = new String[0];
    private String ubicacion [] = new String[0];
    private String telefono [] = new String[0];
    private String comentarios [] = new String[0];
    private String valor [] = new String[0];
    private String idagenda [] = new String[0];
    private String idagendador [] = new String[0];
    private String zonas [] = new String[0];
    private String DatosM[] = new String[0];

    public int agendaactual=-1;

    private String Url="http://agenda.fututel.com/yotas/";
    private ListView lv1;
    private TextView datosclientes;
    private Button asignarB;
    private Button Bejecutar;
    private String iduser;
    private String nameU;
    private Spinner spinner;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_agendas);
        iduser = getIntent().getStringExtra("idt");
        nameU = getIntent().getStringExtra("nombreT");
        asignarB = (Button) findViewById(R.id.botonborrar);
        Bejecutar = (Button) findViewById(R.id.botondesalida);
        lv1 = (ListView) findViewById(R.id.tv1);
        datosclientes = (TextView) findViewById(R.id.datoscliente);
        spinner = (Spinner) findViewById(R.id.spinner);
        String [] opciones = {"instalaciones","traslados","migraciones","retiros","soportes","reinstalacion","reubicacion","plan dual"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(adapter);

        asignarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                borrar();


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

        Bejecutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instalar();

            }
        });


    }

    public void borrar(){
        if(agendaactual>=0){
            if(fecha_asignacion[agendaactual].equals(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()))){
                Toast.makeText(misAgendas.this, "el tiempo minimo de espera para borrar es de un dia",Toast.LENGTH_SHORT).show();

            }
            else{
                if(spinner.getSelectedItem().toString().trim().equals("instalaciones")){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Url+"actualizaragenda.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(misAgendas.this, response,Toast.LENGTH_SHORT).show();
                            agendaactual=0;
                            conectar();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(misAgendas.this, "no se pudo borrar"+error,Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parametros=new HashMap<String,String>();
                            parametros.put("id",idagenda[agendaactual]);
                            parametros.put("idtecnico","0");
                            parametros.put("estate","PENDIENTE");
                            parametros.put("fecha_asignacion","1000-01-01");
                            return parametros;

                        }
                    };
                    requestQueue= Volley.newRequestQueue(misAgendas.this);
                    requestQueue.add(stringRequest);
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Url+"actualizaragendasvarias.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(misAgendas.this, "borrado de "+iduser+" correctamente",Toast.LENGTH_SHORT).show();
                            agendaactual=-1;
                            conectar();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(misAgendas.this, "no se pudo borrar"+error,Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parametros=new HashMap<String,String>();
                            parametros.put("id",idagenda[agendaactual]);
                            parametros.put("idtecnico","0");
                            parametros.put("estate","PENDIENTE");
                            parametros.put("fecha_asignacion","1000-01-01");
                            return parametros;

                        }
                    };
                    requestQueue= Volley.newRequestQueue(misAgendas.this);
                    requestQueue.add(stringRequest);
                }
            }




        }
        else{
            Toast.makeText(misAgendas.this, "seleccione una agenda ",Toast.LENGTH_LONG).show();
        }




    }
    public void conectar() {
        agendaactual = -1;
        datosclientes.setText("");
        if (spinner.getSelectedItem().toString().trim().equals("instalaciones")) {
            String Url1 = Url + "buscarmisagendas.php?idtecnico=" + iduser;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Url1, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    nombres = new String[response.length()];
                    CC = new String[response.length()];
                    fecha = new String[response.length()];
                    fecha_asignacion = new String[response.length()];
                    ubicacion = new String[response.length()];
                    telefono = new String[response.length()];
                    comentarios = new String[response.length()];
                    valor = new String[response.length()];
                    idagenda = new String[response.length()];
                    idagendador = new String[response.length()];
                    DatosM = new String[response.length()];
                    zonas = new String[response.length()];
                    JSONObject jsonObject = null;
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            jsonObject = response.getJSONObject(i);
                            nombres[i] = jsonObject.getString("cliente");
                            CC[i] = jsonObject.getString("cedula");
                            fecha[i] = jsonObject.getString("fecha_instalacion");
                            fecha_asignacion[i] = jsonObject.getString("fecha_asignacion");
                            ubicacion[i] = jsonObject.getString("direccion");
                            telefono[i] = jsonObject.getString("movil");
                            comentarios[i] = jsonObject.getString("notas");
                            idagenda[i] = jsonObject.getString("id");
                            valor[i] = jsonObject.getString("valor");
                            idagendador[i] = jsonObject.getString("idvendedor");
                            zonas[i] = jsonObject.getString("zona");
                            DatosM[i] = "NOMBRE:  " + nombres[i] + "\n" + "FECHA PARA REALIZACION:  " + fecha[i] + "\n" + "UBICACION:  " + ubicacion[i];


                        } catch (JSONException e) {

                        }
                    }

                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(misAgendas.this, R.layout.list_item_agendas, DatosM);
                    lv1.setAdapter(adapter4);


                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            datosclientes.setText("nombre: " + nombres[i] + "\n" + "fecha para instalacion: " + fecha[i] + "\n" + "ubicacion: " +
                                    ubicacion[i] + "\n" + "CC: " + CC[i] + "\n" + "celular: " + telefono[i] + "\n" + "comentarios del agendador: " + comentarios[i]);
                            agendaactual = i;
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(misAgendas.this, "no se obtuvo una respuesta", Toast.LENGTH_SHORT).show();
                    lv1.setAdapter(null);


                }
            }
            );
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
        } else {
            String Url1 = Url + "buscarmisagendasvarias.php?idtecnico=" + iduser + "&tipodeagenda=" + spinner.getSelectedItem().toString().trim();

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Url1, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    nombres = new String[response.length()];
                    CC = new String[response.length()];
                    fecha = new String[response.length()];
                    fecha_asignacion = new String[response.length()];
                    ubicacion = new String[response.length()];
                    telefono = new String[response.length()];
                    comentarios = new String[response.length()];
                    idagenda = new String[response.length()];
                    valor = new String[response.length()];
                    idagendador = new String[response.length()];
                    zonas = new String[response.length()];
                    DatosM = new String[response.length()];
                    JSONObject jsonObject = null;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            nombres[i] = jsonObject.getString("nombrecliente");
                            CC[i] = jsonObject.getString("cedula");
                            fecha[i] = jsonObject.getString("fecha_instalacion");
                            fecha_asignacion[i] = jsonObject.getString("fecha_asignacion");
                            ubicacion[i] = jsonObject.getString("direccion");
                            telefono[i] = jsonObject.getString("movil");
                            valor[i] = jsonObject.getString("valor");
                            comentarios[i] = jsonObject.getString("comentarios");
                            idagenda[i] = jsonObject.getString("id");
                            idagendador[i] = jsonObject.getString("idagendador");
                            zonas[i] = jsonObject.getString("zona");
                            DatosM[i] = "NOMBRE:  " + nombres[i] + "\n" + "FECHA PARA REALIZACION:  " + fecha[i] + "\n" + "UBICACION:  " + ubicacion[i];


                        } catch (JSONException e) {
                            Toast.makeText(misAgendas.this, "no se obtuvieron resultados", Toast.LENGTH_SHORT).show();
                        }
                    }

                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(misAgendas.this, R.layout.list_item_agendas, DatosM);
                    lv1.setAdapter(adapter4);


                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            datosclientes.setText("nombre: " + nombres[i] + "\n" + "fecha para instalacion: " + fecha[i] + "\n" + "ubicacion: " +
                                    ubicacion[i] + "\n" + "CC: " + CC[i] + "\n" + "celular: " + telefono[i] + "\n" + "comentarios del agendador: " + comentarios[i]
                                    + "\n" + "precio: " + valor[i]);
                            agendaactual = i;
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(misAgendas.this, "no se obtuvo una respuesta ", Toast.LENGTH_SHORT).show();
                    lv1.setAdapter(null);

                }
            }
            );
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
        }
    }
    public void instalar(){

        if(agendaactual>=0){


            Intent intent = new Intent(misAgendas.this, instalar.class);
            intent.putExtra("idt",iduser);
            intent.putExtra("nombreT",nameU);
            intent.putExtra("tipo",spinner.getSelectedItem().toString().trim());
            intent.putExtra("zona",zonas[agendaactual]);
            intent.putExtra("direccion",ubicacion[agendaactual]);
            intent.putExtra("nombrecliente",nombres[agendaactual]);
            intent.putExtra("idagenda",idagenda[agendaactual]);
            intent.putExtra("idagendador",idagendador[agendaactual]);
            intent.putExtra("valor",valor[agendaactual]);
            intent.putExtra("cedula",CC[agendaactual]);

            startActivity(intent);
            misAgendas.this.finish();

        }
        else{
            Toast.makeText(misAgendas.this, "seleccione una agenda ",Toast.LENGTH_SHORT).show();
        }




    }


}