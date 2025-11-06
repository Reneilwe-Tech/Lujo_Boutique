package com.example.lujosboutique1;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {
    private List<String> sizes;
    private ProductType productType;
    private OnSizeSelectedListener listener;
    private int selectedPosition = -1;

    public interface OnSizeSelectedListener {
        void onSizeSelected(String size);
    }

    public enum ProductType {
        SHOES, CLOTHING, ACCESSORIES
    }

    public SizeAdapter(List<String> sizes, ProductType productType, OnSizeSelectedListener listener) {
        this.sizes = sizes;
        this.productType = productType;
        this.listener = listener;
    }

    public class SizeViewHolder extends RecyclerView.ViewHolder {
        TextView sizeText;
        MaterialCardView sizeCard;

        public SizeViewHolder(View itemView) {
            super(itemView);
            sizeText = itemView.findViewById(R.id.size_text);
            sizeCard = itemView.findViewById(R.id.size_card);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    int previousPosition = selectedPosition;
                    selectedPosition = position;
                    if (previousPosition != -1) notifyItemChanged(previousPosition);
                    notifyItemChanged(selectedPosition);
                    listener.onSizeSelected(sizes.get(position));
                }
            });
        }
    }

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes;
        switch (productType) {
            case SHOES:
                layoutRes = R.layout.item_shoe_size;
                break;
            case CLOTHING:
            case ACCESSORIES:
            default:
                layoutRes = R.layout.item_clothing_size;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        String size = sizes.get(position);
        holder.sizeText.setText(size);

        Context context = holder.itemView.getContext();
        if (position == selectedPosition) {
            holder.sizeCard.setStrokeColor(ContextCompat.getColor(context, R.color.blue_green));
            holder.sizeCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue_green));
            holder.sizeText.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.sizeCard.setStrokeColor(ContextCompat.getColor(context, R.color.lightGray));
            holder.sizeCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.sizeText.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

    public String getSelectedSize() {
        return selectedPosition != -1 ? sizes.get(selectedPosition) : null;
    }
}