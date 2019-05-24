package com.example.dbrealm.vistas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dbrealm.R;
import com.example.dbrealm.adaptador.NotitasAdapter;
import com.example.dbrealm.modelos.NotitasModel;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<NotitasModel>> {


    public Realm realm;

    private FloatingActionButton fabNueva;

    private RealmResults<NotitasModel> listaNotas;
    private RecyclerView recyclerViewNotas;
    private NotitasAdapter notitasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        findViewID();

    }

    private void findViewID() {

        fabNueva = findViewById(R.id.fabNueva);

        listaNotas = realm.where(NotitasModel.class).findAll();
        listaNotas.addChangeListener(this);
        recyclerViewNotas = findViewById(R.id.recyclerPersona);
        recyclerViewNotas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notitasAdapter = new NotitasAdapter(getApplicationContext(), listaNotas);
        recyclerViewNotas.setAdapter(notitasAdapter);

        listenOnClick();

    }

    private void listenOnClick(){
        fabNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertNuevaNota();
            }
        });

        notitasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotitasModel notitasModel = listaNotas.get(recyclerViewNotas.getChildAdapterPosition(view));
                alertEditarNota(notitasModel);
            }
        });


        notitasAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Seguro que deseas eliminar?")
                        .setTitle("Eliminar")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarNota(view);
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

    public void alertNuevaNota(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.nueva_notita, null);
        builder.setView(view);

        final EditText edtTitulo = view.findViewById(R.id.edtTitulo);
        final EditText edtDescripcion = view.findViewById(R.id.edtDescripcion);

        builder.setMessage("Agregar");
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombre = edtTitulo.getText().toString().trim();
                String descripcion = edtDescripcion.getText().toString();
                if (nombre.length()>0 && descripcion.length()>0){
                    guardarNota(nombre, descripcion);
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


    public void alertEditarNota(final NotitasModel notitasModel){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.nueva_notita, null);
        builder.setView(view);

        final EditText edtTitulo = view.findViewById(R.id.edtTitulo);
        final EditText edtDescripcion = view.findViewById(R.id.edtDescripcion);

        edtTitulo.setText(notitasModel.getTitulo());
        edtDescripcion.setText(notitasModel.getDescripcion());

        edtTitulo.setSelection(edtTitulo.getText().length());

        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombreNota = edtTitulo.getText().toString().trim();
                String descNota = edtDescripcion.getText().toString();
                if (nombreNota.length()>0){
                    realm.beginTransaction();
                    notitasModel.setTitulo(nombreNota);
                    notitasModel.setDescripcion(descNota);
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

    public void guardarNota(String nombre, String apellido){
        realm.beginTransaction();
        NotitasModel notitasModel = new NotitasModel(nombre, apellido);
        realm.copyToRealm(notitasModel);
        realm.commitTransaction();
    }

    public void eliminarNota(View view){
        NotitasModel notitasModel = listaNotas.get(recyclerViewNotas.getChildAdapterPosition(view));
        realm.beginTransaction();
        if(notitasModel != null){
            notitasModel.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    @Override
    public void onChange(@NonNull RealmResults<NotitasModel> notitasModels) {
        notitasAdapter.notifyDataSetChanged();
    }



}

