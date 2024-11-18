package com.example.iotintromisin.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iotintromisin.interfaces.ActividadApi;
import com.example.iotintromisin.model.Activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<List<Activity>> actividades = new MutableLiveData<>();

    public LiveData<List<Activity>> getActividades() {
        return actividades;
    }

    /*private final MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }*/
}