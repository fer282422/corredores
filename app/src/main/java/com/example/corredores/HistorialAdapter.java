package com.example.corredores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistorialAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HistorialActivity.Entrenamiento> entrenamientos;
    private LayoutInflater inflater;

    public HistorialAdapter(Context context, ArrayList<HistorialActivity.Entrenamiento> entrenamientos) {
        this.context = context;
        this.entrenamientos = entrenamientos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return entrenamientos.size();
    }

    @Override
    public Object getItem(int position) {
        return entrenamientos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_entrenamiento, parent, false);
            holder = new ViewHolder();
            holder.tvFecha = convertView.findViewById(R.id.tvFecha);
            holder.tvDistancia = convertView.findViewById(R.id.tvDistancia);
            holder.tvTiempo = convertView.findViewById(R.id.tvTiempo);
            holder.tvTipo = convertView.findViewById(R.id.tvTipo);
            holder.tvRitmo = convertView.findViewById(R.id.tvRitmo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HistorialActivity.Entrenamiento entrenamiento = entrenamientos.get(position);

        // datos en la vista
        holder.tvFecha.setText(entrenamiento.getFecha());
        holder.tvDistancia.setText(String.format("%.1f km", entrenamiento.getDistancia()));
        holder.tvTiempo.setText(String.format("%d min", entrenamiento.getTiempo()));
        holder.tvTipo.setText(entrenamiento.getTipo());
        holder.tvRitmo.setText(String.format("%.2f min/km", entrenamiento.getRitmo()));

        return convertView;
    }

    // viewHolder del listview
    static class ViewHolder {
        TextView tvFecha;
        TextView tvDistancia;
        TextView tvTiempo;
        TextView tvTipo;
        TextView tvRitmo;
    }
}