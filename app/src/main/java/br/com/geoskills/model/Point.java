package br.com.geoskills.model;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import br.com.geoskills.R;
import br.com.geoskills.enums.ColorForTags;

public class Point {
    private final View pointView;
    private final String latitude;
    private final String longitude;
    private String colorTag;


    public Point(View pointView, String latitude, String longitude) {
        this.pointView = pointView;
        this.latitude = latitude;
        this.longitude = longitude;
        this.colorTag = "";
    }


    public void togglerColor(ColorForTags color) {
        colorTag = color.getColorName();
        int colorRes = ContextCompat.getColor(pointView.getContext(), getColorResource(color));

        GradientDrawable pointBackground = (GradientDrawable) pointView.getBackground();
        pointBackground.setColor(colorRes);
        pointBackground.setStroke(5, Color.BLACK);

    }

    public void addConstraintOnPoint(ConstraintLayout mConstraintLayout, String latitude, String longitude, Resources resources) {
        ConstraintSet constraintSet = new ConstraintSet();

        @SuppressLint("DiscouragedApi") int coordLatid = resources.getIdentifier(latitude, "id", pointView.getContext().getPackageName()); // guideline id lat
        @SuppressLint("DiscouragedApi") int coordLongid = resources.getIdentifier(longitude, "id", pointView.getContext().getPackageName()); // guideline id long

        constraintSet.clone(mConstraintLayout);

        constraintSet.connect(pointView.getId(), ConstraintSet.TOP, coordLatid, ConstraintSet.BOTTOM); // conecta o point ao guideline
        constraintSet.connect(pointView.getId(), ConstraintSet.START, coordLongid, ConstraintSet.END);

        constraintSet.applyTo(mConstraintLayout);
    }

    private int getColorResource(ColorForTags colorTag) {
        switch (colorTag) {
            case RED:
                return R.color.coor_red;
            case GREEN:
                return R.color.coor_green;
            case BLUE:
                return R.color.coor_blue;
            case ORANGE:
                return R.color.coor_orange;
            case YELLOW:
                return R.color.coor_yellow;
            case PURPLE:
                return R.color.coor_purple;
            default:
                return R.color.white;
//                throw new IllegalArgumentException("Invalid color tag");
        }
    }

    public View getPointView() {
        return pointView;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getColorTag() {
        return colorTag;
    }

}
