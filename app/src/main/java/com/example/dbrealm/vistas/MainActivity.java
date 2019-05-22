package com.example.dbrealm.vistas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dbrealm.R;
import com.example.dbrealm.adaptador.PersonaAdapter;
import com.example.dbrealm.modelos.PersonaModel;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<PersonaModel>> {


    public Realm realm;

    private FloatingActionButton fabNueva;

    private RealmResults<PersonaModel> listaPersonas;
    private RecyclerView recyclerViewPersona;
    private PersonaAdapter personaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        findViewID();

    }

    private void findViewID() {

        fabNueva = findViewById(R.id.fabNueva);

        listaPersonas = realm.where(PersonaModel.class).findAll();
        listaPersonas.addChangeListener(this);
        recyclerViewPersona = findViewById(R.id.recyclerPersona);
        recyclerViewPersona.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        personaAdapter = new PersonaAdapter(getApplicationContext(), listaPersonas);
        recyclerViewPersona.setAdapter(personaAdapter);

        listenOnClick();

    }

    private void listenOnClick(){
        fabNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertNuevaPersona();
            }
        });

        personaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonaModel personaModel = listaPersonas.get(recyclerViewPersona.getChildAdapterPosition(view));
                alertEditarPersona(personaModel);
            }
        });


        personaAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Seguro que deseas eliminar?")
                        .setTitle("Eliminar")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarPersona(view);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog Notificacion = builder.create();
                Notificacion.show();
                return false;
            }
        });

    }

    public void alertNuevaPersona(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.nueva_persona, null);
        builder.setView(view);

        final EditText edtNombre = view.findViewById(R.id.edtNombre);
        final EditText edtApelido = view.findViewById(R.id.edtApellido);

        builder.setMessage("Agregar Persona");
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombre = edtNombre.getText().toString().trim();
                String apellido = edtApelido.getText().toString();
                if (nombre.length()>0){
                    guardarPersona(nombre, apellido);
                }else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_mensaje_nombre_vacio), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    public void alertEditarPersona(final PersonaModel personaModel){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.nueva_persona, null);
        builder.setView(view);

        final EditText edtNombre = view.findViewById(R.id.edtNombre);
        final EditText edtApellido = view.findViewById(R.id.edtApellido);

        edtNombre.setText(personaModel.getNombre());
        edtApellido.setText(personaModel.getApellido());

        edtNombre.setSelection(edtNombre.getText().length());

        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombrePersona = edtNombre.getText().toString().trim();
                String apellidoPersona = edtApellido.getText().toString();
                if (nombrePersona.length()>0){
                    realm.beginTransaction();
                    personaModel.setNombre(nombrePersona);
                    personaModel.setApellido(apellidoPersona);
                    realm.commitTransaction();
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_mensaje_nombre_vacio), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void guardarPersona(String nombre, String apellido){
        realm.beginTransaction();
        PersonaModel personaModel = new PersonaModel(nombre, apellido);
        realm.copyToRealm(personaModel);
        realm.commitTransaction();
    }

    public void eliminarPersona(View view){
        PersonaModel personaModel = listaPersonas.get(recyclerViewPersona.getChildAdapterPosition(view));
        realm.beginTransaction();
        assert personaModel != null;
        personaModel.deleteFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void onChange(@NonNull RealmResults<PersonaModel> personaModels) {
        personaAdapter.notifyDataSetChanged();
    }



}

