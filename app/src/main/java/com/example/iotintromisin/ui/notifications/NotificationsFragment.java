package com.example.iotintromisin.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotintromisin.MainActivity;
import com.example.iotintromisin.config.ActividadAdapter;
import com.example.iotintromisin.databinding.FragmentNotificationsBinding;
import com.example.iotintromisin.interfaces.ActividadApi;
import com.example.iotintromisin.model.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private RecyclerView recyclerView;
    private ActividadAdapter adapter;
    private List<Activity> activityList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView; // Asume que tienes un RecyclerView en fragment_home.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ActividadAdapter(activityList);
        recyclerView.setAdapter(adapter);

        fetchActivities();

        return root;
    }
    private void fetchActivities() {
        // Configura Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://98.80.51.122") // Cambia por la URL de tu backend
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ActividadApi apiService = retrofit.create(ActividadApi.class);

        // Llama al endpoint GET
        apiService.obtenerActividades().enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Actualiza la lista de actividades y el adaptador
                    activityList.clear();
                    activityList.addAll(response.body());
                    Collections.sort(activityList, (a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()));
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener actividades: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error completo",t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}