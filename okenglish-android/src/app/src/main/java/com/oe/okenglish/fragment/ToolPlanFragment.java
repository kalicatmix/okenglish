package com.oe.okenglish.fragment;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.oe.okenglish.R;
import com.oe.okenglish.application.OKApplication;
import com.oe.okenglish.entity.Plan;
import com.oe.okenglish.entity.Word;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.presentor.ToolPlanPresentor;
import com.oe.okenglish.res.Res;
import com.oe.okenglish.view.PlanDetailDialog;

import java.util.ArrayList;

public class ToolPlanFragment extends Fragment {
    public static class Plans {
        public static final String PETS = "  公共英语  ";
        public static final String PROFESSOR = "  职称英语  ";
        public static final String GRE = "  GRE英语  ";
        public static final String TOEFL = "  托福英语  ";
        public static final String COMPUTER = " 计算机英语 ";
        public static final String BUSINESS = "  商业英语  ";
        public static final String POSTGRADUATE = " 研究生英语 ";
        public static final String IELTS = "  雅思英语  ";
        public static final String FINANIAL = "  金融英语  ";
        public static final String GMAT = " GMAT英语 ";
        public static final String DOCOTOR = "  考博英语  ";
        public static final String MBA = "  MBA英语  ";
    }

    private final int COLUMN_ITEM = 3;
    public static final int TYPE_PETS = 1;
    public static final int TYPE_PROFESSOR = 2;
    public static final int TYPE_GRE = 3;
    public static final int TYPE_TOEFL = 4;
    public static final int TYPE_COMPUTER = 5;
    public static final int TYPE_BUSINESS = 6;
    public static final int TYPE_POSTGRADUATE = 7;
    public static final int TYPE_IELTS = 8;
    public static final int TYPE_FINANIAL = 9;
    public static final int TYPE_GMAT = 10;
    public static final int TYPE_DOCTOR = 11;
    public static final int TYPE_MBA = 12;
    public static final int TYPE_NULL = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_plan, container, false);
        RecyclerView plans = view.findViewById(R.id.tool_plan);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), COLUMN_ITEM);
        plans.setLayoutManager(gridLayoutManager);
        plans.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = (int) (getActivity().getResources().getDisplayMetrics().density * 10);
                outRect.right = 0;
            }
        });
        Dialog dialog = new Dialog(getContext());
        View favoriteDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_favorite_show, null, false);
        RecyclerView list = favoriteDialog.findViewById(R.id.plan_favorite_dialog_list);
        FavoriteItemAdaptor favoriteItemAdaptor = new FavoriteItemAdaptor(new DiffUtil.ItemCallback() {
            @Override
            public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return false;
            }
        }, getActivity().getApplicationContext());
        list.setAdapter(favoriteItemAdaptor);
        Point display = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getRealSize(display);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(display.x,display.y/3*2);
        dialog.setContentView(favoriteDialog, params);
        ToolPlanPresentor.newPresentor().setPlans(GeneratorTool.generatePlan(getContext(), new String[][]{
                {Plans.PETS, toString(TYPE_PETS), toString(R.drawable.ic_pets)},
                {Plans.PROFESSOR, toString(TYPE_PROFESSOR), toString(R.drawable.ic_professor)},
                {Plans.GRE, toString(TYPE_GRE), toString(R.drawable.ic_gre)},
                {Plans.TOEFL, toString(TYPE_TOEFL), toString(R.drawable.ic_toefl)},
                {Plans.COMPUTER, toString(TYPE_COMPUTER), toString(R.drawable.ic_computer)},
                {Plans.BUSINESS, toString(TYPE_BUSINESS), toString(R.drawable.ic_business)},
                {Plans.POSTGRADUATE, toString(TYPE_POSTGRADUATE), toString(R.drawable.ic_postgraduate)},
                {Plans.IELTS, toString(TYPE_IELTS), toString(R.drawable.ic_ielts)},
                {Plans.FINANIAL, toString(TYPE_FINANIAL), toString(R.drawable.ic_financial)},
                {Plans.GMAT, toString(TYPE_GMAT), toString(R.drawable.ic_gmat)},
                {Plans.DOCOTOR, toString(TYPE_DOCTOR), toString(R.drawable.ic_doctor)},
                {Plans.MBA, toString(TYPE_MBA), toString(R.drawable.ic_mba)},
                {"  我的收藏  ", toString(TYPE_NULL), toString(R.drawable.ic_favorite)},
        })).setView(plans).addOnItemClickListener(new ToolPlanPresentor.ToolPlanClickListener() {
            @Override
            public void onItemClick(Plan plan) {
                if (plan.getDbName().equals("" + TYPE_NULL)) {
                    favoriteItemAdaptor.update();
                    dialog.show();
                } else new PlanDetailDialog(getContext(), plan).show();
            }
        }).setReaderViewAdaptor();
        return view;
    }

    private String toString(Object obj) {
        return obj + "";
    }

    private static class FavoriteItemAdaptor extends ListAdapter {
        private ArrayList<Word> words;
        private Context context;

        public FavoriteItemAdaptor(@NonNull DiffUtil.ItemCallback diffCallback, Context context) {
            super(diffCallback);
            words = new ArrayList<>();
            this.context = context;
            Cursor cursor = context.getContentResolver().query(Uri.parse(Res.FAVO_CONTENT_URL), new String[]{"word", "trans"}, "id==?", new String[]{(AppKit.toString(((OKApplication) context).getUser().getId()))}, null);
            while (cursor.moveToNext())
                words.add(new Word(cursor.getString(0), cursor.getString(1)));
        }

        public void update() {
            words = new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(Uri.parse(Res.FAVO_CONTENT_URL), new String[]{"word", "trans"}, "id==?", new String[]{(AppKit.toString(((OKApplication) context).getUser().getId()))}, null);
            while (cursor.moveToNext())
                words.add(new Word(cursor.getString(0), cursor.getString(1)));
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return words.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FavoriteItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            View bView = holder.itemView;
            TextView wordView = bView.findViewById(R.id.item_favorite_word), transView = bView.findViewById(R.id.item_favorite_trans);
            Word word = words.get(position);
            wordView.setText(word.getWord() + " :  ");
            transView.setText(word.getTranslate()+"\n");
        }
    }

    public static class FavoriteItemViewHolder extends RecyclerView.ViewHolder {
        public FavoriteItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
