package com.example.lujosboutique1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lujosboutique1.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductListingActivity extends AppCompatActivity {

    private TextInputEditText etProductName, etProductDescription, etProductPrice;
    private Button btnUploadImage, btnSaveListing;

    // Replace with your actual API endpoint
    private static final String CREATE_PRODUCT_URL = "https://your-api-domain.com/api/products";
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;

    private Uri selectedImageUri;
    private String selectedImagePath;

    // Activity result launcher for image picker
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LujosBoutiquePrefs", MODE_PRIVATE);

        // Initialize views
        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSaveListing = findViewById(R.id.btnSaveListing);

        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        selectedImagePath = getRealPathFromURI(selectedImageUri);
                        Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
                        btnUploadImage.setText("Image Selected ✓");
                    }
                }
        );

        // Set click listener for upload image button
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Set click listener for save listing button
        btnSaveListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveListing();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) return null;

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }

    private void handleSaveListing() {
        // Get input values
        String productName = etProductName.getText().toString().trim();
        String productDescription = etProductDescription.getText().toString().trim();
        String productPriceStr = etProductPrice.getText().toString().trim();

        // Validate inputs
        if (productName.isEmpty()) {
            etProductName.setError("Product name is required");
            etProductName.requestFocus();
            return;
        }

        if (productDescription.isEmpty()) {
            etProductDescription.setError("Product description is required");
            etProductDescription.requestFocus();
            return;
        }

        if (productPriceStr.isEmpty()) {
            etProductPrice.setError("Product price is required");
            etProductPrice.requestFocus();
            return;
        }

        double productPrice;
        try {
            productPrice = Double.parseDouble(productPriceStr);
            if (productPrice <= 0) {
                etProductPrice.setError("Price must be greater than 0");
                etProductPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etProductPrice.setError("Invalid price format");
            etProductPrice.requestFocus();
            return;
        }

        // Check if user is logged in
        String authToken = sharedPreferences.getString("authToken", "");
        if (authToken.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProductListingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Create the product listing
        createProductListing(productName, productDescription, productPrice, authToken);
    }

    private void createProductListing(String name, String description, double price, String token) {
        // Disable button to prevent multiple clicks
        btnSaveListing.setEnabled(false);

        // Build multipart request body
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("description", description)
                .addFormDataPart("price", String.valueOf(price));

        // Add image if selected
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            File imageFile = new File(selectedImagePath);
            if (imageFile.exists()) {
                RequestBody imageBody = RequestBody.create(
                        imageFile,
                        MediaType.parse("image/*")
                );
                builder.addFormDataPart("image", imageFile.getName(), imageBody);
            }
        }

        RequestBody requestBody = builder.build();

        // Create request with authorization header
        Request request = new Request.Builder()
                .url(CREATE_PRODUCT_URL)
                .addHeader("Authorization", "Bearer " + token)
                .post(requestBody)
                .build();

        // Make API call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSaveListing.setEnabled(true);
                        Toast.makeText(ProductListingActivity.this,
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
                        btnSaveListing.setEnabled(true);

                        if (isSuccessful) {
                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                String productId = jsonResponse.optString("id", "");

                                Toast.makeText(ProductListingActivity.this,
                                        "Product listed successfully!",
                                        Toast.LENGTH_SHORT).show();

                                // Clear form
                                clearForm();

                                // Optional: Navigate back or to product list
                                // finish();

                            } catch (JSONException e) {
                                Toast.makeText(ProductListingActivity.this,
                                        "Product created but error parsing response",
                                        Toast.LENGTH_SHORT).show();
                                clearForm();
                            }
                        } else {
                            try {
                                JSONObject errorJson = new JSONObject(responseBody);
                                String errorMessage = errorJson.optString("message", "Failed to create listing");
                                Toast.makeText(ProductListingActivity.this,
                                        errorMessage,
                                        Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                Toast.makeText(ProductListingActivity.this,
                                        "Failed to create listing. Please try again.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }

    private void clearForm() {
        etProductName.setText("");
        etProductDescription.setText("");
        etProductPrice.setText("");
        selectedImageUri = null;
        selectedImagePath = null;
        btnUploadImage.setText(getString(R.string.button_upload_image));
    }
}