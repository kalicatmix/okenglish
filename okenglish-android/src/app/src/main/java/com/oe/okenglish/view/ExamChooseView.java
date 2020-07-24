package com.oe.okenglish.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class ExamChooseView extends AppCompatTextView {
    public static final int ICON_CORRECT = 0;
    public static final int ICON_WRONG = 1;

    public ExamChooseView(Context context) {
        super(context);
    }

    public ExamChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExamChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAnswerIcon(int type) {
        String color = "#D81B60";
        if (type == ICON_CORRECT) color = "#008577";
        setTextColor(Color.parseColor(color));
    }
}
