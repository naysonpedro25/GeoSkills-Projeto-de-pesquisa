package br.com.geoskills.view.auth;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import br.com.geoskills.R;
import br.com.geoskills.databinding.FragmentRecoverBinding;
import br.com.geoskills.ultil.Sounds;
import br.com.geoskills.viewmodel.AuthViewModel;

import com.google.android.material.snackbar.Snackbar;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;

import java.util.Objects;


public class RecoverFragment extends Fragment {
   private AuthViewModel viewModel;
   private FragmentRecoverBinding binding;
   private Sounds sounds;
   private NavController navController;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      binding = FragmentRecoverBinding.inflate(inflater, container, false);
      sounds = Sounds.getInstance(requireContext());
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
      navController = Objects.requireNonNull(Navigation.findNavController(view));
      initClick();
   }

   private void initClick() {

      binding.btnRecover.setOnClickListener(v -> {
         sounds.clickSound();
         recoverPassword(binding.editEmail.getText().toString());
      });

      binding.btnLogin.setOnClickListener(v -> {
         sounds.clickSound();
         Navigation.findNavController(v).navigate(R.id.action_recoverFragment_to_loginFragment);
      });
      binding.btnRegister.setOnClickListener(v -> {
         sounds.clickSound();
         Navigation.findNavController(v).navigate(R.id.action_recoverFragment_to_registerFragment);
      });

      ClickShrinkEffectKt.applyClickShrink(binding.btnLogin, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnRegister, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnRecover, 0.95f, 200L);
   }

   private void recoverPassword(String email) {
      if (email.isEmpty()) {
         CookieBar.build(requireActivity()).setTitle("Preencha todos os campos").setMessage("Preencha todos os campos do formulário de registro para poder continuar")
                 .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
         return;
      }
      AlertDialog alertDialog = viewModel.makeLoadingAlert(requireContext());
      alertDialog.show();
      viewModel.recoverPassword(email);
      viewModel.getSendedEmail().observe(getViewLifecycleOwner(), aBoolean -> {
         if (aBoolean) {
            alertDialog.dismiss();
            Snackbar.make(requireView(), "E-mail enviado", Snackbar.LENGTH_SHORT).show();
            navController.navigate(R.id.action_recoverFragment_to_loginFragment);
         }
      });
      final boolean[] uniqueCookieBar = {false};
      viewModel.getErrorRecoverPassword().observe(getViewLifecycleOwner(), errorData -> {
         if (errorData != null) {
            alertDialog.dismiss();
            if (!uniqueCookieBar[0]) {
               CookieBar.build(requireActivity()).setTitle(errorData.getTitle())
                       .setMessage(errorData.getMessage())
                       .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
               uniqueCookieBar[0] = !uniqueCookieBar[0];
            }
         }
      });
   }


}