package ru.mirea.ivlevdaniil.mirea_project.ui.gallery;

import android.net.Uri;
import android.view.*;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ru.mirea.ivlevdaniil.mirea_project.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder> {

    private final ArrayList<Uri> images;
    private final boolean selectMode;
    private final OnImageClickListener listener;

    public interface OnImageClickListener {
        void onClick(Uri uri);
    }

    public GalleryAdapter(ArrayList<Uri> images, boolean selectMode, OnImageClickListener listener) {
        this.images = images;
        this.selectMode = selectMode;
        this.listener = listener;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri uri = images.get(position);
        holder.imageView.setImageURI(uri);
        holder.imageView.setOnClickListener(v -> listener.onClick(uri));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
