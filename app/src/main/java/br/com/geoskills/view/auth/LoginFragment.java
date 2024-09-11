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


import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;

import java.util.Objects;

import br.com.geoskills.R;
import br.com.geoskills.ultil.Sounds;
import br.com.geoskills.databinding.FragmentLoginBinding;
import br.com.geoskills.viewmodel.AuthViewModel;


public class LoginFragment extends Fragment {
   private AuthViewModel viewModel;
   private FragmentLoginBinding binding;
   private Sounds sounds;
   private NavController navController;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      binding = FragmentLoginBinding.inflate(inflater, container, false);
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
      binding.btnLogin.setOnClickListener(v -> {
         loginUser(binding.editEmail.getText().toString(), binding.editPassword.getText().toString());
         sounds.clickSound();
      });
      binding.btnRegister.setOnClickListener(v -> {
         sounds.clickSound();
         Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
      });
      binding.btnRecover.setOnClickListener(v -> {
         sounds.clickSound();
         Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_recoverFragment);
      });

      ClickShrinkEffectKt.applyClickShrink(binding.btnLogin, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnRegister, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnRecover, .95f, 200L);
   }

   private void loginUser(String email, String password) {
      if (email.isEmpty() || password.isEmpty()) {

         CookieBar.build(requireActivity()).setTitle("Preencha todos os campos").setMessage("Preencha todos os campos do formulário de login para poder continuar")
                 .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
         return;
      }
      viewModel.loginUser(email, password);

      AlertDialog alertDialog = viewModel.makeLoadingAlert(requireContext());
      alertDialog.show();
      final boolean[] uniqueCookieBar = {false};
      final boolean[] uniqueNav = {false};

      viewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
         if (firebaseUser != null) {
            alertDialog.dismiss();
            if(!uniqueNav[0]) {
               navController.navigate(R.id.action_global_homeFragment);
               uniqueNav[0] = !uniqueNav[0];
            }
         }
      });
      viewModel.getErrorInLoginOrsignIn().observe(getViewLifecycleOwner(), errorData -> {
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