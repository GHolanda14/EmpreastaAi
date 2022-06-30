package com.example.emprestaai.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emprestaai.Model.Pedido;
import com.example.emprestaai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SolicitacaoAdapter extends RecyclerView.Adapter<SolicitacaoAdapter.ViewHolder>{
    ArrayList<Pedido> solicitacoes;
    Context activity;

    public SolicitacaoAdapter(Context context, ArrayList<Pedido> solicitacoes) {
        activity = context;
        this.solicitacoes = solicitacoes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNome, tvPeriodo, tvStatus, tvLocalEncontro,tvSolicitante;
        ImageView dImagem;
        ImageButton btnAceitar, btnRecusar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvPeriodo = itemView.findViewById(R.id.tvPeriodo);
            dImagem = itemView.findViewById(R.id.ivObjeto);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLocalEncontro = itemView.findViewById(R.id.tvLocalEncontro);
            tvSolicitante = itemView.findViewById(R.id.tvSolicitante);
            btnAceitar = itemView.findViewById(R.id.btnAceitar);
            btnRecusar = itemView.findViewById(R.id.btnRecusar);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_solicitacoes, parent, false);
        return new SolicitacaoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvNome.setText(solicitacoes.get(position).getObjeto().getNome());
        holder.tvStatus.setText(solicitacoes.get(position).getStatus());
        holder.tvLocalEncontro.setText(solicitacoes.get(position).getLocal());
        holder.tvPeriodo.setText(solicitacoes.get(position).getPeriodo());
        holder.tvSolicitante.setText(solicitacoes.get(position).getSolicitante());
        holder.dImagem.setImageBitmap(solicitacoes.get(position).getObjeto().getImagem());

        holder.btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Pedidos")
                        .document(solicitacoes.get(position).getIdPedido())
                        .update("status","Emprestado")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    solicitacoes.get(position).setStatus("Emprestado");

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("msg",e.getMessage());
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return solicitacoes.size();
    }
}
