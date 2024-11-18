package com.example.iotintromisin.config;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotintromisin.R;
import com.example.iotintromisin.model.Activity;

import java.util.List;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ViewHolder>{
    private List<Activity> activityList; // Lista de actividades

    // Constructor
    public ActividadAdapter(List<Activity> activityList) {
        this.activityList = activityList;
    }

    // Infla el layout para cada elemento del RecyclerView
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actividad, parent, false);
        return new ViewHolder(view);
    }

    // Asocia los datos con las vistas del layout
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Activity activity = activityList.get(position);
        holder.descripcionTextView.setText(activity.getDescription()); // Ajusta según los campos de tu objeto
        holder.horaTextView.setText(activity.getTimestamp());
    }

    // Devuelve el número de elementos en la lista
    @Override
    public int getItemCount() {
        return activityList.size();
    }

    // Clase interna para definir el ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descripcionTextView;
        public TextView horaTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            descripcionTextView = itemView.findViewById(R.id.descripcion); // IDs del layout
            horaTextView = itemView.findViewById(R.id.hora);
        }
    }
}
