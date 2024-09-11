package br.com.geoskills.view;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.geoskills.R;
import br.com.geoskills.ultil.Sounds;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import java.util.Objects;

public class FinalGamesDialog extends AlertDialog {
    private final Sounds sounds;
    private final TextView textPoints;
    private final ImageView imgStars;
    private final Button btnFinish;



    public FinalGamesDialog(Context context, ClickListenerBtnFinish clickListenerBtnFinish){
        super(context, R.style.dialog);
        sounds = Sounds.getInstance(context);

        View view = LayoutInflater.from(context).inflate(R.layout.quiz_end_alert_dialog, null);
        super.setView(view);
        super.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(super.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.getWindow().getAttributes().windowAnimations = R.style.animdialog;
        super.setOnDismissListener(dialog -> sounds.transitSound());
        textPoints = view.findViewById(R.id.user_points);
        imgStars = view.findViewById(R.id.stars_user_points);
        btnFinish = view.findViewById(R.id.btn_finish_quiz);
        ClickShrinkEffectKt.applyClickShrink(btnFinish, .95f, 200L);

        btnFinish.setOnClickListener((View v) -> clickListenerBtnFinish.click(this));
    }

    public void setStarsCount(int points){
        switch (points) {
            case 0:
                imgStars.setImageResource(R.drawable.stars_0);
                break;
            case 1:
                imgStars.setImageResource(R.drawable.stars_1);
                break;
            case 2:
                imgStars.setImageResource(R.drawable.stars_2);
                break;
            default:
                imgStars.setImageResource(R.drawable.stars_3);
        }
    }

    public void setTextPoints(String points){
        textPoints.setText(String.format("Pontuação: +%s", points));
    }
    

    public interface ClickListenerBtnFinish{
        void click(FinalGamesDialog finalGamesDialog);
    }
}
