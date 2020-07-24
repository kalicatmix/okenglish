package com.oe.okenglish.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.oe.okenglish.R;
import com.oe.okenglish.application.OKApplication;
import com.oe.okenglish.entity.Word;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.provider.db.WordService;
import com.oe.okenglish.view.ExamChooseView;

import java.util.ArrayList;

public class OkEnglishExam extends AppCompatActivity {
    private ArrayList<Word> words;
    private int current = 0, correctCount = 0, wrongCount = 0, correctIndex = 0;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        setContentView(R.layout.ok_exam);
        initView();
    }

    private void initData() {
        words = new ArrayList<>();
        OKApplication okApplication = (OKApplication) getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Integer> wordId = WordService.getInstance(okApplication).queryAllWordByPlanId(okApplication.getUser().getPlan());
                for (int i = 0; i < 100; i++) {
                    words.add(WordService.getInstance(okApplication).queryWordById(wordId.get(GeneratorTool.generateRandomNumber(0, wordId.size()))));
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void initView() {
        TextView backBtn = findViewById(R.id.toolbar_back_btn);
        CardView wordCard = findViewById(R.id.ok_exam_card), resultCard = findViewById(R.id.ok_exam_result);
        TextView wordText = findViewById(R.id.ok_exam_word);
        ExamChooseView itemA = findViewById(R.id.ok_exam_item_a), itemB = findViewById(R.id.ok_exam_item_b), itemC = findViewById(R.id.ok_exam_item_c), itemD = findViewById(R.id.ok_exam_item_d);
        TextView submit = findViewById(R.id.ok_exam_submit), next = findViewById(R.id.ok_exam_next);
        TextView correctText = findViewById(R.id.exam_correct_text), wrongText = findViewById(R.id.exam_wrong_text), analyText = findViewById(R.id.exam_analysis_text);
        View.OnClickListener opClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ok_exam_submit:
                        wordCard.setVisibility(View.GONE);
                        resultCard.setVisibility(View.VISIBLE);
                        correctText.setText("你答对了" + correctCount + "个单词");
                        wrongText.setText("你答错了" + wrongCount + "个单词");
                        float factorOne = 50000 * (correctCount + wrongCount) / 100;
                        float factorTwo = (float) correctCount / (float) (correctCount + wrongCount);
                        int wordSize = (int) (factorOne * factorTwo);
                        analyText.setText("评估词汇量:" + wordSize);
                        break;
                    case R.id.ok_exam_next:
                        operateAllItem(new ExamChooseView[]{itemA, itemB, itemC, itemD}, false);
                        current++;
                        if (current > words.size()) {
                            Snackbar.make(backBtn, "评测已结束", 1000).show();
                        }
                        wordText.setText(words.get(current).getWord());
                        fillItemText(new ExamChooseView[]{itemA, itemB, itemC, itemD});
                        break;
                }
            }
        };
        submit.setOnClickListener(opClick);
        next.setOnClickListener(opClick);
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    wordText.setText(words.get(current).getWord());
                    fillItemText(new ExamChooseView[]{itemA, itemB, itemC, itemD});
                }
            }
        };
        View.OnClickListener itemClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 0;
                ExamChooseView chooseView = itemA;
                switch (v.getId()) {
                    case R.id.ok_exam_item_a:
                        break;
                    case R.id.ok_exam_item_b:
                        index = 1;
                        chooseView = itemB;
                        break;
                    case R.id.ok_exam_item_c:
                        index = 2;
                        chooseView = itemC;
                        break;
                    case R.id.ok_exam_item_d:
                        index = 3;
                        chooseView = itemD;
                        break;
                }
                if (index == correctIndex) {
                    correctCount++;
                    chooseView.setAnswerIcon(ExamChooseView.ICON_CORRECT);
                } else {
                    wrongCount++;
                    chooseView.setAnswerIcon(ExamChooseView.ICON_WRONG);
                }
                operateAllItem(new ExamChooseView[]{itemA, itemB, itemC, itemD}, true);
            }
        };
        itemA.setOnClickListener(itemClick);
        itemB.setOnClickListener(itemClick);
        itemC.setOnClickListener(itemClick);
        itemD.setOnClickListener(itemClick);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OkEnglish.class));
            }
        });
        initData();

    }

    private void operateAllItem(ExamChooseView view[], boolean disable) {
        for (ExamChooseView v : view) {
            if (disable) v.setEnabled(false);
            else v.setEnabled(true);
        }
    }

    private void fillItemText(ExamChooseView view[]) {
        correctIndex = GeneratorTool.generateRandomNumber(0, view.length);
        String wordStr = words.get(current).getTranslate(), otherWord = "";
        for (int i = 0; i < view.length; i++) {
            view[i].setTextColor(Color.BLACK);
            if (i == correctIndex) {
                view[i].setText(wordStr);
            } else {
                while (wordStr.equals((otherWord = words.get(GeneratorTool.generateRandomNumber(0, words.size())).getTranslate())))
                    ;
                view[i].setText(otherWord);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
