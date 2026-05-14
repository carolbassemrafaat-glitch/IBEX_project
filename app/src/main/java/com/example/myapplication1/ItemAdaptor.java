package com.example.myapplication1;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication1.R;
import com.example.myapplication1.data.entity.Item;
import java.text.BreakIterator;
import java.util.List;

public class ItemAdaptor extends RecyclerView.Adapter<ItemAdaptor.ItemViewHolder> {
    private List<Item> items;
    public ItemAdaptor(List<Item> items) {
        this.items = items;
    }

    public void updateItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflateItemView(parent);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        bindItem(holder, item);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    private View inflateItemView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
    }
    public void clearAll(){
        this.items.clear();
        notifyDataSetChanged();
    }
    private void bindItem(ItemViewHolder holder, Item item) {
        holder.nameText.setText(item.getItemDesc());
        holder.priceText.setText(String.valueOf(item.getPrice()));
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, priceText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }
        private void initViews(View itemView) {
            nameText = itemView.findViewById(R.id.itemName);
            priceText = itemView.findViewById(R.id.itemPrice);
        }
    }
}
