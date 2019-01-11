package com.app.cat.kevin.thecatapp.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cat.kevin.thecatapp.R;
import com.app.cat.kevin.thecatapp.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionErrorView extends LinearLayout {

    public interface OnTryAgainButtonListener {
        void onTryAgainButtonClick();
    }


    private OnTryAgainButtonListener onTryAgainButtonListener;


    public ConnectionErrorView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ConnectionErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ConnectionErrorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.connection_error_view, this);
        ButterKnife.bind(this);
    }

    public void setOnTryAgainButtonListener(OnTryAgainButtonListener onTryAgainButtonListener) {
        this.onTryAgainButtonListener = onTryAgainButtonListener;
    }


    @OnClick(R.id.txt_try_again)
    public void buttonTryAgainClick() {
        onTryAgainButtonListener.onTryAgainButtonClick();
    }
}