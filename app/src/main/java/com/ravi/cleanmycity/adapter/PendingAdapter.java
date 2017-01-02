package com.ravi.cleanmycity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.List;

import com.ravi.cleanmycity.R;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {

    private List<Pending> pendingList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView address, id, time;
        public ImageView image;
        public ProgressBar pBar;

        public MyViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.complaintAddress);
            id = (TextView) view.findViewById(R.id.pendingId);
            time = (TextView) view.findViewById(R.id.complaintTime);
            image = (ImageView) view.findViewById(R.id.complaintImage);
            pBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }


    public PendingAdapter(Context context, List<Pending> pendingList) {
        this.context = context;
        this.pendingList = pendingList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Pending pending = pendingList.get(position);
        holder.address.setText(pending.getAddress());
        holder.id.setText(pending.getId());
        holder.time.setText(pending.getTime());
        File file = new File(pending.getPath());
        Glide.with(context)
                .load(file)
                .listener(new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.pBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }
}
