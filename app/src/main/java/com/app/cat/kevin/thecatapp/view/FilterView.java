package com.app.cat.kevin.thecatapp.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.app.cat.kevin.thecatapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterView extends LinearLayout {

    public interface OnFilterButtonListener {
        void onFilterButtonClick(int value);
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }

    private OnFilterButtonListener onFilterButtonListener;
    private OnCancelClickListener onCancelClickListener;

    public FilterView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FilterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.filter_view, this);
        ButterKnife.bind(this);
    }

    public void setOnFilterButtonListener(OnFilterButtonListener onFilterButtonListener) {
        this.onFilterButtonListener = onFilterButtonListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }


    @BindView(R.id.radio_filter_view)
    RadioGroup radioGroup;

    @OnClick(R.id.filter_view_btn_cancel)
    public void buttonCancelClick() {
        onCancelClickListener.onCancelClick();
    }

    @OnClick(R.id.filter_view_btn_start)
    public void buttonFilterClick() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        int color = catColorSelected(selectedId);
        onFilterButtonListener.onFilterButtonClick(color);
    }

    private int catColorSelected(int selectedId) {
        switch (selectedId) {
            case R.id.filter_view_radio_black:
                return ContextCompat.getColor(getContext(), R.color.cat_tag_black);
            case R.id.filter_view_radio_brown:
                return ContextCompat.getColor(getContext(), R.color.cat_tag_brown);
            case R.id.filter_view_radio_gray:
                return ContextCompat.getColor(getContext(), R.color.cat_tag_gray);
            case R.id.filter_view_radio_white:
                return ContextCompat.getColor(getContext(), R.color.cat_tag_white);
            default:
                return 0;
        }
    }
}