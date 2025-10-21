package com.example.lujosboutique1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateAccountActivity extends AppCompatActivity {

    private TextView tvLoginLink;
    private EditText editTextFullName, editTextPassword, editTextEmailOrPhone;
    private Button btnSignUp, btnGoogleSignUp;

    // Replace with your actual API endpoint
    private static final String SIGNUP_URL = "https://your-api-domain.com/api/signup";
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize views using IDs from your XML
        tvLoginLink = findViewById(R.id.tvLoginLink);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPassword = findViewById(R.id.editPassword);
        editTextEmailOrPhone = findViewById(R.id.editTextPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoogleSignUp = findViewById(R.id.btnGoogleSignUp);

        // Navigate to login activity
        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Handle sign-up button click
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        // Handle Google sign-up click
        btnGoogleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateAccountActivity.this, "Google Sign Up coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSignUp() {
        // Get input values
        String fullName = editTextFullName.getText().toString().trim();
        String emailOrPhone = editTextEmailOrPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate inputs
        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
            return;
        }

        if (emailOrPhone.isEmpty()) {
            editTextEmailOrPhone.setError("Email or phone is required");
            editTextEmailOrPhone.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrPhone).matches()) {
            editTextEmailOrPhone.setError("Please enter a valid email");
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

        // Call the API
        signUpUser(fullName, emailOrPhone, password);
    }

    private void signUpUser(String name, String email, String password) {
        btnSignUp.setEnabled(false);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            btnSignUp.setEnabled(true);
            return;
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(SIGNUP_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSignUp.setEnabled(true);
                        Toast.makeText(CreateAccountActivity.this,
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
                        btnSignUp.setEnabled(true);

                        if (isSuccessful) {
                            Toast.makeText(CreateAccountActivity.this,
                                    "Account created successfully!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CreateAccountActivity.this,
                                    "Sign up failed. Please try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
//