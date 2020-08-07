package com.android.mvp.adapter;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseItemTouchCallBack<T> extends ItemTouchHelper.Callback {
    private final List<T> list;

    public BaseItemTouchCallBack(List<T> list) {
        this.list = list;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //这个方法是设置是否滑动时间，以及拖拽的方向，所以在这里需要判断一下是列表布局还是网格布局，
        // 如果是列表布局的话则拖拽方向为DOWN和UP，如果是网格布局的话则是DOWN和UP和LEFT和RIGHT
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        //得到当拖拽的viewHolder的Position
        int fromPosition = viewHolder.getAdapterPosition();
        //拿到当前拖拽到的item的viewHolder
        int toPosition = target.getAdapterPosition();
        list.add(toPosition, list.remove(fromPosition));// change position

        recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && selectStatusEnable()) {
            // 选中时的颜色处理
            viewHolder.itemView.setBackgroundColor(statusColor());
        }
        super.onSelectedChanged(viewHolder, actionState);
    }


    protected abstract boolean selectStatusEnable();


    @ColorInt
    protected abstract int statusColor();


    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // 清除选中态不需要颜色
        if (selectStatusEnable()) {
            viewHolder.itemView.setBackgroundColor(0);
        }

    }
}
