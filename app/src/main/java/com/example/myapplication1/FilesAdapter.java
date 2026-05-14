package com.example.myapplication1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication1.data.entity.FileEntity;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FileViewHolder> {

    private List<FileEntity> files;
    private OnFileClickListener listener;

    public interface OnFileClickListener {
        void onFileClick(FileEntity file);
    }
    public FilesAdapter(List<FileEntity> files, OnFileClickListener listener) {
        this.files = files;
        this.listener = listener;
    }
    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileEntity file = files.get(position);
        holder.textView.setText(file.fileName);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFileClick(file);
            }
        });
    }
    @Override
    public int getItemCount() {
        return files.size();
    }
    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
