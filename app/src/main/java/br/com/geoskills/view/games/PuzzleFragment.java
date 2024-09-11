package br.com.geoskills.view.games;

import android.annotation.SuppressLint;
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
import androidx.navigation.Navigation;

import br.com.geoskills.R;
import br.com.geoskills.databinding.FragmentPuzzleBinding;
import br.com.geoskills.model.ContainerView;
import br.com.geoskills.model.DraggedView;
import br.com.geoskills.model.PuzzleLevel;
import br.com.geoskills.repository.GamePuzzleRepository;
import br.com.geoskills.ultil.Sounds;
import br.com.geoskills.view.FinalGamesDialog;
import br.com.geoskills.viewmodel.GameViewModel;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PuzzleFragment extends Fragment {
   private FragmentPuzzleBinding binding;
   private GameViewModel gameViewModel;

   private List<View> draggedViewsLayout; //  só serve para preencher o draggedVies
   private final List<DraggedView> draggedViews = new ArrayList<>();
   private TextView [] textProjections;

   private List<List<View>> containerViewsLayout;//  só serve para preencher o containerViews
   private final List<List<ContainerView>> containerViews = new ArrayList<>();
   private List<ViewGroup> parentDraggedViews;

   private final List<PuzzleLevel> levels = GamePuzzleRepository.getAllLevels();

   private int currentLevel = 1;
   private int points = 0;

   private Sounds sounds;

   private String[] tagsForDraggedViews;

   private int[][] imgProjections;

   private boolean isAlertDialogShowing = false;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      binding = FragmentPuzzleBinding.inflate(inflater, container, false);
      requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
      draggedViewsLayout = Arrays.asList(binding.projecao1, binding.projecao2, binding.projecao3, binding.projecao4, binding.projecao5, binding.projecao6, binding.projecao7, binding.projecao8, binding.projecao9);
      parentDraggedViews = draggedViewsLayout.stream().map(view1 -> (ViewGroup) view1.getParent()).collect(Collectors.toList());
      textProjections  = new TextView[]{binding.projecaoTitle1, binding.projecaoTitle2, binding.projecaoTitle3};
      containerViewsLayout = Arrays.asList(
              Arrays.asList(binding.container1, binding.container2, binding.container3),
              Arrays.asList(binding.container4, binding.container5, binding.container6),
              Arrays.asList(binding.container7, binding.container8, binding.container9));

      sounds = Sounds.getInstance(requireContext());
      addInfoLevel();
      createContainerViews();
      createDraggedViews();
      shuffleAndReassignParents();

      binding.btnReset.setOnClickListener(v -> {
         resetDrag();
      });
      binding.btnNextLevel.setOnClickListener(v -> {
         verify();
      });

      binding.btnBack.setOnClickListener(v ->{
         Navigation.findNavController(view).navigate(R.id.action_puzzleFragment_to_homeFragment);
      });

      ClickShrinkEffectKt.applyClickShrink(binding.btnBack, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnReset, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnNextLevel, .95f, 200L);
   }

   private void resetDrag() {
      for (List<ContainerView> containerView : containerViews) {
         for (ContainerView child : containerView) {
            child.resetDrags();
         }
      }
   }

   @SuppressLint("SetTextI18n")
   private void addInfoLevel() {
      PuzzleLevel level = levels.get(currentLevel - 1);
      tagsForDraggedViews = level.getTags();
      imgProjections = level.getImgs();
      String [] titles = level.getAuthors();
      binding.textLevel.setText(currentLevel + " / " + GamePuzzleRepository.LEVEL_NUM);
      for(int i = 0; i < titles.length; i++) textProjections[i].setText(titles[i]);



   }


   private void createContainerViews() {

      for (int i = 0; i < containerViewsLayout.size(); i++) {
         containerViews.add(new ArrayList<>());
         for (int j = 0; j < containerViewsLayout.get(i).size(); j++) {
            containerViews.get(i).add(new ContainerView(containerViewsLayout.get(i).get(j), ((ViewGroup) containerViewsLayout.get(i).get(j)).getChildAt(0), false, tagsForDraggedViews[i]));
//            Log.i( "TESTE","tag" + (i + 1));
         }
      }
   }

   private void createDraggedViews() {

      for (int i = 0; i < draggedViewsLayout.size(); i++) {
         if (i < 3) {
            draggedViews.add(new DraggedView(draggedViewsLayout.get(i), tagsForDraggedViews[0], imgProjections[0][i]));
         } else if (i < 6) {
            draggedViews.add(new DraggedView(draggedViewsLayout.get(i), tagsForDraggedViews[1], imgProjections[1][i - 3]));
         } else {
            draggedViews.add(new DraggedView(draggedViewsLayout.get(i), tagsForDraggedViews[2], imgProjections[2][i - 6]));
         }
      }

   }

   private void shuffleAndReassignParents() {

      Collections.shuffle(draggedViewsLayout);


      for (int i = 0; i < draggedViewsLayout.size(); i++) {
         View draggedView = draggedViewsLayout.get(i);
         ViewGroup newParent = parentDraggedViews.get(i);

         if (draggedView.getParent() != null) {
            ((ViewGroup) draggedView.getParent()).removeView(draggedView);
         }

         newParent.addView(draggedView);
      }
   }

   private void verify() {

      if (!areAllViewsDragged()) {


         CookieBar.build(requireActivity()).setTitle("Mova todas as caixas! ").setMessage("Por favor, arraste todas as projeções antes de verificar.")
                 .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();

         return;
      }

      boolean[] isAllCorrect = {true};
      for (List<ContainerView> containerView : containerViews) {
         for (ContainerView child : containerView) {
            if (!child.verifyTagDragIsEquals()) {
               isAllCorrect[0] = false;
               break;
            }
         }
      }
      if(!isAlertDialogShowing) {
         isAlertDialogShowing = true;
         AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
         View view = LayoutInflater.from(requireContext()).inflate(R.layout.coordinate_next_level_alert, null);
         AlertDialog alertDialog = builder.setView(view).create();
         alertDialog.setCanceledOnTouchOutside(false);
         Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         alertDialog.getWindow().getAttributes().windowAnimations = R.style.animdialog;

         TextView textExplanation = view.findViewById(R.id.text_next_level);
         TextView title = view.findViewById(R.id.title_next_level);
         Button btnNext = view.findViewById(R.id.btn_next_level);

         btnNext.setOnClickListener(v -> {
            alertDialog.dismiss();
            isAlertDialogShowing = false;
            if (isAllCorrect[0])
               points++;
            advanceLevel();
         });

         if (isAllCorrect[0]) {
            title.setBackgroundResource(R.drawable.bg_title_explanation_positive);
            btnNext.setBackgroundResource(R.drawable.btn_login_bg);
            textExplanation.setText("Você acertou\n +1 ");

         } else {
            title.setBackgroundResource(R.drawable.bg_title_explanation_negative);
            textExplanation.setText("Você errou \n +0");
            btnNext.setBackgroundResource(R.drawable.btn_recover_bg);
         }

         alertDialog.show();

      }
   }

   private void advanceLevel() {
      if (currentLevel < levels.size()) {
         currentLevel++;
         resetDrag();
         resetLevel();
      } else {
         finalGame();
      }
   }



   private void resetLevel() {
      draggedViews.clear();
      containerViews.clear();
      addInfoLevel();
      createContainerViews();
      createDraggedViews();
      shuffleAndReassignParents();

   }
   private boolean areAllViewsDragged() {
      for (DraggedView draggedView : draggedViews) {
         for (ViewGroup parentDraggedView : parentDraggedViews) {
            if(parentDraggedView.equals(draggedView.getView().getParent())) return false;
         }

      }
      return true;
   }

   private AlertDialog makeAlertDialogFinalGame(int points){
      FinalGamesDialog mAlert = new FinalGamesDialog(requireContext(), finalGamesDialog -> {
         sounds.clickSound();
         finalGamesDialog.dismiss();
         Navigation.findNavController(requireView()).navigate(R.id.action_puzzleFragment_to_homeFragment);
      });
      mAlert.setTextPoints(String.valueOf(points));
      mAlert.setStarsCount(points);
      return mAlert;
   }

   private void finalGame() {
      gameViewModel.updatePointsUser(points);
      AlertDialog loadingAlert = gameViewModel.makeLoadingAlert(requireContext());
      loadingAlert.show();
      gameViewModel.getIsEndedPuzzle().observe(getViewLifecycleOwner(), b ->{
         if(b){
            loadingAlert.dismiss();
            makeAlertDialogFinalGame(points).show();
         }
      });
   }

}