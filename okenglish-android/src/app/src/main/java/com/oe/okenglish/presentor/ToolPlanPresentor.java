package com.oe.okenglish.presentor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oe.okenglish.R;
import com.oe.okenglish.entity.Plan;

public class ToolPlanPresentor implements Presentor<ToolPlanPresentor> {
    private static View view;
    private static ToolPlanPresentor toolPlanPresentor;
    private static Plan plans[];
    private ToolPlanPresentor.PlanViewAdaptor planViewAdaptor;
    private ToolPlanPresentor.ToolPlanClickListener listener;

    private ToolPlanPresentor() {
    }

    public Plan[] getPlans() {
        return plans;
    }

    public ToolPlanPresentor setPlans(Plan[] plans) {
        this.plans = plans;
        return this;
    }

    private void addPlanViewAdaptor() {
        planViewAdaptor = new ToolPlanPresentor.PlanViewAdaptor(listener);
        ((RecyclerView) view).setAdapter(planViewAdaptor);
    }

    public ToolPlanPresentor setReaderViewAdaptor(RecyclerView.Adapter planViewAdaptor) {
        this.planViewAdaptor = (ToolPlanPresentor.PlanViewAdaptor) planViewAdaptor;
        addPlanViewAdaptor();
        return this;
    }

    public ToolPlanPresentor setReaderViewAdaptor() {
        if (this.plans != null) addPlanViewAdaptor();
        return this;
    }

    public ToolPlanPresentor addOnItemClickListener(ToolPlanPresentor.ToolPlanClickListener listener) {
        this.listener = listener;
        return this;
    }

    public static ToolPlanPresentor newPresentor() {
        synchronized (ToolReaderPresentor.class) {
            if (toolPlanPresentor == null) toolPlanPresentor = new ToolPlanPresentor();
        }
        return toolPlanPresentor;
    }

    public ToolPlanPresentor setView(@NonNull View view) {
        this.view = view;
        return this;
    }

    @Override
    public ToolPlanPresentor update() {
        planViewAdaptor.notifyDataSetChanged();
        return this;
    }

    private static class PlanViewHolder extends RecyclerView.ViewHolder {
        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private static class PlanViewAdaptor extends RecyclerView.Adapter {
        private ToolPlanPresentor.ToolPlanClickListener listener;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool_plan,null, false);
            return new ToolPlanPresentor.PlanViewHolder(item);
        }

        PlanViewAdaptor(ToolPlanClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            View item = holder.itemView;
            Plan plan = plans[position];
            TextView textView=item.findViewById(R.id.tool_plan_text);
            ImageView imageView=item.findViewById(R.id.tool_plan_img);
            textView.setText(plan.getName());
            imageView.setImageDrawable(plan.getDrawable());
            item.setTag(position);
            item.setOnClickListener(listener);
        }

        @Override
        public int getItemCount() {
            return plans.length;
        }
    }

    public static abstract class ToolPlanClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onItemClick(plans[(int) v.getTag()]);
        }

        public abstract void onItemClick(Plan plan);
    }
}
