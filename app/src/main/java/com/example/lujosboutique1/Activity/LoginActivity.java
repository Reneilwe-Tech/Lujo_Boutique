package com.example.lujosboutique1.Activity;

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

import com.example.lujosboutique1.HomeOverviewActivity;
import com.example.lujosboutique1.R;

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

    private EditText editTextLoginEmail, editTextLoginPassword;
    private CheckBox checkBoxRememberMe;
    private Button btnLogin;
    private ImageButton btnGoogleSignIn;
    private TextView tvForgotPassword, tvSignUpLink;

    // Replace with your actual API endpoint
    private static final String LOGIN_URL = "https://your-api-domain.com/api/login";
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LujosBoutiquePrefs", MODE_PRIVATE);

        // Initialize views - matching XML IDs
        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);

        // Load saved credentials if "Remember me" was checked
        loadSavedCredentials();

        // Set click listener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Set click listener for Google Sign In button
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,
                        "Google Sign In coming soon!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for Forgot Password
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,
                        "Password reset coming soon!",
                        Toast.LENGTH_SHORT).show();
                // You can create a ForgotPasswordActivity later
                // Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                // startActivity(intent);
            }
        });

        // Set click listener for Sign Up link
        tvSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadSavedCredentials() {
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");
            editTextLoginEmail.setText(savedEmail);
            editTextLoginPassword.setText(savedPassword);
            checkBoxRememberMe.setChecked(true);
        }
    }

    private void handleLogin() {
        // Get input values
        String email = editTextLoginEmail.getText().toString().trim();
        String password = editTextLoginPassword.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty()) {
            editTextLoginEmail.setError("Email or phone is required");
            editTextLoginEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextLoginEmail.setError("Please enter a valid email");
            editTextLoginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextLoginPassword.setError("Password is required");
            editTextLoginPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextLoginPassword.setError("Password must be at least 6 characters");
            editTextLoginPassword.requestFocus();
            return;
        }

        // Save credentials if "Remember me" is checked
        if (checkBoxRememberMe.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", true);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
        } else {
            // Clear saved credentials if unchecked
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.remove("email");
            editor.remove("password");
            editor.apply();
        }

        // Call the API
        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        // Disable button to prevent multiple clicks
        btnLogin.setEnabled(false);

        // Create JSON request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            btnLogin.setEnabled(true);
            return;
        }

        // Create request
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        // Make API call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this,
                                "Network error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                final boolean isSuccessful = response.isSuccessful();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnLogin.setEnabled(true);

                        if (isSuccessful) {
                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);

                                // Save authentication token
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

                                // Navigate to HomeOverview activity
                                Intent intent = new Intent(LoginActivity.this, HomeOverviewActivity.class);
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
                    }
                });
            }
        });
    }
}