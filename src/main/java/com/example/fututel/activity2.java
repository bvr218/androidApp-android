package com.example.fututel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
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

public class activity2 extends AppCompatActivity {
    private String nombres [] = new String[0];
    private String CC [] = new String[0];
    private String fecha [] = new String[0];
    private String ubicacion [] = new String[0];
    private String telefono [] = new String[0];
    private String comentarios [] = new String[0];
    private String idagenda [] = new String[0];
    private String DatosM[] = new String[0];

    private String [] cliente = new String[4];

    private int agendaactual=-1;

    private String Url="http://agenda.fututel.com/yotas/";
    private String [] namezonas;
    private String [] idzonas;
    private String [] datosz;
    private ListView lv1;
    private Switch solo;
    private TextView datosclientes;
    private Spinner spinner;
    private Spinner spinner1;
    private String fecha_asignacion;
    private ImageButton Bactualizar;
    private Button asignarB;
    private String iduser;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        solo = (Switch) findViewById(R.id.switch1);
        solo.setText("solo creadas este mes");
        iduser = getIntent().getStringExtra("idt");
        Bactualizar = (ImageButton) findViewById(R.id.Bactualizar);
        fecha_asignacion = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        asignarB = (Button) findViewById(R.id.botondesalida);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner2);

        Bzonas();

        String [] opciones = {"instalaciones","traslados","migraciones","retiros","soportes","reinstalacion","reubicacion","plan dual"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(adapter);
        lv1 = (ListView) findViewById(R.id.tv1);
        datosclientes = (TextView) findViewById(R.id.datoscliente);

        asignarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                asignar();
            }
        });
        Bactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                conectar();
            }
        });

    }
    public void asignar(){

        String Url1 = Url+"contarmisagendas.php?idtecnico="+iduser;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Url1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                int valor[]={0,0};
                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        valor[i] = Integer.parseInt(jsonObject.getString("COUNT(*)"));

                    } catch (Exception e) {

                    }
                }
                int respuesta=valor[0]+valor[1];
                if(respuesta<3){
                    if(agendaactual>=0){
                        if(spinner.getSelectedItem().toString().trim().equals("instalaciones")){
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url+"actualizaragenda.php", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity2.this, response,Toast.LENGTH_SHORT).show();
                                    email();


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity2.this, "no se pudo asignar"+error,Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> parametros=new HashMap<String,String>();
                                    parametros.put("id",idagenda[agendaactual]);
                                    parametros.put("idtecnico",iduser);
                                    parametros.put("estate","ASIGNADO");
                                    parametros.put("fecha_asignacion",fecha_asignacion);
                                    return parametros;

                                }
                            };
                            requestQueue= Volley.newRequestQueue(activity2.this);
                            requestQueue.add(stringRequest);
                        }
                        else{
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url+"actualizaragendasvarias.php", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(activity2.this, "asignado a "+iduser+" correctamente",Toast.LENGTH_SHORT).show();
                                    email();


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity2.this, "no se pudo asignar"+error,Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> parametros=new HashMap<String,String>();
                                    parametros.put("id",idagenda[agendaactual]);
                                    parametros.put("idtecnico",iduser);
                                    parametros.put("estate","ASIGNADO");
                                    parametros.put("fecha_asignacion",fecha_asignacion);
                                    return parametros;

                                }
                            };
                            requestQueue= Volley.newRequestQueue(activity2.this);
                            requestQueue.add(stringRequest);
                        }


                    }
                    else{
                        Toast.makeText(activity2.this, "seleccione una agenda ",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(activity2.this, "cupo maximo para asignacion agotado",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(activity2.this, "error con el servidor",Toast.LENGTH_SHORT).show();

            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);



    }


    public void conectar(){
        agendaactual=0;
        datosclientes.setText("");
        String tipo = idzonas[spinner1.getSelectedItemPosition()];
        int tipon = Integer.parseInt(tipo)-1;
        if(spinner.getSelectedItem().toString().trim().equals("instalaciones")){
            String Url1;
            solo.setText("solo creadas este mes");
            if(solo.isChecked()){
                if(tipo.equals("0")){
                    Url1 = Url+"buscarsinzona.php?solo=true";
                }
                else{
                    Url1 = Url+"buscar.php?zona="+tipon+"&solo=true";
                }
            }else{
                if(tipo.equals("0")){
                    Url1 = Url+"buscarsinzona.php?solo=false";
                }
                else{
                    Url1 = Url+"buscar.php?zona="+tipon+"&solo=false";
                }
            }


            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Url1, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    nombres = new String[response.length()];
                    CC = new String[response.length()];
                    fecha = new String[response.length()];
                    ubicacion = new String[response.length()];
                    telefono = new String[response.length()];
                    comentarios = new String[response.length()];
                    idagenda = new String[response.length()];
                    DatosM = new String[response.length()];
                    JSONObject jsonObject = null;
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            jsonObject = response.getJSONObject(i);
                            nombres[i] = jsonObject.getString("cliente");
                            CC[i] = jsonObject.getString("cedula");
                            fecha[i] = jsonObject.getString("fecha_instalacion");
                            ubicacion[i] = jsonObject.getString("direccion");
                            telefono[i] = jsonObject.getString("movil");
                            comentarios[i] = jsonObject.getString("notas");
                            idagenda[i] = jsonObject.getString("id");
                            DatosM[i]="NOMBRE:  "+nombres[i]+"\n--------------------------------------\n"+"FECHA DE INSTALACION:  "+fecha[i]+"\n--------------------------------------\n"+"UBICACION:  "+ubicacion[i];


                        } catch (JSONException e) {

                        }
                    }

                    ArrayAdapter <String> adapter4 = new ArrayAdapter<String>(activity2.this,R.layout.list_item_agendas,DatosM);
                    lv1.setAdapter(adapter4);




                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            datosclientes.setText("nombre: "+ nombres[i]+"\n--------------------------------------\n"+"fecha para instalacion: "+ fecha[i]+"\n--------------------------------------\n"+"ubicacion: " +
                                    ubicacion[i]+"\n--------------------------------------\n"+"CC: "+CC[i]+"\n--------------------------------------\n"+"celular: "+telefono[i]+"\n--------------------------------------\n"+"comentarios del agendador: "+comentarios[i]);
                            agendaactual=i;
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity2.this, "no se obtuvo una respuesta",Toast.LENGTH_LONG).show();
                    lv1.setAdapter(null);


                }
            }
            );
            requestQueue=Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
        }
        else{
            String Url1;
            solo.setText("solo hoy");
            if(solo.isChecked()){
                if(tipo.equals("0")){
                    Url1= Url+"buscaragendassinzona.php?tipodeagenda="+spinner.getSelectedItem().toString().trim()+"&solo=true";
                }
                else{
                    Url1 = Url+"buscarAgendas.php?zona="+tipo+"&tipodeagenda="+spinner.getSelectedItem().toString().trim()+"&solo=true";
                }
            } else{
                if(tipo.equals("0")){
                    Url1= Url+"buscaragendassinzona.php?tipodeagenda="+spinner.getSelectedItem().toString().trim()+"&solo=false";
                }
                else{
                    Url1 = Url+"buscarAgendas.php?zona="+tipo+"&tipodeagenda="+spinner.getSelectedItem().toString().trim()+"&solo=false";
                }
            }



            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Url1, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    nombres = new String[response.length()];
                    CC = new String[response.length()];
                    fecha = new String[response.length()];
                    ubicacion = new String[response.length()];
                    telefono = new String[response.length()];
                    comentarios = new String[response.length()];
                    idagenda = new String[response.length()];
                    DatosM = new String[response.length()];
                    JSONObject jsonObject = null;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            nombres[i] = jsonObject.getString("nombrecliente");
                            CC[i] = jsonObject.getString("cedula");
                            fecha[i] = jsonObject.getString("fecha_instalacion");
                            ubicacion[i] = jsonObject.getString("direccion");
                            telefono[i] = jsonObject.getString("movil");
                            comentarios[i] = jsonObject.getString("comentarios");
                            idagenda[i] = jsonObject.getString("id");
                            DatosM[i]="NOMBRE:  "+nombres[i]+"\n--------------------------------------\n"+"FECHA DE CREACION:  "+fecha[i]+"\n--------------------------------------\n"+"UBICACION:  "+ubicacion[i];


                        } catch (JSONException e) {
                            Toast.makeText(activity2.this, "error: "+e,Toast.LENGTH_SHORT).show();
                        }
                    }

                    ArrayAdapter <String> adapter4 = new ArrayAdapter<String>(activity2.this,R.layout.list_item_agendas,DatosM);
                    lv1.setAdapter(adapter4);



                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            datosclientes.setText("nombre: "+ nombres[i]+"\n--------------------------------------\n"+"fecha de creacion: "+ fecha[i]+"\n--------------------------------------\n"+"ubicacion: " +
                                    ubicacion[i]+"\n--------------------------------------\n"+"CC: "+CC[i]+"\n--------------------------------------\n"+"celular: "+telefono[i]+"\n--------------------------------------\n"+"comentarios del agendador: "+comentarios[i]);
                            agendaactual=i;
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity2.this, "no se obtuvo una respuesta",Toast.LENGTH_SHORT).show();
                    lv1.setAdapter(null);


                }
            }
            );
            requestQueue=Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
        }



    }

    public void Bzonas(){
        String Url1 = Url+"buscarzonas.php";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Url1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                namezonas = new String[response.length()];
                idzonas = new String[response.length()];
                datosz = new String[response.length()+1];
                datosz[0]="todas las zonas";
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        namezonas[i] = jsonObject.getString("zona");
                        idzonas[i] = jsonObject.getString("id");
                        datosz[i+1]=idzonas[i]+". "+namezonas[i];


                    } catch (JSONException e) {

                    }
                }
                ArrayAdapter <String> adapter = new ArrayAdapter<String>(activity2.this, R.layout.spinerzonas,datosz);
                spinner1.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity2.this, "no se obtuvo una respuesta",Toast.LENGTH_SHORT).show();
                lv1.setAdapter(null);


            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }

    public void email(){
        String Url2 = "http://internet2.fututel.com/html/agenda/asignacion.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url2+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                conectar();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity2.this, "no se pudo ejecutar"+error,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("nombre",nombres[agendaactual]);
                parametros.put("idagenda",idagenda[agendaactual]);
                parametros.put("tipo",spinner.getSelectedItem().toString().trim());
                parametros.put("ubicacion",ubicacion[agendaactual]);
                parametros.put("zona",namezonas[agendaactual]);
                parametros.put("telefono",telefono[agendaactual]);
                parametros.put("idtecnico",iduser);
                parametros.put("cedula",CC[agendaactual]);
                parametros.put("fecha_instalacion",fecha[agendaactual]);
                parametros.put("comentarios",comentarios[agendaactual]);
                return parametros;

            }
        };
        requestQueue= Volley.newRequestQueue(activity2.this);
        requestQueue.add(stringRequest);
    }

}