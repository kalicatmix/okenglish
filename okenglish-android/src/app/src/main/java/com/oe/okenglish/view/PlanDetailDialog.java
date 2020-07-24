package com.oe.okenglish.view;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.oe.okenglish.R;
import com.oe.okenglish.application.OKApplication;
import com.oe.okenglish.callback.CommonCallback;
import com.oe.okenglish.entity.Plan;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.kit.OkHttpClientKit;
import com.oe.okenglish.res.Res;

import okhttp3.Response;

public class PlanDetailDialog extends Dialog {
    public static interface PlanDelegation {
        public void update();
    }

    public static class PlanDelegate {
        PlanDelegation delegation;

        public void delegate(PlanDelegation delegation) {
            this.delegation = delegation;
        }

        public void update() {
            delegation.update();
        }
    }

    private PlanDelegate planDelegate;

    public PlanDetailDialog(@NonNull Context context, Plan plan) {
        super(context);
        OKApplication application = (OKApplication) context.getApplicationContext();
        planDelegate = new PlanDelegate();
        planDelegate.delegate(application.getPlanDelegation());
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_plan_detail, null);
        ImageView imageView = view.findViewById(R.id.dialog_plan_detail_img);
        TextView textView = view.findViewById(R.id.dialog_plan_detail_text);
        FloatingActionButton addBtn = view.findViewById(R.id.dialog_plan_detail_btn);
        imageView.setImageDrawable(plan.getDrawable());
        textView.setText(plan.getName());
        if ((application.getUser().getPlan() == Integer.parseInt(plan.getDbName()))) {
            addBtn.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_selected));
            addBtn.setEnabled(false);
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.getUser().setPlan(Integer.parseInt(plan.getDbName()));
                addBtn.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_selected));
                addBtn.setEnabled(false);
                ContentValues values = new ContentValues();
                values.put("id", application.getUser().getId());
                values.put("name", application.getUser().getName());
                values.put("plan", application.getUser().getPlan());
                values.put("dayWord", application.getUser().getDayWord());
                application.getContentResolver().update(Uri.parse(Res.USER_CONTENT_URL), values, "id=?", new String[]{AppKit.toString(application.getUser().getId())});
                planDelegate.update();
                AppKit.log("token", GeneratorTool.getTokenHeader(application));
                OkHttpClientKit.getInstance().post(ApiKit.URL_REMOTE_HOST + GeneratorTool.replaceStr(ApiKit.URL_USER_PLAN_SYNC, "?", application.getUser().getName()), "plan=" + plan.getDbName(), GeneratorTool.getTokenHeader(application)).enqueue(new CommonCallback() {
                    @Override
                    public void call(Object data) {
                        AppKit.log("sync plan code", ((Response) data).code());
                    }
                });
                Snackbar.make(v, "添加成功", 3000).setTextColor(context.getColor(R.color.colorAccent)).show();
            }
        });
        setContentView(view);
    }
}
