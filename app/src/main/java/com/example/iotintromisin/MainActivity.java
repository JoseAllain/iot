package com.example.iotintromisin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotintromisin.databinding.ActivityMainBinding;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String ESP32_IP = "https://distinctly-diverse-moth.ngrok-free.app";
    private Button btnToogle;
    private Button btnToogle1;
    private Button btnToogle2;
    private Button btnToogle3;
    private Button btnToogle4;
    private Button btnToogle5;
    private Button btnToogle6;
    private Button btnToogle7;

    private boolean ledEncendido = false;
    private SharedPreferences preferences;
    private static final String PREF_LED_STATE = "led_state";

    private Handler handler;
    private Runnable periodicTask;
    private boolean isAlarmActive = false;
    private OkHttpClient client;

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        client=new OkHttpClient();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        /*WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://http://192.169.100.89");*/

        btnToogle = findViewById(R.id.btnToogle);
        btnToogle1 = findViewById(R.id.btnToogle1);
        btnToogle3 = findViewById(R.id.btnToogle3);
        btnToogle4 = findViewById(R.id.btnToogle4);
        btnToogle5 = findViewById(R.id.btnToogle5);
        btnToogle6 = findViewById(R.id.btnToogle6);
        btnToogle7 = findViewById(R.id.btnToogle7);
        Switch alarmSwitch = findViewById(R.id.switch1);

        preferences = getSharedPreferences("LedPrefs", MODE_PRIVATE);
        // Recuperar el último estado conocido del LED
        ledEncendido = preferences.getBoolean(PREF_LED_STATE, false);

        btnToogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledEncendido = !ledEncendido; // Cambiar estado
                String endpoint = ledEncendido ? "/led/on?pin=4" : "/led/off?pin=4";
                sendRequest(endpoint);
                actualizarTextoBoton(btnToogle);
                guardarEstadoLed();
            }
        });
        btnToogle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledEncendido = !ledEncendido; // Cambiar estado
                String endpoint = ledEncendido ? "/led/on?pin=5" : "/led/off?pin=5";
                sendRequest(endpoint);
                actualizarTextoBoton(btnToogle1);
                guardarEstadoLed();
            }
        });
        btnToogle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledEncendido = !ledEncendido; // Cambiar estado
                String endpoint = ledEncendido ? "/led/on?pin=18" : "/led/off?pin=18";
                sendRequest(endpoint);
                actualizarTextoBoton(btnToogle3);
                guardarEstadoLed();
            }
        });
        btnToogle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledEncendido = !ledEncendido; // Cambiar estado
                String endpoint = ledEncendido ? "/led/on?pin=19" : "/led/off?pin=19";
                sendRequest(endpoint);
                actualizarTextoBoton(btnToogle4);
                guardarEstadoLed();
            }
        });
        btnToogle5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledEncendido = !ledEncendido; // Cambiar estado
                String endpoint = ledEncendido ? "/led/on?pin=21" : "/led/off?pin=21";
                sendRequest(endpoint);
                actualizarTextoBoton(btnToogle5);
                guardarEstadoLed();
            }
        });
        btnToogle6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledEncendido = !ledEncendido; // Cambiar estado
                String endpoint = ledEncendido ? "/led/on?pin=13" : "/led/off?pin=13";
                sendRequest(endpoint);
                actualizarTextoBoton(btnToogle6);
                guardarEstadoLed();
            }
        });
        btnToogle7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ledEncendido = !ledEncendido; // Cambiar estado
                String endpoint = ledEncendido ? "/led/on?pin=12" : "/led/off?pin=12";
                sendRequest(endpoint);
                actualizarTextoBoton(btnToogle7);
                guardarEstadoLed();
            }
        });

        // Configurar el switch para activar/desactivar la alarma
        alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                String endpoint = "/alarm/activate";
                sendRequest(endpoint);
            }else{
                String endpoint = "/alarm/deactivate";
                sendRequest(endpoint);

            }
        });


        // Configurar la tarea periódica
        periodicTask = new Runnable() {
            @Override
            public void run() {
                if (isAlarmActive) { // Solo verificar si la alarma está activa
                    checkMotionStatus();
                }
                handler.postDelayed(this, 5000); // Ejecutar nuevamente en 5 segundos
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMotionDetection(); // Detener el handler al destruir la actividad
    }

    private void startMotionDetection() {
        handler.post(periodicTask);
    }

    private void stopMotionDetection() {
        handler.removeCallbacks(periodicTask);
    }

    private void actualizarTextoBoton(Button button) {
        button.setText(ledEncendido ? "Apagar LED" : "Encender LED");
        button.setBackgroundTintList(ContextCompat.getColorStateList(this, ledEncendido ? R.color.red : R.color.green));
    }

    private void guardarEstadoLed() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_LED_STATE, ledEncendido);
        editor.apply();
    }

    private void sendRequest(final String endpoint) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ESP32_IP + endpoint);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        runOnUiThread(() ->
                                Toast.makeText(MainActivity.this, "Error al enviar comando", Toast.LENGTH_SHORT).show()
                        );
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Error de conexión "+e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*private void guardarActividad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ESP32_IP + "/api/activities/crearActividad");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        runOnUiThread(() ->
                                Toast.makeText(MainActivity.this, "Error al enviar comando", Toast.LENGTH_SHORT).show()
                        );
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Error de conexión "+e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
    private void checkMotionStatus() {
        String url = ESP32_IP + "/motion/status";

        new Thread(() -> {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    if (responseBody.contains("motion: true")) {
                        // Si detecta movimiento, mostrar el modal y vibrar
                        runOnUiThread(() -> {
                            showAlert();
                            vibratePhone();
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> showErrorDialog(e.getMessage()));
            }
        }).start();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ESP32_IP + "/motion/status");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        runOnUiThread(() ->
                                Toast.makeText(MainActivity.this, "Go billar al enviar comando", Toast.LENGTH_SHORT).show()
                        );
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Error de conexión "+e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle("¡Alerta!")
                .setMessage("Se ha detectado movimiento")
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void showErrorDialog(String ga) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("No se pudo conectar al servidor"+ ga)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void vibratePhone() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(500); // Vibrar durante 500ms
        }
    }
}
