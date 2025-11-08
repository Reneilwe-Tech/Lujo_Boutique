package com.example.lujosboutique1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lujosboutique1.Customer;
import com.example.lujosboutique1.R;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<Customer> customerList;
    private OnCustomerClickListener listener;

    public interface OnCustomerClickListener {
        void onCustomerClick(Customer customer);
    }

    public CustomerAdapter(List<Customer> customerList, OnCustomerClickListener listener) {
        this.customerList = customerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bind(customer);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void updateData(List<Customer> newCustomers) {
        customerList.clear();
        customerList.addAll(newCustomers);
        notifyDataSetChanged();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView customerAvatar;
        private TextView customerName;
        private TextView customerEmail;
        private TextView customerPhone;
        private TextView customerStatus;
        private TextView ordersCount;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            customerAvatar = itemView.findViewById(R.id.customerAvatar);
            customerName = itemView.findViewById(R.id.customerName);
            customerEmail = itemView.findViewById(R.id.customerEmail);
            customerPhone = itemView.findViewById(R.id.customerPhone);
            customerStatus = itemView.findViewById(R.id.customerStatus);
            ordersCount = itemView.findViewById(R.id.ordersCount);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCustomerClick(customerList.get(position));
                    }
                }
            });
        }

        public void bind(Customer customer) {
            customerName.setText(customer.getName());
            customerEmail.setText(customer.getEmail());
            customerPhone.setText(customer.getPhone());
            customerStatus.setText(customer.getStatus());
            ordersCount.setText(customer.getOrdersCount() + " orders");

            // Set status background color
            if ("Active".equals(customer.getStatus())) {
                customerStatus.setBackgroundResource(R.drawable.status_active_background);
                customerStatus.setTextColor(itemView.getContext().getColor(R.color.green));
            } else {
                customerStatus.setBackgroundResource(R.drawable.status_inactive_background);
                customerStatus.setTextColor(itemView.getContext().getColor(R.color.red));
            }
        }
    }
}