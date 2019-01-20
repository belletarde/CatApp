package com.app.cat.kevin.thecatapp.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.cat.kevin.thecatapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterView extends LinearLayout {

    public interface OnFilterButtonListener {
        void onFilterButtonClick(String value);
    }

    private OnFilterButtonListener onFilterButtonListener;

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


    @BindView(R.id.radio_filter_view)
    RadioGroup radioGroup;

    @OnClick(R.id.filter_view_btn)
    public void buttonFilterClick() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        onFilterButtonListener.onFilterButtonClick(radioButton.getText().toString());
    }
}