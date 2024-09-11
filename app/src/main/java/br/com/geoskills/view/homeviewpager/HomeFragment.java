package br.com.geoskills.view.homeviewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import br.com.geoskills.R;
import br.com.geoskills.databinding.FragmentHomeBinding;
import br.com.geoskills.ultil.Sounds;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;


public class HomeFragment extends Fragment {

   private FragmentHomeBinding binding;
   private Sounds sounds;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      binding = FragmentHomeBinding.inflate(inflater, container, false);
      return binding.getRoot();

   }




   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      sounds = Sounds.getInstance(requireContext());
      ClickShrinkEffectKt.applyClickShrink(binding.btnPlayQuiz, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnPlayCoordinates, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.projecoes, .95f, 200L);
      binding.btnPlayQuiz.setOnClickListener(v -> {
         sounds.clickSound();
         Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_quizGameFragment);
      });
      binding.btnPlayCoordinates.setOnClickListener(v -> {
         sounds.clickSound();
         Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_coordinatesGameFragment);
      });
      binding.projecoes.setOnClickListener(v->{
         sounds.clickSound();
         Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_puzzleFragment);
      });





   }

   public void startTutorial2(Tutorial3 tutorial3) {


      TapTargetView.showFor(requireActivity(),
              TapTarget.forView(binding.btnPlayQuiz, "Clique aqui para abrir o perfil do usuário")
                      .tintTarget(false)
                      .outerCircleColor(R.color.blue_deep_dark)
                      .outerCircleAlpha(.9f)
                      .cancelable(false)
                      .textColor(R.color.white)
                      .targetCircleColor(R.color.white),
              new TapTargetView.Listener(){
                 @Override
                 public void onTargetClick(TapTargetView view) {
                    super.onTargetClick(view);
                    tutorial3.start();
                 }
              }
      );
   }


   public  interface Tutorial3{
      void start();
   }

}


