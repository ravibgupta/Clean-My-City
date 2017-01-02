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

import java.util.List;

import com.ravi.cleanmycity.R;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.MyViewHolder> {

    private List<Complaint> complaintList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView address, id, time;
        public ImageView image;
        public ProgressBar pBar;

        public MyViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.complaintDescription);
            id = (TextView) view.findViewById(R.id.complaintId);
            time = (TextView) view.findViewById(R.id.complaintTime);
            image = (ImageView) view.findViewById(R.id.complaintImage);
            pBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }


    public ComplaintAdapter(Context context, List<Complaint> complaintList) {
        this.context = context;
        this.complaintList = complaintList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaint_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Complaint complaint = complaintList.get(position);
        holder.address.setText(complaint.getAddress());
        holder.id.setText(complaint.getId());
        holder.time.setText(complaint.getTime());
        Glide.with(context)
                .load(complaint.getUrl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.pBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }
}
