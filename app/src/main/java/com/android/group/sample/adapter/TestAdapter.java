package com.android.group.sample.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mvp.adapter.BaseRecyclerViewListAdapter;

import java.util.List;

public class TestAdapter extends BaseRecyclerViewListAdapter<TestAdapter.TestViewHolder, Integer> {

    public TestAdapter(List<Integer> data) {
        super(data);
    }

    @Override
    protected void onBindViewHolder(TestViewHolder holder, Integer integer, int position) {
        holder.itemView.setBackgroundResource(integer);
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(200,300));
        return new TestViewHolder(imageView);
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
