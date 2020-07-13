package com.android.mvp.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author ccx
 * @date 2019/6/14
 */
public abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void onItemHolderClick(RecyclerView.ViewHolder holder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, holder.itemView,
                    holder.getAdapterPosition(), holder.getItemId());
        }
    }

    static class BaseItemDecoration extends RecyclerView.ItemDecoration {

        protected Drawable divider;

        public BaseItemDecoration(Drawable divider) {
            this.divider = divider;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        }


        public void drawHorizontal(Canvas c, RecyclerView parent) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin
                        + divider.getIntrinsicWidth();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + divider.getIntrinsicHeight();
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);

                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin;
                final int left = child.getRight() + params.rightMargin;
                final int right = left + divider.getIntrinsicWidth();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    public static class BaseGridItemDecoration extends BaseItemDecoration {

        private int turnNum;

        public BaseGridItemDecoration(Drawable divider) {
            this(divider, 5);
        }

        public BaseGridItemDecoration(Drawable divider, int num) {
            super(divider);
            this.turnNum = num;
        }


        @Override
        public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
            super.getItemOffsets(outRect, itemPosition, parent);
            int spanCount = getSpanCount(parent);
            int childCount = parent.getAdapter().getItemCount();
            if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
                outRect.set(divider.getIntrinsicWidth(), divider.getIntrinsicHeight(), divider.getIntrinsicWidth(),
                        0);
            } else if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
                outRect.set(divider.getIntrinsicWidth(), divider.getIntrinsicHeight(),
                        divider.getIntrinsicWidth() * turnNum,
                        divider.getIntrinsicHeight());
            } else if (isFirstRaw(parent, itemPosition, spanCount, childCount)) {
                outRect.set(divider.getIntrinsicWidth() * turnNum, divider.getIntrinsicHeight(),
                        divider.getIntrinsicWidth(),
                        divider.getIntrinsicHeight());
            } else {
                outRect.set(divider.getIntrinsicWidth(), divider.getIntrinsicHeight(), divider.getIntrinsicWidth(),
                        divider.getIntrinsicHeight());
            }

        }

        private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                return pos % spanCount == 0;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                return pos % spanCount == 0;
            }
            return false;
        }

        private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                childCount = childCount - childCount % spanCount;
                return pos >= childCount;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                // StaggeredGridLayoutManager 且纵向滚动
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    childCount = childCount - childCount % spanCount;
                    // 如果是最后一行，则不需要绘制底部
                    return pos >= childCount;
                } else { // StaggeredGridLayoutManager 且横向滚动
                    // 如果是最后一行，则不需要绘制底部
                    return (pos + 1) % spanCount == 0;
                }
            }
            return false;
        }

        private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
                                     int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                return (pos + 1) % spanCount == 0;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    return (pos + 1) % spanCount == 0;
                } else {
                    childCount = childCount - childCount % spanCount;
                    return pos >= childCount;
                }
            }
            return false;
        }

        private int getSpanCount(RecyclerView parent) {
            int spanCont = -1;
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                spanCont = ((GridLayoutManager) layoutManager).getSpanCount();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanCont = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            }
            return spanCont;
        }
    }

    public static class BaseLinearLayoutDefaultMarginItemDecoration extends BaseItemDecoration {

        private int turnNum;

        public BaseLinearLayoutDefaultMarginItemDecoration(Drawable divider) {
            this(divider, 5);
        }

        public BaseLinearLayoutDefaultMarginItemDecoration(Drawable divider, int num) {
            super(divider);
            this.turnNum = num;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
            super.getItemOffsets(outRect, itemPosition, parent);
            int spanCount = getSpanCount(parent);
            int childCount = parent.getAdapter().getItemCount();
            if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
                outRect.set(divider.getIntrinsicWidth(), divider.getIntrinsicHeight(), divider.getIntrinsicWidth(),
                        0);
            } else if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
                outRect.set(divider.getIntrinsicWidth(), divider.getIntrinsicHeight() * turnNum,
                        divider.getIntrinsicWidth(),
                        divider.getIntrinsicHeight());
            } else if (isFirstRaw(parent, itemPosition, spanCount, childCount)) {
                outRect.set(divider.getIntrinsicWidth(), divider.getIntrinsicHeight() * turnNum,
                        divider.getIntrinsicWidth(),
                        divider.getIntrinsicHeight());
            } else {
                outRect.set(divider.getIntrinsicWidth(), divider.getIntrinsicHeight(), divider.getIntrinsicWidth(),
                        divider.getIntrinsicHeight());
            }

        }

        private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                return pos % spanCount == 0;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                return pos % spanCount == 0;
            }
            return false;
        }

        private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                childCount = childCount - childCount % spanCount;
                return pos >= childCount;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                // StaggeredGridLayoutManager 且纵向滚动
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    childCount = childCount - childCount % spanCount;
                    // 如果是最后一行，则不需要绘制底部
                    return pos >= childCount;
                } else { // StaggeredGridLayoutManager 且横向滚动
                    // 如果是最后一行，则不需要绘制底部
                    return (pos + 1) % spanCount == 0;
                }
            }
            return false;
        }

        private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
                                     int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                return (pos + 1) % spanCount == 0;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    return (pos + 1) % spanCount == 0;
                } else {
                    childCount = childCount - childCount % spanCount;
                    return pos >= childCount;
                }
            }
            return false;
        }

        private int getSpanCount(RecyclerView parent) {
            int spanCont = -1;
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                spanCont = ((GridLayoutManager) layoutManager).getSpanCount();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanCont = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            }
            return spanCont;
        }
    }


    public static class BaseMoveTopBottomDefaultMarginItemDecoration extends RecyclerView.ItemDecoration {

        private int turnNum;

        public BaseMoveTopBottomDefaultMarginItemDecoration(int turnNum) {
            this.turnNum = turnNum;
        }


        @Override
        public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
            super.getItemOffsets(outRect, itemPosition, parent);
            int spanCount = getSpanCount(parent);
            int childCount = parent.getAdapter().getItemCount();
            if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
                outRect.set(0, 1, 0,
                        0);
            } else if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
                outRect.set(0, turnNum,
                        0,
                        1);
            } else if (isFirstRaw(parent, itemPosition, spanCount, childCount)) {
                outRect.set(0, turnNum,
                        0,
                        1);
            } else {
                outRect.set(0, turnNum, 0,
                        turnNum);
            }

        }

        private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                return pos % spanCount == 0;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                return pos % spanCount == 0;
            }
            return false;
        }

        private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                childCount = childCount - childCount % spanCount;
                return pos >= childCount;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                // StaggeredGridLayoutManager 且纵向滚动
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    childCount = childCount - childCount % spanCount;
                    // 如果是最后一行，则不需要绘制底部
                    return pos >= childCount;
                } else { // StaggeredGridLayoutManager 且横向滚动
                    // 如果是最后一行，则不需要绘制底部
                    return (pos + 1) % spanCount == 0;
                }
            }
            return false;
        }

        private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
                                     int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                return (pos + 1) % spanCount == 0;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    return (pos + 1) % spanCount == 0;
                } else {
                    childCount = childCount - childCount % spanCount;
                    return pos >= childCount;
                }
            }
            return false;
        }

        private int getSpanCount(RecyclerView parent) {
            int spanCont = -1;
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                spanCont = ((GridLayoutManager) layoutManager).getSpanCount();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanCont = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            }
            return spanCont;
        }
    }

}
