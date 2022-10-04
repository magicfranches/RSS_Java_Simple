package com.frutasloscursos.myrss;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
{

    ArrayList<String> noticiasrss; //mContacts
    ArrayList<String> linkrss;     //link
    ArrayList<String> fecharss;    //fecha

    public MainAdapter(ArrayList<String> titles, ArrayList<String> links, ArrayList<String> pubDate) {
        this.noticiasrss = titles;
        this.linkrss = links;
        this.fecharss = pubDate;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.tvnoticias.setText(noticiasrss.get(position)); //Posicion noticias
        holder.tvfecha.setText(fecharss.get(position)); // Posicion fecha
        Uri uri = Uri.parse(linkrss.get(position));

        //On Click
        holder.tvnoticias.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lanza el link al navegador
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticiasrss.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvnoticias; //TITULOS Text View
        public TextView tvfecha;    //Fecha Text View

         public ViewHolder(@NonNull View itemView) {
            super(itemView);
             tvnoticias = (TextView) itemView.findViewById(R.id.noticia); //Noticia EN EL LAYOUT
             tvfecha = (TextView) itemView.findViewById(R.id.fecha); //Fecha EN EL LAYOUT
        }
    }
}
