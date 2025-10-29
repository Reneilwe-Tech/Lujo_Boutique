package com.example.lujosboutique1;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Map;

public class AccountSettingsActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etAddress, etCurrentPassword, etNewPassword;
    Button btnSaveProfile, btnChangePassword;
    ApiInterface apiInterface;
    int userId = 1; // Replace with logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        loadUserInfo();

        btnSaveProfile.setOnClickListener(v -> updateProfile());
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void loadUserInfo() {
        apiInterface.getUserInfo(userId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body().get("success"))) {
                    Map user = (Map) response.body().get("user");
                    etName.setText(user.get("name").toString());
                    etEmail.setText(user.get("email").toString());
                    etPhone.setText(user.get("phone") != null ? user.get("phone").toString() : "");
                    etAddress.setText(user.get("address") != null ? user.get("address").toString() : "");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(AccountSettingsActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile() {
        apiInterface.updateProfile(userId, etName.getText().toString(), etPhone.getText().toString(), etAddress.getText().toString())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Toast.makeText(AccountSettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(AccountSettingsActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void changePassword() {
        apiInterface.changePassword(userId,
                etCurrentPassword.getText().toString(),
                etNewPassword.getText().toString()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Toast.makeText(AccountSettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(AccountSettingsActivity.this, "Error changing password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
