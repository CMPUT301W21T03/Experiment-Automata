package com.example.experiment_automata.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.experiment_automata.R;

/**
 * Extends the TextView to add a uuid attribute
 */
public class LinkView extends androidx.appcompat.widget.AppCompatTextView {
    public LinkView(Context context) {
        super(context);
        init(null, 0);
    }

    public LinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LinkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.LinkView, defStyle, 0);

        a.recycle();
    }
}