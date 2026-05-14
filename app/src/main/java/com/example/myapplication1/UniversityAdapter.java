package com.example.myapplication1;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder> {

    private List<University> universityList;

    public UniversityAdapter(List<University> universityList) {
        this.universityList = universityList;
    }
    public void updateList(List<University> newList) {
        this.universityList = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UniversityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_university, parent, false);
        return new UniversityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityViewHolder holder, int position) {
        University uni = universityList.get(position);
        holder.txtName.setText(uni.getName());
        holder.txtCountry.setText(uni.getCountry());
    }

    @Override
    public int getItemCount() {
        return universityList != null ? universityList.size() : 0;
    }

    static class UniversityViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCountry;
        UniversityViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textName);
            txtCountry = itemView.findViewById(R.id.textCountry);
        }
    }
}
