package com.example.lujosboutique1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lujosboutique1.models.ReturnResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnRequestActivity extends AppCompatActivity {

    EditText etOrderId, etProductId, etReason, etRefundAmount;
    Button btnSubmitReturn;
    ApiInterface apiInterface;
    int userId = 1; // Replace with logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_request);

        etOrderId = findViewById(R.id.etOrderId);
        etProductId = findViewById(R.id.etProductId);
        etReason = findViewById(R.id.etReason);
        etRefundAmount = findViewById(R.id.etRefundAmount);
        btnSubmitReturn = findViewById(R.id.btnSubmitReturn);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnSubmitReturn.setOnClickListener(v -> submitReturn());
    }

    private void submitReturn() {
        int orderId = Integer.parseInt(etOrderId.getText().toString());
        int productId = Integer.parseInt(etProductId.getText().toString());
        String reason = etReason.getText().toString();
        double refundAmount = Double.parseDouble(etRefundAmount.getText().toString());

        apiInterface.requestReturn(orderId, productId, userId, reason, refundAmount)
                .enqueue(new Callback<ReturnResponse>() {
                    @Override
                    public void onResponse(Call<ReturnResponse> call, Response<ReturnResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(ReturnRequestActivity.this,
                                    response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReturnResponse> call, Throwable t) {
                        Toast.makeText(ReturnRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
