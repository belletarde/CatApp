package com.app.cat.kevin.thecatapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.cat.kevin.thecatapp.R;
import com.app.cat.kevin.thecatapp.model.Cat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.ViewHolder> {

    private Context context;
    private List<Cat> catList;
    private OnListClick onCatListClickListener;
    private OnLikeClick onLikeClickListener;
    private OnShareClick onShareClickListener;

    public CatListAdapter(Context context, List<Cat> catList, OnListClick onCatListClickListener,
                          OnLikeClick onLikeClickListener, OnShareClick onShareClickListener) {
        this.context = context;
        this.catList = catList;
        this.onLikeClickListener = onLikeClickListener;
        this.onCatListClickListener = onCatListClickListener;
        this.onShareClickListener = onShareClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list_item, parent, false);
        return new CatListAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    @Override
    public void onBindViewHolder(final CatListAdapter.ViewHolder holder, int position) {

        Cat cat = catList.get(position);
        Glide
            .with(context)
            .load(cat.getUrl())
            .apply(new RequestOptions().centerCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.catImage);

        if (cat.getLike()) {
            setLikeImage(holder.likeImageBtn, R.drawable.ic_action_heart_selected);
        } else {
            setLikeImage(holder.likeImageBtn, R.drawable.ic_action_heart);
        }

        holder.itemView.setOnClickListener(view -> onCatListClickListener.onCatListClickListener(cat.getId()));

        holder.likeImageBtn.setOnClickListener(v -> onLikeClickListener.onLikeClickListener(cat.getId(), position));

        holder.shareImageBtn.setOnClickListener(v -> onShareClickListener.onShareClickListener(cat));
    }

    public interface OnListClick {
        void onCatListClickListener(String id);
    }

    public interface OnShareClick {
        void onShareClickListener(Cat cat);
    }

    public interface OnLikeClick {
        void onLikeClickListener(String id, int position);
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.cat_image)
        ImageView catImage;

        @Nullable
        @BindView(R.id.cat_like_ic)
        ImageView likeImageBtn;

        @BindView(R.id.cat_share_ic)
        ImageView shareImageBtn;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private void setLikeImage(ImageView imageLike, int imgDrawable) {
        Glide
            .with(context)
            .load(imgDrawable)
            .into(imageLike);
    }
}
