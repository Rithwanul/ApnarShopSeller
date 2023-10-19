package com.bikroybaba.seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.ItemSubPopupBinding;
import com.bikroybaba.seller.interfaces.CategoryInterface;
import com.bikroybaba.seller.model.remote.request.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<Category> categoryList;
    private final Context context;
    private final CategoryInterface categoryInterface;
    ListPopupWindow popupWindow;

    public CategoryAdapter(List<Category> categoryList, Context context,
                           CategoryInterface categoryInterface, ListPopupWindow popup) {
        this.categoryList = categoryList;
        this.context = context;
        this.categoryInterface = categoryInterface;
        popupWindow = popup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubPopupBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.item_sub_popup, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.binding.popupMenuTitle.setText(category.getCategoryTitle());
        holder.itemView.setOnClickListener(v -> {
            categoryInterface.categoryId(category.getCategoryId());
            popupWindow.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSubPopupBinding binding;

        public ViewHolder(ItemSubPopupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
