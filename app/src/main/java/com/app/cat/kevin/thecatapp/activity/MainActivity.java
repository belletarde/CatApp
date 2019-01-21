package com.app.cat.kevin.thecatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.app.cat.kevin.thecatapp.R;
import com.app.cat.kevin.thecatapp.adapter.CatListAdapter;
import com.app.cat.kevin.thecatapp.api.service.CatApiService;
import com.app.cat.kevin.thecatapp.model.Cat;
import com.app.cat.kevin.thecatapp.model.FavouriteRequest;
import com.app.cat.kevin.thecatapp.model.FavouriteResponse;
import com.app.cat.kevin.thecatapp.view.ConnectionErrorView;
import com.app.cat.kevin.thecatapp.view.FilterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

import static com.app.cat.kevin.thecatapp.activity.ListDetailActivity.USER_ID;


public class MainActivity extends AppCompatActivity implements CatListAdapter.OnListClick,
        CatListAdapter.OnLikeClick, ConnectionErrorView.OnTryAgainButtonListener, FilterView.OnFilterButtonListener, FilterView.OnCancelClickListener
        {

    @BindView(R.id.cat_list)
    RecyclerView catRecyclerView;

    @BindView(R.id.list_first_loading)
    ProgressBar listProgress;

    @BindView(R.id.view_connection_error)
    ConnectionErrorView connectionErrorView;

    @BindView(R.id.filter_view)
    FilterView filterView;

    private static final int INITIAL_PAGE = 12;
    private CompositeDisposable catCompositeDisposable = new CompositeDisposable();
    private boolean loading = false;
    private int pastVisibleItems, visibleItemCount, totalItemCount, page = INITIAL_PAGE;
    private List<Cat> catList = new ArrayList<>();
    private CatListAdapter adapter;
    private int positionLiked;
    private Menu mainMenu;
    private CatApiService catApiService = new CatApiService();
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(
                this, OrientationHelper.VERTICAL, false);

        fetchCatList();
    }

    public void fetchCatList() {
        catCompositeDisposable.add(catApiService.getCatList(page).subscribe(
                this::catListSuccessResponse,
                this::catListErrorResponse));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.filter_layout, menu);
        mainMenu = menu;
        return true;
    }


    private void setFilterVisible() {
        mainMenu.setGroupVisible(R.id.filter_group, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_filter) {
            if(filterView.getVisibility() == View.VISIBLE ) {
                animDown(filterView);
                filterView.setVisibility(View.GONE);
                animUp(catRecyclerView);
                initCatRecycler(catList);
                loading = false;
            } else {
                animDown(catRecyclerView);
                catRecyclerView.setVisibility(View.GONE);
                animUp(filterView);
                filterView.setVisibility(View.VISIBLE);
                filterView.setOnFilterButtonListener(this::onFilterButtonClick);
                filterView.setOnCancelClickListener(this::onCancelClick);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void catListSuccessResponse(List<Cat> catListResponse) {
        setFilterVisible();
        int catListSize = catListResponse.size();
        for (int i = 0; i < catListSize; i++) {
            catListResponse.get(i).setTag(randomColor());
        }

        recyclerAdapterLoader(catListResponse);
    }

    public void setCatList(List<Cat> catList) {
        this.catList.addAll(catList);
    }

    private void animUp(View view) {
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        view.startAnimation(slideUp);
    }

    private void animDown(View view) {
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        view.startAnimation(slideDown);
    }

    private void initCatRecycler(List<Cat> cats) {

        animUp(catRecyclerView);
        initRecyclerProgress();
        adapter = new CatListAdapter(
                this,
                cats,
                this::onCatListClickListener,
                this::onLikeClickListener);
        catRecyclerView.setLayoutManager(layoutManager);
        catRecyclerView.setAdapter(adapter);
    }

    public void like(FavouriteRequest favouriteRequest) {
        catCompositeDisposable.add(catApiService.likeCat(favouriteRequest).subscribe(
                this::favouriteResponseSuccess,
                this::favouriteResponsError));
    }

    private void recyclerScrollListener(LinearLayoutManager layoutManager) {
        catRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0)
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (!loading)
                    {
                        if ( (visibleItemCount + pastVisibleItems) >= totalItemCount)
                        {
                            loading = true;
                            startSnack(getString(R.string.cat_loader_txt));
                            page++;
                            fetchCatList();
                        }
                    }
                }
            }
        });
    }

    private void initRecyclerProgress() {
        listProgress.setVisibility(View.GONE);
        connectionErrorView.setVisibility(View.GONE);
        catRecyclerView.setVisibility(View.VISIBLE);
    }

    private void favouriteResponsError(Throwable throwable) {
        if (throwable.getMessage().contains("400")) {
            successFavorite(getString(R.string.error_already_favourite));
            return;
        }
        startSnack(getString(R.string.error_on_favorite));
    }

    private void favouriteResponseSuccess(FavouriteResponse favouriteResponse) {
        successFavorite(getString(R.string.success_favourite));
    }

    private void successFavorite(String snackText) {
        startSnack(snackText);
        catList.get(positionLiked).setLike(true);
        adapter.notifyDataSetChanged();
    }

    private void startSnack(String infoData) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, infoData, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.snack_btn_title), view -> {
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_purple ))
                .show();
    }

    public void catListErrorResponse(Throwable throwable) {
        listProgress.setVisibility(View.GONE);
        loading = false;
        if(catList.size() <= 0) {
            connectionErrorView.setVisibility(View.VISIBLE);
            connectionErrorView.setOnTryAgainButtonListener(this);
            return;
        }

        startSnack(getString(R.string.on_infinity_loading_error));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!catCompositeDisposable.isDisposed()) {
            catCompositeDisposable.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!catCompositeDisposable.isDisposed()) {
            catCompositeDisposable.clear();
        }
    }

    @Override
    public void onCatListClickListener(String id) {
        Intent intent = new Intent(this, ListDetailActivity.class);
        intent.putExtra(USER_ID, id);
        startActivity(intent);
    }

    @Override
    public void onLikeClickListener(String id, int position) {
        startSnack(getString(R.string.on_like_click_message));
        positionLiked = position;
        FavouriteRequest favouriteRequest = new FavouriteRequest(id, getResources().getString(R.string.api_cat_user_id));
        like(favouriteRequest);
    }

    @Override
    public void onTryAgainButtonClick() {
        fetchCatList();
        listProgress.setVisibility(View.VISIBLE);
        connectionErrorView.setVisibility(View.GONE);
    }

    @Override
    public void onFilterButtonClick(int radioValue) {
        animDown(filterView);
        filterView.setVisibility(View.GONE);
        animUp(catRecyclerView);
        if(radioValue != 0) {
            loading = true;
            List<Cat> newFilteredCatList = new ArrayList<>();
            for (int i = 0; i < catList.size(); i++) {
                Cat cat = catList.get(i);
                if (cat.getTag() == radioValue) {
                    newFilteredCatList.add(cat);
                }
            }

            if(newFilteredCatList.size() > 0){
                initCatRecycler(newFilteredCatList);
                return;
            } else {
                startSnack("No cats found with selected tag");
                loading = false;
                initCatRecycler(catList);
            }
        }
    }

    private void recyclerAdapterLoader(List<Cat> catListResponse) {
        loading = false;
        setCatList(catListResponse);

        if(page > INITIAL_PAGE) {
            adapter.notifyDataSetChanged();
        } else {
            initCatRecycler(catList);
        }
        recyclerScrollListener(layoutManager);
    }

    private int random() {
        Random r = new Random();
        return  r.nextInt(5 - 1);
    }

    private int randomColor() {
        switch (random()) {
            case 1:
                return ContextCompat.getColor(this, R.color.cat_tag_brown);
            case 2:
                return ContextCompat.getColor(this, R.color.cat_tag_gray);
            case 3:
                return ContextCompat.getColor(this, R.color.cat_tag_black);
            default:
                return ContextCompat.getColor(this, R.color.cat_tag_white);
        }
    }

    @Override
    public void onCancelClick() {
        animDown(filterView);
        filterView.setVisibility(View.GONE);
        animUp(catRecyclerView);
        initCatRecycler(catList);
        loading = false;
    }

}

