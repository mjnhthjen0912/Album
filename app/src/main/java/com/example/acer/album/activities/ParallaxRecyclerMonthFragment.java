package com.example.acer.album.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.acer.album.R;
import com.example.acer.album.models.Picture;
import com.example.acer.album.models.PictureLab;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class ParallaxRecyclerMonthFragment extends Fragment {
    private RecyclerView recyclerView;
    //private CrimeAdapter adapter;
    private static int REQUEST_CODE = 123;
    private int widthScreen;
    private int heigthScreen;
    private MonthAdapter adapter = null;
    private List<String> listMonths;
    ImageView imageHeader;
    View header;
    Random random = new Random();
    boolean isRunTimer = true;
    LinearLayout foregroundW;
    boolean isHasLayout = false;

    static final int VIEW_DETAIL_IMAGE = 231;
    int countScroll = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parallax_recycler_month, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.month_recycle_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        header = getLayoutInflater().inflate(R.layout.fragment_parallax_recycler_header, recyclerView, false);

        // set image header
        imageHeader = header.findViewById(R.id.imageHeader);
        foregroundW = header.findViewById(R.id.foregroundWhite);

        countScroll = 0;
        UpdateUI();
        foregroundW.setAlpha(0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ALPHA", "onResume");
        //UpdateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == VIEW_DETAIL_IMAGE){
            Log.d("onActivityResult", "onActivityResult");
            //UpdateUI();
        }
    }

    private void UpdateUI() {
        if (adapter == null) {
            listMonths = PictureLab.getInstance(getActivity()).getPictureMonth().getListKey();
            adapter = new MonthAdapter(listMonths);

            getWithAndHeightOfScreen(getContext());

            if (listMonths.size() > 0) {

                imageHeader.setMaxWidth(widthScreen);
                imageHeader.setMaxHeight(heigthScreen/3);

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        while(isRunTimer){
                            int size = PictureLab.getInstance(getActivity()).getPictureMonth().getListAllPicture().size();
                            int num = random.nextInt(size-1);
                            String path = PictureLab.getInstance(getContext()).getPictureMonth().getListAllPicture().get(num).getPath();

                            ShowImageAsyncTask showImageAsyncTask = new ShowImageAsyncTask(imageHeader, widthScreen, heigthScreen / 3, path, getActivity());
                            showImageAsyncTask.execute(path);

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                Timer timer = new Timer();

                timer.schedule(timerTask, 0);
            }


            adapter.setParallaxHeader(header, recyclerView);
            adapter.setData(listMonths);


            adapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {

                @Override
                public void onParallaxScroll(float v, float v1, View view) {
                    if(countScroll < 4){
                        countScroll++;
                    }
                    else if(countScroll >= 4){
                        foregroundW.setAlpha(v * 1.2f);
                    }
                }
            });

            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    public void getWithAndHeightOfScreen(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthScreen = displayMetrics.widthPixels;
        heigthScreen = displayMetrics.heightPixels;
    }


    public class MonthHolder extends RecyclerView.ViewHolder {
        public TextView txtMonthTitle;
        RecyclerView rawRecycleViewFlexBox;

        String mMonth = null;

        RawMonthAdapter rawMonthAdapter;

        public MonthHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.raw_recycle_view_flexbox, viewGroup, false));

            txtMonthTitle = (TextView) itemView.findViewById(R.id.txtMontTitle);
            rawRecycleViewFlexBox = itemView.findViewById(R.id.rawRecycleViewFlexBox);

            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            layoutManager.setAlignItems(AlignItems.STRETCH);
            rawRecycleViewFlexBox.setLayoutManager(layoutManager);
        }


        public void bind(String month) {
            // set listMonth cho 1 mMonth
            if (this.mMonth == null) {
                this.mMonth = month;

                this.txtMonthTitle.setText(this.mMonth);

                if (rawMonthAdapter == null) {
                    rawMonthAdapter = new RawMonthAdapter(PictureLab.getInstance(getActivity()).getPictureMonth().getListPictureInMonth(mMonth));
                    rawRecycleViewFlexBox.setAdapter(rawMonthAdapter);
                }
            }else {
                //rawMonthAdapter.notifyDataSetChanged();
            }
        }

        /// RawMonthHolder
        public class RawFlexBox extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView rawImageView;
            Picture mPicture;

            public RawFlexBox(LayoutInflater inflater, ViewGroup viewGroup) {
                super(inflater.inflate(R.layout.raw_flexbox, viewGroup, false));
                rawImageView = (ImageView) itemView.findViewById(R.id.rawImageView);
                itemView.setOnClickListener(this);
            }

            public void bind(Picture picture, int width, int height) {

                if (mPicture == null) {
                    this.mPicture = picture;


//                    Picasso.get()
//                            .load(new File(picture.getPath()))
//                            .error(R.mipmap.ic_launcher)
//                            .placeholder(new ColorDrawable(Color.WHITE))
//                            .resize(width, height)
//                            .into(rawImageView);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.override(width, height);
                    requestOptions.centerCrop();
                    requestOptions.placeholder(new ColorDrawable(Color.WHITE));

                    Glide.with(getActivity())
                            .load(picture.getPath())
                            .apply(
                                    requestOptions
                            )
                            .into(rawImageView);

                    //ShowImageAsyncTask showImageAsyncTask = new ShowImageAsyncTask(rawImageView, width, height, 0);
                    //showImageAsyncTask.execute(picture.getPath());

                    //Log.d("IMAGE", "bind: "+picture.getPath());
                }
            }

            @Override
            public void onClick(View view) {
                // open new activity
                Intent intent = PicturePaperActivity.newIntent(getActivity(), mPicture.getId());
                startActivityForResult(intent, VIEW_DETAIL_IMAGE);
                //startActivity(intent);
            }
        }



        /// RawMonthAdapter
        public class RawMonthAdapter extends RecyclerView.Adapter<RawFlexBox> {
            List<Picture> listPicture;
            int numberImageInRow = 4;

            List<Integer> listSize;
            int heightImage = heigthScreen / 7;

            public RawMonthAdapter(List<Picture> pictures) {
                this.listPicture = pictures;
                this.listSize = new ArrayList<>(listPicture.size());
                CalculatorSize();
            }

            private void CalculatorSize() {
                int n = listPicture.size() / numberImageInRow;
                int du = listPicture.size() % numberImageInRow;

                int space = (dpToPx(1));
                int i = 0;
                for (i = 0; i < n; i++) {
                    int nJ = i * numberImageInRow + numberImageInRow;
                    int sumWidth = 0;
                    for (int j = i * numberImageInRow; j < nJ; j++) {
                        int width = Math.round(heightImage / (listPicture.get(j).getHeight() * 1.0f / listPicture.get(j).getWidth()));
                        listSize.add(width);
                        sumWidth += width;
                    }


                    int widthAdd = 0;
                    widthAdd = widthScreen - sumWidth -dpToPx(1);
                    widthAdd = Math.round(widthAdd * 1.0f / numberImageInRow);

                    for (int j = i * numberImageInRow; j < nJ; j++) {
                        int tem = listSize.get(j) + widthAdd;
                        tem -= space;
                        listSize.set(j, tem);
                    }
                }

                //Log.d("DEBUG", listSize.size()+" size");

                // số dư còn lại
                int idxCurrent = i * numberImageInRow;
                int sumWidth = 0;
                for (int k = 0; k < du; k++) {
                    int width = Math.round(heightImage / (listPicture.get(k + idxCurrent).getHeight() * 1.0f / listPicture.get(k + idxCurrent).getWidth()));
                    listSize.add(width);
                    sumWidth += width;
                }

                int widthAdd = 0;
                if (widthScreen < (sumWidth)) {
                    widthAdd = widthScreen - (sumWidth + space);
                    widthAdd = Math.round(widthAdd * 1.0f / du);

                    for (int k = 0; k < du; k++) {
                        int tem = listSize.get(k + idxCurrent) + widthAdd;
                        listSize.set(k + idxCurrent, tem);
                    }
                }
            }

            @Override
            public RawFlexBox onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

                return new RawFlexBox(layoutInflater, parent);
            }

            @Override
            public void onBindViewHolder(RawFlexBox holder, int position) {
                ViewGroup.LayoutParams lp = holder.rawImageView.getLayoutParams();
                if (lp instanceof FlexboxLayoutManager.LayoutParams) {
                    FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams)
                            holder.rawImageView.getLayoutParams();
                    flexboxLp.setFlexGrow(1.0f);
                }


                holder.bind(listPicture.get(position), listSize.get(position), heightImage);
            }


            @Override
            public int getItemCount() {
                return listPicture.size();
            }

            public int dpToPx(int dp) {
                DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
                return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            }
        }
    }

    public class MonthAdapter extends ParallaxRecyclerAdapter<String> {
        private List<String> listMonth;

        public MonthAdapter(List<String> listMonth) {
            super(listMonth);
            this.listMonth = listMonth;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter, int i) {
            ((MonthHolder) viewHolder).bind(listMonth.get(i));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MonthHolder(layoutInflater, viewGroup);
        }

        @Override
        public int getItemCountImpl(ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter) {
            return this.getData().size();
        }

    }
}


