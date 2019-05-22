package com.example.dbrealm.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dbrealm.R;
import com.example.dbrealm.modelos.PersonaModel;


import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.ViewHolderPersona>
        implements View.OnClickListener, View.OnLongClickListener{

    private Context context;
    private List<PersonaModel>listaCategoria;
    private View.OnClickListener listener;
    private View.OnLongClickListener onLongClickListener;

    public PersonaAdapter(Context context, List<PersonaModel> listaCategoria){
        this.context = context;
        this.listaCategoria = listaCategoria;
    }

    @NonNull
    @Override
    public ViewHolderPersona onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_persona,parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolderPersona(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPersona holder, int position) {
        holder.txtNombre.setText(listaCategoria.get(position).getNombre());
        holder.txtApellido.setText(listaCategoria.get(position).getApellido());
    }

    @Override
    public int getItemCount() {
        return listaCategoria.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public boolean onLongClick(View view){
        if (onLongClickListener != null){
            onLongClickListener.onLongClick(view);
        }
        return false;
    }

    public class ViewHolderPersona extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtApellido;

        public ViewHolderPersona(View itemView){
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtApellido = itemView.findViewById(R.id.txtApellido);

        }

    }


}
