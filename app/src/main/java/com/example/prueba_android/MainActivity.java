package com.example.prueba_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Equipo> ListEquipo = new ArrayList<Equipo>();
    ArrayAdapter<Equipo> arrayAdapterEquipo;

    EditText pais, equipo, anio;
    Equipo equipoSelected;
    //Button btnIngresar,btnEliminar,btnActualizar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listaV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        equipo = findViewById(R.id.editTextName);
        pais = findViewById(R.id.editTextCountry);
        anio = findViewById(R.id.editTextYear);

        listaV = findViewById(R.id.listaView);

        inicializarFirebase();
        listarDatos();

        listaV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                equipoSelected = (Equipo) parent.getItemAtPosition(position);
                equipo.setText(equipoSelected.getEquipo());
                pais.setText(equipoSelected.getPais());
                anio.setText(equipoSelected.getAnio());
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatos() {
        databaseReference.child("Equipo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListEquipo.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Equipo e = objSnapshot.getValue(Equipo.class);
                    ListEquipo.add(e);

                    arrayAdapterEquipo = new ArrayAdapter<Equipo>(MainActivity.this, android.R.layout.simple_list_item_1, ListEquipo);
                    listaV.setAdapter(arrayAdapterEquipo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addData(View view){

        String equipoA = equipo.getText().toString().trim();
        String paisA = pais.getText().toString().trim();
        String anioA = anio.getText().toString().trim();

        if (equipoA.equals("") || paisA.equals("") || anioA.equals("") ){
            Toast.makeText(this, "Por Favor Ingrese los Datos", Toast.LENGTH_LONG).show();
        }else{
            Equipo e = new Equipo();
            e.setPais(paisA);
            e.setEquipo(equipoA);
            e.setAnio(anioA);
            e.setUID(UUID.randomUUID().toString());
            databaseReference.child("Equipo").child(e.getUID()).setValue(e);
            Toast.makeText(this, "Datos Agregados !", Toast.LENGTH_LONG).show();
            vaciarCampos();
        }
    }

    public void updateData(View view){
        String equipoA = equipo.getText().toString().trim();
        String paisA = pais.getText().toString().trim();
        String anioA = anio.getText().toString().trim();

        if (equipoA.equals("") || paisA.equals("") || anioA.equals("") ){
            Toast.makeText(this, "Por Favor Ingrese los Datos", Toast.LENGTH_LONG).show();
        }else{
            Equipo e = new Equipo();

            e.setUID(equipoSelected.getUID());
            e.setPais(pais.getText().toString().trim());
            e.setEquipo(equipo.getText().toString().trim());
            e.setAnio(anio.getText().toString().trim());
            databaseReference.child("Equipo").child(e.getUID()).setValue(e);
            Toast.makeText(this, "Datos Acualizados !", Toast.LENGTH_LONG).show();
            vaciarCampos();
        }
    }
    public void deleteData(View view){
        String equipoA = equipo.getText().toString().trim();
        String paisA = pais.getText().toString().trim();
        String anioA = anio.getText().toString().trim();

        if (equipoA.equals("") || paisA.equals("") || anioA.equals("") ){
            Toast.makeText(this, "Por Favor Ingrese los Datos", Toast.LENGTH_LONG).show();
        }else{
            Equipo e = new Equipo();

            e.setUID(equipoSelected.getUID());
            databaseReference.child("Equipo").child(e.getUID()).removeValue();
            Toast.makeText(this, "Eliminado !", Toast.LENGTH_LONG).show();
            vaciarCampos();
        }
    }

    private void vaciarCampos() {
        equipo.setText("");
        pais.setText("");
        anio.setText("");
    }

}