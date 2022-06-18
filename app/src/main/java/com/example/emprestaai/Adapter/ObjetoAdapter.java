package com.example.emprestaai.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Model.Objeto;
import com.example.emprestaai.R;

import java.util.ArrayList;

public class ObjetoAdapter extends RecyclerView.Adapter<ObjetoAdapter.ViewHolder> {
    private ArrayList<Objeto> objetos;
    ItemClicado activity;

    public interface ItemClicado{
        void onItemClicked(int posicao, ArrayList<Objeto> objetos);
    }

    public ObjetoAdapter(Context context, ArrayList<Objeto> lista){
        objetos = lista;
        activity = (ItemClicado) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNome, tvDescricao, tvStatus;
        ImageView dImagem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvDescricao = itemView.findViewById(R.id.tvPeriodo);
            dImagem = itemView.findViewById(R.id.ivObjeto);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(objetos.indexOf((Objeto) itemView.getTag()),objetos);
                }
            });
        }
    }

    @NonNull
    @Override
    public ObjetoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_itens, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjetoAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(objetos.get(position));
        holder.tvNome.setText(objetos.get(position).getNome());
        holder.tvStatus.setText(objetos.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return objetos.size();
    }
}
