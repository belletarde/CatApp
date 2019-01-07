package com.app.cat.kevin.thecatapp.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cat.kevin.thecatapp.R;
import com.app.cat.kevin.thecatapp.api.service.CatApiService;
import com.app.cat.kevin.thecatapp.model.Cat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

public class ListDetailActivity extends AppCompatActivity {

    public final static String USER_ID = "id";

    @BindView(R.id.name)
    public TextView name;

    @BindView(R.id.img_src)
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
        getSupportActionBar().setTitle(response.getId());
        Glide
                .with(this)
                .load(response.getUrl())
                .apply(new RequestOptions().centerCrop())
                .into(catPic);

    }

    public void detailErrorResponse(Throwable throwable) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

    }
}
