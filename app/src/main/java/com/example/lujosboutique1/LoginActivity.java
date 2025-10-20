package com.example.lujosboutique1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmailOrPhone, editTextPassword;
    private CheckBox checkBoxRememberMe;
    private Button btnLogin;
    private ImageButton btnGoogleSignIn;
    private TextView tvForgotPassword, tvSignUpLink;

    //  API endpoint
    private static final String LOGIN_URL = "https://your-api-domain.com/api/login";
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        client = new OkHttpClient();
        sharedPreferences = getSharedPreferences("LujosBoutiquePrefs", MODE_PRIVATE);

        // IDs defined in XML
        editTextEmailOrPhone = findViewById(R.id.editTextPassword);
        editTextPassword = findViewById(R.id.editPassword);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);

        loadSavedCredentials();

        btnLogin.setOnClickListener(v -> handleLogin());

        btnGoogleSignIn.setOnClickListener(v ->
                Toast.makeText(LoginActivity.this, "Google Sign In coming soon!", Toast.LENGTH_SHORT).show());

        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(LoginActivity.this, "Password reset coming soon!", Toast.LENGTH_SHORT).show());

        tvSignUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    private void loadSavedCredentials() {
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");
            editTextEmailOrPhone.setText(savedEmail);
            editTextPassword.setText(savedPassword);
            checkBoxRememberMe.setChecked(true);
        }
    }

    private void handleLogin() {
        String emailOrPhone = editTextEmailOrPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (emailOrPhone.isEmpty()) {
            editTextEmailOrPhone.setError("Email or phone is required");
            editTextEmailOrPhone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        if (checkBoxRememberMe.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", true);
            editor.putString("email", emailOrPhone);
            editor.putString("password", password);
            editor.apply();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.remove("email");
            editor.remove("password");
            editor.apply();
        }

        loginUser(emailOrPhone, password);
    }

    private void loginUser(String emailOrPhone, String password) {
        btnLogin.setEnabled(false);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", emailOrPhone);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            btnLogin.setEnabled(true);
            return;
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this,
                            "Network error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                final boolean isSuccessful = response.isSuccessful();

                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);

                    if (isSuccessful) {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);

                            String token = jsonResponse.optString("token", "");
                            String userId = jsonResponse.optString("userId", "");
                            String userName = jsonResponse.optString("name", "");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("authToken", token);
                            editor.putString("userId", userId);
                            editor.putString("userName", userName);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            Toast.makeText(LoginActivity.this,
                                    "Login successful!",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, activity_home_overview.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this,
                                    "Error parsing response",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject errorJson = new JSONObject(responseBody);
                            String errorMessage = errorJson.optString("message", "Login failed");
                            Toast.makeText(LoginActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this,
                                    "Invalid email or password. Please try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
