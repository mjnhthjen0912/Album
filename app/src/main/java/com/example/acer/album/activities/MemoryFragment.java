package com.example.acer.album.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.acer.album.R;
import com.example.acer.album.models.Picture;
import com.example.acer.album.models.PictureLab;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MemoryFragment extends Fragment{
    RecyclerView memoryRecycleView;
    private int widthScreen;
    private int heigthScreen;
    MemoryAdapter adapter = null;

    public void getWithAndHeightOfScreen(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthScreen = displayMetrics.widthPixels;
        heigthScreen = displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory, container, false);
        memoryRecycleView = view.findViewById(R.id.memory_recyclerview);
        memoryRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getWithAndHeightOfScreen(getActivity());

        UpdateUI();

        return view;
    }

    public void UpdateUI(){
        if(adapter == null){
            List<String> list = PictureLab.getInstance(getActivity()).getPictureAlbum().getListKey();
            if(list != null){
                adapter = new MemoryAdapter(list);
                memoryRecycleView.setAdapter(adapter);
            }
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    public class MemoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtMemory;
        ImageView imgMemory;
        CardView cardView;
        TextView txtItemCount;

        public MemoryHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.raw_recycler_view_card, viewGroup, false));
            txtMemory = itemView.findViewById(R.id.memory_txt);
            imgMemory = itemView.findViewById(R.id.memory_img);
            cardView = itemView.findViewById(R.id.card_view);
            txtItemCount = itemView.findViewById(R.id.memory_txt_items);
        }

        public void Bind(String album){
            String[] str = album.split("/");
            txtMemory.setText(str[str.length-1]);
            int size = PictureLab.getInstance(getActivity()).getPictureAlbum().getMapPictureInAlbum(album).size();
            txtItemCount.setText(size + " items");

            List<Picture> pictures = new ArrayList<>(PictureLab.getInstance(getActivity()).getPictureAlbum().getMapPictureInAlbum(album).values());

            if(pictures != null){
                Picture picture = pictures.get(0);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.override(widthScreen, heigthScreen/3);
                requestOptions.centerCrop();
                requestOptions.placeholder(new ColorDrawable(Color.WHITE));

                Glide.with(getActivity())
                        .load(picture.getPath())
                        .apply(
                                requestOptions
                        )
                        .transition(withCrossFade())
                        .into(imgMemory);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class MemoryAdapter extends RecyclerView.Adapter<MemoryHolder>{
        List<String> list;

        public MemoryAdapter(List<String> list){
            this.list = list;
        }

        @Override
        public MemoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            return new MemoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(MemoryHolder holder, int position) {
            holder.Bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
