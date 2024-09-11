package br.com.geoskills.view.games;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import br.com.geoskills.R;
import br.com.geoskills.databinding.FragmentCoordinatesGameBinding;
import br.com.geoskills.enums.ColorForTags;
import br.com.geoskills.model.ColoredCoordinate;
import br.com.geoskills.model.Coordinate;
import br.com.geoskills.model.Point;
import br.com.geoskills.repository.GameCoordinateRepository;
import br.com.geoskills.ultil.Sounds;
import br.com.geoskills.view.FinalGamesDialog;
import br.com.geoskills.viewmodel.GameViewModel;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import java.util.Objects;

public class CoordinatesGameFragment extends Fragment {
    private FragmentCoordinatesGameBinding binding;
    private GameViewModel gameViewModel;
    private final Point[] points = new Point[6];
    private View[] pointsViews;
   private Sounds sounds;
    private int pontuationTotal = 0;
    private final ColorForTags[] colors = ColorForTags.values();
    private NavController navController;

    private Coordinate[] coordinates = GameCoordinateRepository.getListCoordinates().get(0);
    private ColoredCoordinate[] coloredCoordinates = GameCoordinateRepository.getListCoordinatesWithColors().get(0);


    private TextView[][] pointsText;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCoordinatesGameBinding.inflate(inflater, container, false);

        pointsViews = new View[]{
                binding.point1,
                binding.point2,
                binding.point3,
                binding.point4,
                binding.point5,
                binding.point6
        };

        for (int i = 0; i < points.length; i++) {
            Coordinate coordinate = coordinates[i];
            points[i] = new Point(pointsViews[i], coordinate.getLatitude(), coordinate.getLongitude());
        }

       pointsText = new TextView[][]{
                {binding.lat1, binding.long1},
                {binding.lat2, binding.long2},
                {binding.lat3, binding.long3},
                {binding.lat4, binding.long4},
                {binding.lat5, binding.long5},
                {binding.lat6, binding.long6},
        };

        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sounds = Sounds.getInstance(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        navController = Navigation.findNavController(requireView());
        initGame();
    }

    private void initGame(){
        initClick();
        addConstraintOnPoints();
        clearPoints();
        setNewText();
    }

    private void initClick() { // certo
        final int[] togglesPoint = new int[points.length];

        for (int i = 0; i < points.length; i++) {
            final int index = i;
            points[i].getPointView().setOnClickListener(v -> {
                sounds.clickSound();
                if (togglesPoint[index] == togglesPoint.length)
                    togglesPoint[index] = 0;
                points[index].togglerColor(colors[togglesPoint[index]]);
                togglesPoint[index]++;
            });

            ClickShrinkEffectKt.applyClickShrink(points[i].getPointView(), .95f, 200L);
        }

        final int[] toggleCoordinates = {1};
        binding.btnContinue.setOnClickListener(v -> {
            ClickListenerBtnNextLevel clickListenerBtnNextLevel = () ->{
                switchTagsCoords(toggleCoordinates[0]);
                toggleCoordinates[0]++;
                addConstraintOnPoints();
                setNewText();
                clearPoints();
            };

            int points = verifyAnswers();
            if(points == 6) pontuationTotal++;
            AlertDialog nextLevel;
            if (toggleCoordinates[0] > GameCoordinateRepository.getListCoordinates().size() - 1) {
                nextLevel = makeAlertDialogNextGame(points, () -> finalGame());
            }else{
                nextLevel = makeAlertDialogNextGame(points, clickListenerBtnNextLevel);
            }

            Objects.requireNonNull(nextLevel).show();
        });


        binding.btnClear.setOnClickListener(v -> {
            sounds.clickSound();
            clearPoints();
        });

        binding.btnBack.setOnClickListener(v -> navController.navigate(R.id.action_coordinatesGameFragment_to_homeFragment));

        ClickShrinkEffectKt.applyClickShrink(binding.btnContinue, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnClear, .95f, 200L);
        ClickShrinkEffectKt.applyClickShrink(binding.btnBack, .95f, 200L);
    }

    private void finalGame() {
        gameViewModel.updatePointsUser(pontuationTotal);
        AlertDialog loadingAlert = gameViewModel.makeLoadingAlert(requireContext());
        loadingAlert.show();
        gameViewModel.getIsEndedCoordinate().observe(getViewLifecycleOwner(), b ->{
            if(b){
                loadingAlert.dismiss();
                makeAlertDialogFInalGame( pontuationTotal).show();
            }
        });
    }

    private int verifyAnswers() {
        int punctuation = 0;
       for (Point point : points) {
          for (int j = 0; j < coordinates.length; j++) {
             if (point.getLatitude().equals(coloredCoordinates[j].getLatitude()) && point.getLongitude().equals(coloredCoordinates[j].getLongitude()) &&
                     point.getColorTag().equals(coloredCoordinates[j].getColor()))
                punctuation++;

          }

       }

        return punctuation;
    }


    private void addConstraintOnPoints() {
        for (int i = 0; i < points.length; i++) {
            Coordinate coordinate = coordinates[i];
            points[i].addConstraintOnPoint(binding.mapConst, coordinate.getLatitude(), coordinate.getLongitude(), getResources());
        }
    }

    private void switchTagsCoords(int tag) {
        if (tag > GameCoordinateRepository.getListCoordinates().size() - 1) return;
        coordinates = GameCoordinateRepository.getListCoordinates().get(tag);
        coloredCoordinates = GameCoordinateRepository.getListCoordinatesWithColors().get(tag);
    }

    private void clearPoints() {
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(pointsViews[i], coordinates[i].getLatitude(), coordinates[i].getLongitude());
            points[i].togglerColor(ColorForTags.WHITE);
        }
    }

    private void setNewText() {
        for (int i = 0; i < points.length; i++) {
            pointsText[i][0].setText(textFormat(coordinates[i].getLatitude()));
            pointsText[i][1].setText(textFormat(coordinates[i].getLongitude()));
        }
    }

    private String textFormat(String s) { // 10 até o underline
        String[] parts = s.split("_");
        if (parts.length != 2) {
            return s;
        }
        String mid = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1);

        return mid + ": " + parts[1] + "°";


    }

    private AlertDialog makeAlertDialogFInalGame(int points) {
        FinalGamesDialog mAlert = new FinalGamesDialog(requireContext(), finalGamesDialog -> {
            sounds.clickSound();
            finalGamesDialog.dismiss();
            navController.navigate(R.id.action_coordinatesGameFragment_to_homeFragment);
        });
        mAlert.setTextPoints(String.valueOf(points));
        mAlert.setStarsCount(points);


        return mAlert;
    }

    private AlertDialog makeAlertDialogNextGame(int points, ClickListenerBtnNextLevel clickListenerBtnNextLevel){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.coordinate_next_level_alert, null);
        AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animdialog;

        TextView textExplanation = view.findViewById(R.id.text_next_level);
        TextView title = view.findViewById(R.id.title_next_level);
        Button btnNext = view.findViewById(R.id.btn_next_level);

        if(points == 6){
            title.setBackgroundResource(R.drawable.bg_title_explanation_positive);
            btnNext.setBackgroundResource(R.drawable.btn_login_bg);
            textExplanation.setText("Você acertou\n +1 ");

        }else {
            title.setBackgroundResource(R.drawable.bg_title_explanation_negative);
            textExplanation.setText("Você errou \n +0");
            btnNext.setBackgroundResource(R.drawable.btn_recover_bg);
        }

        btnNext.setOnClickListener(v -> {
            alertDialog.dismiss();
            clickListenerBtnNextLevel.click();
        });

        return alertDialog;

    }


    private interface ClickListenerBtnNextLevel {
        void click();
    }
}
