package com.example.dbrealm.aplicacion;

import android.app.Application;

import com.example.dbrealm.modelos.PersonaModel;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class Aplicacion extends Application {

    public static AtomicInteger personaId = new AtomicInteger();

    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(getApplicationContext());
        setUpConfig();
        Realm realm = Realm.getDefaultInstance();
        personaId = getByTabla(realm, PersonaModel.class);
        realm.close();
    }

    private void setUpConfig(){
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }


    private <T extends RealmObject> AtomicInteger getByTabla(Realm realm, Class<T> anyClass){

        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size()>0)? new AtomicInteger(results.max("id").intValue()): new AtomicInteger();

    }

}
