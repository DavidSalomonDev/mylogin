package dev.davidsalomon.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private Button btnLogin, btnExit;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MyLoginPrefs", MODE_PRIVATE);

        // Inicializar vistas
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);

        // Configurar listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_register) {
            Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_exit) {
            finishAffinity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el usuario existe en SharedPreferences
        String storedPassword = sharedPreferences.getString(username, null);

        if (storedPassword != null && storedPassword.equals(password)) {
            // Login exitoso
            Toast.makeText(this, R.string.success_login, Toast.LENGTH_SHORT).show();

            // Obtener el nombre completo del usuario
            String fullName = sharedPreferences.getString(username + "_fullname", "Usuario");

            // Iniciar HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("USERNAME", username);
            intent.putExtra("FULLNAME", fullName);
            startActivity(intent);
        } else {
            // Login fallido
            Toast.makeText(this, R.string.error_login, Toast.LENGTH_SHORT).show();
        }
    }
}