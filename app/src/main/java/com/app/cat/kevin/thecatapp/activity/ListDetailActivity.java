package com.app.cat.kevin.thecatapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cat.kevin.thecatapp.R;
import com.app.cat.kevin.thecatapp.api.service.CatApiService;
import com.app.cat.kevin.thecatapp.model.Breed;
import com.app.cat.kevin.thecatapp.model.Cat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.neo.arcchartview.ArcChartView;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

public class ListDetailActivity extends AppCompatActivity {

    public final static String USER_ID = "id";

    @BindView(R.id.cat_name)
    public TextView catname;

    @BindView(R.id.cat_description)
    public TextView catDescription;

    @BindView(R.id.cat_detail_img)
    public ImageView catPic;

    @NonNull
    private CompositeDisposable catCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);
        ButterKnife.bind(this);

        String id = getIntent().getStringExtra(USER_ID);
        setSupport();
        getCatDetail(id);
    }

    private void setSupport() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (catCompositeDisposable != null && !catCompositeDisposable.isDisposed()) {
            catCompositeDisposable.clear();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (catCompositeDisposable != null && !catCompositeDisposable.isDisposed()) {
            catCompositeDisposable.clear();
        }
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    public void getCatDetail(String id) {
        CatApiService catApiService = new CatApiService();
        catCompositeDisposable.add(catApiService.getCatById(id).subscribe(
                this::detailSuccessResponse,
                this::detailErrorResponse));

    }

    public void detailSuccessResponse(Cat response) {
        String title = "Cat";
        if(response.getBreed().size() > 0) {
            title = breedInfo(response.getBreed().get(0));
        }
        getSupportActionBar().setTitle(title);
        Glide
                .with(this)
                .load(response.getUrl())
                .apply(new RequestOptions().centerCrop())
                .into(catPic);
    }

    public void detailErrorResponse(Throwable throwable) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    public String breedInfo(Breed breed) {
        catDescription.setText(breed.getDescription());
        catname.setText(breed.getName());
        return breed.getName();
    }
}
