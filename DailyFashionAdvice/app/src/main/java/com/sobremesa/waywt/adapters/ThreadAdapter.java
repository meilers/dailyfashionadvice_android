package com.sobremesa.waywt.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobremesa.waywt.R;
import com.sobremesa.waywt.events.ThreadItemClickedEvent;
import com.sobremesa.waywt.models.RemoteThread;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by omegatai on 14-11-19.
 */
public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {
    private List<RemoteThread> mThreads;

    public static interface ThreadViewHolderClickListener {
        public void onClick(int position);
    }

    private ThreadViewHolderClickListener mThreadViewHolderClickListener = new ThreadViewHolderClickListener() {
        @Override
        public void onClick(int position) {
            EventBus.getDefault().post(new ThreadItemClickedEvent(position, mThreads.get(position)));
        }
    };


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ThreadViewHolderClickListener mThreadViewHolderClickListener;
        // each data item is just a string in this case
        public TextView mTitleTv;

        public ViewHolder(View v, ThreadViewHolderClickListener threadViewHolderClickListener) {
            super(v);

            mThreadViewHolderClickListener = threadViewHolderClickListener;
            mTitleTv = (TextView)v.findViewById(R.id.list_item_thread_title_tv);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mThreadViewHolderClickListener.onClick(getPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ThreadAdapter(List<RemoteThread> threads) {
        mThreads = threads;
        mSelectedItems = new SparseBooleanArray();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ThreadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_thread, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v, mThreadViewHolderClickListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitleTv.setText(mThreads.get(position).getTitle());

        holder.itemView.setActivated(mSelectedItems.get(position, false));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mThreads.size();
    }



    // TODO: check if future implementations have this
    private SparseBooleanArray mSelectedItems;
    
    public void toggleSelection(int pos) {
        if (mSelectedItems.get(pos, false)) {
            mSelectedItems.delete(pos);
        }
        else {
            mSelectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

}