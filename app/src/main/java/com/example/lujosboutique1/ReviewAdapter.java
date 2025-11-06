package com.example.lujosboutique1;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView reviewerName;
        RatingBar reviewRating;
        TextView reviewDate;
        TextView reviewComment;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            reviewerName = itemView.findViewById(R.id.reviewer_name);
            reviewRating = itemView.findViewById(R.id.review_rating);
            reviewDate = itemView.findViewById(R.id.review_date);
            reviewComment = itemView.findViewById(R.id.review_comment);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.reviewerName.setText(review.getUserName());
        holder.reviewRating.setRating(review.getRating());
        holder.reviewDate.setText(review.getDate());
        holder.reviewComment.setText(review.getComment());

        // Load avatar using Glide/Picasso if available
        // Glide.with(holder.itemView.getContext()).load(review.getUserAvatar()).into(holder.userAvatar);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}