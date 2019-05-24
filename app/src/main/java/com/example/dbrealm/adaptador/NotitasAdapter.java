package com.example.dbrealm.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dbrealm.R;
import com.example.dbrealm.modelos.NotitasModel;


import java.util.List;

public class NotitasAdapter extends RecyclerView.Adapter<NotitasAdapter.ViewHolderPersona>
        implements View.OnClickListener, View.OnLongClickListener{

    private Context context;
    private List<NotitasModel>listaCategoria;
    private View.OnClickListener listener;
    private View.OnLongClickListener onLongClickListener;

    public NotitasAdapter(Context context, List<NotitasModel> listaCategoria){
        this.context = context;
        this.listaCategoria = listaCategoria;
    }

    @NonNull
    @Override
    public ViewHolderPersona onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notita,parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolderPersona(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPersona holder, int position) {
        holder.txtNombre.setText(listaCategoria.get(position).getTitulo());
        holder.txtDesc.setText(listaCategoria.get(position).getDescripcion());
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
        TextView txtDesc;

        public ViewHolderPersona(View itemView){
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtTitulo);
            txtDesc = itemView.findViewById(R.id.txtDesc);
        }

    }


}
