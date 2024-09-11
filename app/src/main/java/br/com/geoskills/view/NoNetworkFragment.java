package br.com.geoskills.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import br.com.geoskills.R;
import br.com.geoskills.databinding.FragmentNoNetworkBinding;
import br.com.geoskills.ultil.NetworkUtil;
import br.com.geoskills.ultil.Sounds;
import com.google.android.material.snackbar.Snackbar;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;


public class NoNetworkFragment extends Fragment {
   private FragmentNoNetworkBinding binding;
   private Sounds sounds;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      binding = FragmentNoNetworkBinding.inflate(inflater, container, false);
      return binding.getRoot();

   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      sounds = Sounds.getInstance(requireContext());
      sounds.setMusicEnabled(false);
      requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
         @Override
         public void handleOnBackPressed() {
            if (NetworkUtil.isConnected(requireContext())) {
//            NavOptions navOptions = new NavOptions.Builder().setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out).build();
               NavController navController = Navigation.findNavController(view);
               navController.popBackStack();
            } else
               Snackbar.make(view, "Verifique sua conexão com a internet e tente novamente", Snackbar.LENGTH_SHORT).show();

         }
      });

      binding.tryAgainButton.setOnClickListener(v -> {
         if (NetworkUtil.isConnected(requireContext())) {
//            NavOptions navOptions = new NavOptions.Builder().setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out).build();
            sounds.clickSound();
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack();
         } else
//            Snackbar.make(view, "Verifique sua conexão com a internet e tente novamente", Snackbar.LENGTH_SHORT).show();
            CookieBar.build(requireActivity()).setTitle("Sem conexão com Internet").setMessage("Verifique sua conexão com a Internet e tente novamente")
                    .setCookiePosition(CookieBar.BOTTOM).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_2_offline).show();


      });
      ClickShrinkEffectKt.applyClickShrink(binding.tryAgainButton, .95f, 200L);
   }
}