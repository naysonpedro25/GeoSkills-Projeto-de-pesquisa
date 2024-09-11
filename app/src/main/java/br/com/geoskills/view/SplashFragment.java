package br.com.geoskills.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import br.com.geoskills.R;
import br.com.geoskills.databinding.FragmentSplashBinding;
import br.com.geoskills.ultil.NetworkUtil;
import br.com.geoskills.ultil.Sounds;
import br.com.geoskills.viewmodel.SplashViewModel;


public class SplashFragment extends Fragment {

   private Sounds sounds;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      FragmentSplashBinding binding = FragmentSplashBinding.inflate(inflater, container, false);
      sounds = Sounds.getInstance(requireContext());
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      SplashViewModel viewModel = new ViewModelProvider(requireActivity()).get(SplashViewModel.class);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mainShared", getContext().MODE_PRIVATE);
      final boolean sliderShown = onSliderFinished();

      sounds.setMusicEnabled(false);
      if (!NetworkUtil.isConnected(requireContext())) {
         Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_noNetworkFragment);
      } else if (!sliderShown) {
         Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_mainSliderFragment);
      } else if (viewModel.getCurrentUser().getValue() == null) {
         Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_auth);
      } else {
         viewModel.getUserFromDb();
         viewModel.getUserGetted().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
               Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
            }
         });
      }


   }

   private boolean onSliderFinished() {
      SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("mainShared", Context.MODE_PRIVATE);
      return sharedPreferences.getBoolean("SLIDER_SHOWN", false);
   }

}