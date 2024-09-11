package br.com.geoskills.view.auth;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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
import br.com.geoskills.databinding.FragmentRegisterBinding;
import br.com.geoskills.model.User;
import br.com.geoskills.ultil.Sounds;

import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import org.aviran.cookiebar2.CookieBar;

import java.util.Objects;

import br.com.geoskills.viewmodel.AuthViewModel;

public class RegisterFragment extends Fragment {
   private AuthViewModel viewModel;
   private Sounds sounds;
   private FragmentRegisterBinding binding;
   private int profileSelected = -1;
   private NavController navController;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      binding = FragmentRegisterBinding.inflate(inflater, container, false);
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
      binding.btnRegister.setOnClickListener(v -> {
         sounds.clickSound();
         registerUser(binding.editName.getText().toString(), binding.editEmail.getText().toString(), binding.editPassword.getText().toString());
      });

      binding.boyProfile.setOnClickListener(v -> {
         sounds.clickSound();
         v.setBackgroundResource(R.drawable.profile_selected);
         binding.girlProfile.setBackgroundResource(R.drawable.profile_selection_bg);
         profileSelected = 0;
      });
      binding.girlProfile.setOnClickListener(v -> {
         sounds.clickSound();
         v.setBackgroundResource(R.drawable.profile_selected);
         binding.boyProfile.setBackgroundResource(R.drawable.profile_selection_bg);
         profileSelected = 1;
      });

      binding.btnLogin.setOnClickListener(v -> {
         sounds.clickSound();
         Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
      });
      binding.btnRecover.setOnClickListener(v -> {
         sounds.clickSound();
         Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_recoverFragment);
      });

      ClickShrinkEffectKt.applyClickShrink(binding.btnLogin, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnRegister, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnRecover, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.boyProfile, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.girlProfile, .95f, 200L);

   }

   private void registerUser(String name, String email, String senha) {


      if (name.isEmpty() || email.isEmpty() || senha.isEmpty() || profileSelected == -1) {
         CookieBar.build(requireActivity()).setTitle("Preencha todos os campos").setMessage("Preencha todos os campos do formulário de recuperação para poder continuar")
                 .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
         return;
      }

      if (name.length() < 4 || name.length() > 16) {
         CookieBar.build(requireActivity()).setTitle("Preencha todos os campos").setMessage("O nome de usuário deve ter entre 5 e 16 caracteres")
                 .setCookiePosition(CookieBar.TOP).setBackgroundColor(R.color.red).setDuration(4000).setIcon(R.drawable.ic_error).show();
         return;
      }
      viewModel.registerUser(new User(name, email, 0, profileSelected), senha);
      AlertDialog alertDialog = viewModel.makeLoadingAlert(requireContext());
      alertDialog.show();

      final boolean[] uniqueCookieBar = {false};
      final boolean[] uniqueNav = {false};

      viewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
         if (firebaseUser != null && requireView().isShown()) {
            alertDialog.dismiss();
            if (!uniqueNav[0]) {
               try {
                  navController.navigate(R.id.action_global_homeFragment);
                  uniqueNav[0] = !uniqueNav[0];

               }catch (IllegalArgumentException e){
                  Log.e("NavController" , "Navigation action/destination not found : " + e.getMessage(), e);
               }
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