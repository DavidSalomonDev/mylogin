package dev.davidsalomon.mylogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class RegistrarActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnSave, btnReturn;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MyLoginPrefs", MODE_PRIVATE);

        // Inicializar vistas
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSave);
        btnReturn = findViewById(R.id.btnReturn);

        // Configurar listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveUser() {
        // Obtener valores de los campos
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validar campos
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar longitud del nombre de usuario
        if (username.length() < 3) {
            Toast.makeText(this, R.string.error_username_length, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato de contraseña (alfanumérica y mínimo 5 caracteres)
        if (password.length() < 5 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            Toast.makeText(this, R.string.error_password_length, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, R.string.error_password_match, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.error_email_format, Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar datos en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username, password);
        editor.putString(username + "_fullname", fullName);
        editor.putString(username + "_email", email);
        editor.apply();

        // Mostrar mensaje de éxito
        Toast.makeText(this, R.string.success_register, Toast.LENGTH_SHORT).show();

        // Limpiar campos
        etFullName.setText("");
        etEmail.setText("");
        etUsername.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
    }
}