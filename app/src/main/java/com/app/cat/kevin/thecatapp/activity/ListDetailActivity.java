package com.app.cat.kevin.thecatapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cat.kevin.thecatapp.R;
import com.app.cat.kevin.thecatapp.api.service.CatApiService;
import com.app.cat.kevin.thecatapp.model.Breed;
import com.app.cat.kevin.thecatapp.model.Cat;
import com.app.cat.kevin.thecatapp.view.ConnectionErrorView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.ResponseBody;

public class ListDetailActivity extends AppCompatActivity implements ConnectionErrorView.OnTryAgainButtonListener{

    public final static int LVL_0_SIZE = 10;
    public final static int LVL_1_SIZE = 50;
    public final static int LVL_2_SIZE = 100;
    public final static int LVL_3_SIZE = 150;
    public final static int LVL_4_SIZE = 200;
    public final static int LVL_5_SIZE = 250;
    public final static String USER_ID = "id";
    private String id;
    private CatApiService catApiService = new CatApiService();

    @NonNull
    private CompositeDisposable catCompositeDisposable = new CompositeDisposable();

    @BindView(R.id.cat_description)
    public TextView catDescription;

    @BindView(R.id.cat_detail_img)
    public ImageView catPic;

    @BindView(R.id.view_connection)
    public ConnectionErrorView connectionErrorView;

    @BindView(R.id.cat_detail_progress)
    public ProgressBar catDetailProgress;

    @BindView(R.id.cat_detail_content)
    public ScrollView catDetailLayout;

    @BindView(R.id.adaptability)
    public View adaptability;

    @BindView(R.id.adaptability_level)
    public TextView adaptabilityLevel;

    @BindView(R.id.affection)
    public View affection;

    @BindView(R.id.affection_level)
    public TextView affectionLevel;

    @BindView(R.id.child_friendly)
    public View childFriendly;

    @BindView(R.id.child_friendly_level)
    public TextView childFriendlyLevel;

    @BindView(R.id.dog_friendly)
    public View dogFriendly;

    @BindView(R.id.dog_friendly_level)
    public TextView dogFriendlyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra(USER_ID);
        setSupport();
        getCatDetail();
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

    private void setSupport() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void getCatDetail() {
        catDetailProgress.setVisibility(View.VISIBLE);
        catCompositeDisposable.add(catApiService.getCatById(id).subscribe(
                this::detailSuccessResponse,
                this::detailErrorResponse));

    }

    private void getCatColor(Cat cat) {
        catCompositeDisposable.add(catApiService.downloadCatImage(cat.getUrl()).subscribe(
                this::catImageDownloadSuccess,
                this::catImageDownloadError
        ));
    }

    private void catImageDownloadSuccess(ResponseBody body) throws IOException {
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        String base64 = Base64.encodeToString(body.bytes(), Base64.DEFAULT);
        Toast.makeText(this, "ID:"+id+"BASE64  "+base64, Toast.LENGTH_SHORT).show();
        Log.e("###test64", base64);

//        byte[] buffer = body.bytes();
//        byteBuffer.write(buffer, 0, (int) body.contentLength());
//        bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }

    private void catImageDownloadError(Throwable throwable) {
        Toast.makeText(this, ""+throwable, Toast.LENGTH_SHORT).show();
    }

    public void detailSuccessResponse(Cat response) {
        Animation slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        catDetailLayout.startAnimation(slideRight);
        successProgress();
        getCatColor(response);
        String title = getString(R.string.app_name);
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

    private void successProgress() {
        catDetailProgress.setVisibility(View.GONE);
        catDetailLayout.setVisibility(View.VISIBLE);
        connectionErrorView.setVisibility(View.GONE);
    }

    private void errorProgress() {
        catDetailProgress.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.VISIBLE);
    }

    private void tryAgainProgress() {
        catDetailProgress.setVisibility(View.VISIBLE);
        connectionErrorView.setVisibility(View.GONE);
    }

    public void detailErrorResponse(Throwable throwable) {
        errorProgress();
        connectionErrorView.setOnTryAgainButtonListener(this);
    }

    private String breedInfo(Breed breed) {
        catDescription.setText(breed.getDescription());
        catDiagram(adaptability, breed.getAdaptability(), adaptabilityLevel);
        catDiagram(affection, breed.getAffectionLevel(), affectionLevel);
        catDiagram(childFriendly, breed.getChildFriendly(), childFriendlyLevel);
        catDiagram(dogFriendly, breed.getDogFriendly(), dogFriendlyLevel);
        return breed.getName();
    }

    private void catDiagram(View adaptability, int breedProperties, TextView textView) {
        textView.setText(getString(R.string.breed_detail_value, breedProperties));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) adaptability.getLayoutParams();
        params.width = widthDiagramSize(breedProperties);
        adaptability.setLayoutParams(params);
    }

    private int widthDiagramSize(int count) {
        switch(count) {
            case 0:
                return LVL_0_SIZE;
            case 1:
                return LVL_1_SIZE;
            case 2:
                return LVL_2_SIZE;
            case 3:
                return LVL_3_SIZE;
            case 4:
                return LVL_4_SIZE;
            case 5:
                return LVL_5_SIZE;
            default:
                return LVL_0_SIZE;
        }
    }

    @Override
    public void onTryAgainButtonClick() {
        getCatDetail();
        tryAgainProgress();
    }

}
