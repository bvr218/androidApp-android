package com.example.fututel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PagInicio extends AppCompatActivity {
    private TextView tv1;
    private Button buscar;
    private Button misagendas;
    private Button mistrabajos;
    private Button cambiarcontraseña;
    public String nameU;
    public String idU;
    public String contrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_inicio);
        tv1 = (TextView) findViewById(R.id.bienvenido);
        buscar = (Button) findViewById(R.id.botonveragendas);
        misagendas = (Button) findViewById(R.id.botonmisagendas);
        mistrabajos = (Button) findViewById(R.id.botonmisinstalaciones);
        cambiarcontraseña = (Button) findViewById(R.id.button);
        nameU = getIntent().getStringExtra("nombret");
        idU = getIntent().getStringExtra("idt");
        contrasena = getIntent().getStringExtra("contrasena");
        tv1.setText("bienvenido " + nameU);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VerAgendas();
            }
        });
        misagendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MisAgendas();
            }
        });
        mistrabajos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VerInstalaciones();
            }
        });
        cambiarcontraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CambiarContraseña();
            }
        });

    }
    public void MisAgendas(){
        Intent intent = new Intent(this, misAgendas.class);
        intent.putExtra("idt",idU);
        intent.putExtra("nombreT",nameU);
        startActivity(intent);
    }
    public void VerAgendas(){
        Intent intent = new Intent(this, activity2.class);
        intent.putExtra("idt",idU);
        startActivity(intent);
    }
    public void VerInstalaciones(){
        Intent intent = new Intent(this, misTrabajos1.class);
        intent.putExtra("idt",idU);
        intent.putExtra("nombreT",nameU);
        startActivity(intent);
    }
    public void CambiarContraseña(){
        Intent intent = new Intent(this, config.class);
        intent.putExtra("idt",idU);
        intent.putExtra("contrasena",contrasena);
        startActivity(intent);
    }
}