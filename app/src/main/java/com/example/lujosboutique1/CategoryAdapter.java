package com.example.lujosboutique1;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryName;
        TextView categoryCount;
        MaterialCardView categoryCard;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.category_icon);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryCount = itemView.findViewById(R.id.category_count);
            categoryCard = (MaterialCardView) itemView;

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onCategoryClick(categories.get(position));
                }
            });
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.categoryName.setText(category.getName());
        holder.categoryCount.setText(category.getItemCount() + " items");

        // Set icon based on category
        int iconRes = getIconForCategory(category.getName());
        holder.categoryIcon.setImageResource(iconRes);
    }

    private int getIconForCategory(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "shoes":
                return R.drawable.ic_shoes;
            case "clothing":
                return R.drawable.ic_clothing;
            case "accessories":
                return R.drawable.ic_accessories;
            case "electronics":
                return R.drawable.ic_electronics;
            default:
                return R.drawable.ic_category_default;
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
