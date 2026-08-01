package com.mohammedaqeel.sarfit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomBuilderActivity extends AppCompatActivity {

    private final List<CheckBox> checkboxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_builder);
        Fonts.applyRecursively(this, findViewById(android.R.id.content));

        LinearLayout checkboxContainer = findViewById(R.id.checkboxContainer);
        final Map<String, List<Exercise>> pool = CustomWorkoutData.getMusclePool();

        for (String muscle : pool.keySet()) {
            CheckBox cb = new CheckBox(this);
            cb.setText(muscle);
            cb.setTextColor(Color.parseColor("#EAEAEA"));
            cb.setTextSize(15);
            cb.setPadding(4, 14, 4, 14);
            checkboxContainer.addView(cb);
            checkboxes.add(cb);
        }

        final LinearLayout resultContainer = findViewById(R.id.resultContainer);
        TextView tvGenerate = findViewById(R.id.tvGenerate);

        tvGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultContainer.removeAllViews();
                boolean any = false;

                for (CheckBox cb : checkboxes) {
                    if (!cb.isChecked()) continue;
                    any = true;
                    String muscle = cb.getText().toString();
                    List<Exercise> exercises = pool.get(muscle);

                    TextView header = new TextView(CustomBuilderActivity.this);
                    header.setText(muscle);
                    header.setTextColor(Color.parseColor("#39FF14"));
                    header.setTextSize(17);
                    header.setTypeface(header.getTypeface(), android.graphics.Typeface.BOLD);
                    LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    hlp.topMargin = 18;
                    hlp.bottomMargin = 6;
                    header.setLayoutParams(hlp);
                    resultContainer.addView(header);

                    if (exercises != null) {
                        for (Exercise ex : exercises) {
                            TextView row = new TextView(CustomBuilderActivity.this);
                            row.setText(ex.name + "  \u2014  " + ex.getSetsReps());
                            row.setTextColor(Color.parseColor("#CFCFCF"));
                            row.setTextSize(13.5f);
                            row.setPadding(16, 12, 16, 12);
                            row.setBackgroundResource(R.drawable.gradient_card);
                            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            rlp.bottomMargin = 8;
                            row.setLayoutParams(rlp);
                            resultContainer.addView(row);
                        }
                    }
                }

                if (!any) {
                    TextView empty = new TextView(CustomBuilderActivity.this);
                    empty.setText("Pick at least one muscle group above.");
                    empty.setTextColor(Color.parseColor("#888888"));
                    empty.setTextSize(13);
                    resultContainer.addView(empty);
                }

                Fonts.applyRecursively(CustomBuilderActivity.this, resultContainer);
            }
        });
    }
}
