package br.com.geoskills.view.homeviewpager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import br.com.geoskills.R;
import br.com.geoskills.adapter.HomeViewPagerAdapter;
import br.com.geoskills.databinding.FragmentMainHomeBinding;
import br.com.geoskills.model.User;
import br.com.geoskills.ultil.Sounds;
import br.com.geoskills.view.profile.ProfileBottomSheet;
import br.com.geoskills.view.profile.ReloadFragment;
import br.com.geoskills.viewmodel.AuthViewModel;
import br.com.geoskills.viewmodel.MainHomeFragmentViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.muratozturk.click_shrink_effect.ClickShrinkEffectKt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainHomeFragment extends Fragment {

   private FragmentMainHomeBinding binding;
   private List<Fragment> list;
   private Sounds sounds;
   private boolean toggleSoundClick;
   private boolean toggleMusicClick;
   private ReloadFragment reloadFragment;
   private User mUser;
   private AuthViewModel authViewModel;
   private MainHomeFragmentViewModel homeViewModel;
   private SharedPreferences sharedPreferences;



   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      sharedPreferences = requireActivity().getSharedPreferences("mainShared", Context.MODE_PRIVATE);
      authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
      homeViewModel = new ViewModelProvider(this).get(MainHomeFragmentViewModel.class);
      binding = FragmentMainHomeBinding.inflate(inflater, container, false);
      requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
      list = new ArrayList<>();
      initViewPager();
      sounds = Sounds.getInstance(requireContext());

      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      reloadFragment = new ReloadFragment() {
         @Override
         public void reload() {
            onViewCreated(requireView(), null);
         }

         @Override
         public void reloadInforUser() {
            homeViewModel.reloadInforUser(getViewLifecycleOwner());
         }
      };
      initClicks();
      addInfoUser();
      final boolean tutorialShown = getTutorialShown();
      if (!tutorialShown) startTutorial();
   }

   private void initViewPager() {


      list.add(new HomeFragment());
      list.add(new RankFragment());
      binding.homeViewpager.setAdapter(new HomeViewPagerAdapter(requireActivity(), list));
      binding.homeViewpager.setOffscreenPageLimit(list.size());
      binding.homeViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
         @Override
         public void onPageSelected(int position) {
            super.onPageSelected(position);
            sounds.transitSound();
            if (position == 0) {
               binding.homePage.setBackgroundResource(R.drawable.bottom_nav_active);
               binding.rankPage.setBackgroundResource(R.drawable.bottom_nav);

            } else {
               binding.rankPage.setBackgroundResource(R.drawable.bottom_nav_active);
               binding.homePage.setBackgroundResource(R.drawable.bottom_nav);
            }
         }
      });
   }

   private void initClicks() {
      binding.homePage.setOnClickListener(v -> {
         v.setBackgroundResource(R.drawable.bottom_nav_active);
         binding.rankPage.setBackgroundResource(R.drawable.bottom_nav);
         binding.homeViewpager.setCurrentItem(0);
      });
      binding.rankPage.setOnClickListener(v -> {
         v.setBackgroundResource(R.drawable.bottom_nav_active);
         binding.homePage.setBackgroundResource(R.drawable.bottom_nav);
         binding.homeViewpager.setCurrentItem(1);
      });

      final boolean[] settingShown = {false};
      binding.btnSetting.setOnClickListener(v -> {
         sounds.transitSound();
         AlertDialog settingDialog = initSettingDialog();
         if (!settingShown[0]) {
            settingDialog.show();
            settingShown[0] = true;
         }
         settingDialog.setOnDismissListener(dialog -> settingShown[0] = false);
      });

      binding.perfilUser.setOnClickListener(v -> {
         sounds.transitSound();
         if (mUser != null) {
            ProfileBottomSheet profileBottomSheet = createBottomSheetProfile(mUser.getName(), mUser.getProfileSelected());
            profileBottomSheet.show(getChildFragmentManager(), null);

         }
      });

      ClickShrinkEffectKt.applyClickShrink(binding.homePage, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.rankPage, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.perfilUser, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(binding.btnSetting, .95f, 200L);
   }

   private AlertDialog initSettingDialog() {
      toggleSoundClick = sounds.getIsClickSoundEnabled();
      toggleMusicClick = sounds.getIsMusicEnabled();
      AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
      View view = LayoutInflater.from(requireContext()).inflate(R.layout.setting_dialog, null);
      AlertDialog alertDialog = builder.setView(view).create();
      alertDialog.setCanceledOnTouchOutside(true);
      Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      alertDialog.getWindow().getAttributes().windowAnimations = R.style.animdialog;
      alertDialog.setOnDismissListener(dialog -> sounds.transitSound());
      Button btnSingOut = view.findViewById(R.id.btn_singOut);
      ImageView btnSound = view.findViewById(R.id.sound_btn);
      ImageView btnMusic = view.findViewById(R.id.music_btn);

      btnSingOut.setOnClickListener(v -> {
         AlertDialog loadingDialog = authViewModel.makeLoadingAlert(requireContext());
         alertDialog.dismiss();
         loadingDialog.show();
         sounds.clickSound();
         authViewModel.logout();
         authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser == null) {
               loadingDialog.dismiss();
               Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_auth);
            }
         });
      });


      if (!sounds.getIsMusicEnabled()) {
         btnMusic.setImageResource(R.drawable.ic_music_off);
      }
      if (!sounds.getIsClickSoundEnabled()) {
         btnSound.setImageResource(R.drawable.ic_sound_off);
      }

      btnSound.setOnClickListener(v -> {
         sounds.clickSound();
         if (toggleSoundClick) {
            btnSound.setImageResource(R.drawable.ic_sound_off);
            toggleSoundClick = false;
            sounds.setClickSoundEnabled(false);
         } else {
            btnSound.setImageResource(R.drawable.ic_sound);
            toggleSoundClick = true;
            sounds.setClickSoundEnabled(true);
         }
      });

      btnMusic.setOnClickListener(v -> {
         sounds.clickSound();
         if (toggleMusicClick) {
            btnMusic.setImageResource(R.drawable.ic_music_off);
            toggleMusicClick = false;
            sounds.setMusicEnabled(false);
         } else {
            btnMusic.setImageResource(R.drawable.ic_music);
            toggleMusicClick = true;
            sounds.setMusicEnabled(true);
         }
      });

      ClickShrinkEffectKt.applyClickShrink(btnSound, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(btnMusic, .95f, 200L);
      ClickShrinkEffectKt.applyClickShrink(btnSingOut, .95f, 200L);

      return alertDialog;
   }

   public void addInfoUser() {
      homeViewModel.reloadInforUser(getViewLifecycleOwner());
      homeViewModel.setUserGetted(getViewLifecycleOwner());
      homeViewModel.getUserGetted().observe(getViewLifecycleOwner(), user -> {
         if (user != null) {
            this.mUser = user;
            binding.textNameUser.setText(user.getName());
            binding.textPoints.setText(String.valueOf(user.getPoints()));
            if (user.getProfileSelected() == 0) {
               binding.profileImg.setImageResource(R.drawable.boy_profile);
            } else {
               binding.profileImg.setImageResource(R.drawable.girl_profile);
            }
         }
      });
   }

   public ProfileBottomSheet createBottomSheetProfile(String name, int profileIm) {
      return new ProfileBottomSheet(name, profileIm, reloadFragment);
   }


   private void startTutorial() {

      TapTargetSequence tapTargetSequence = new TapTargetSequence(requireActivity());
      tapTargetSequence.targets(
              TapTarget.forView(binding.btnSetting, "Clique nesse botão para abrir as configurações do app")
                      .tintTarget(false)
                      .outerCircleColor(R.color.blue_deep_dark)
                      .outerCircleAlpha(.9f)
                      .cancelable(false)
                      .textColor(R.color.white)
                      .targetCircleColor(R.color.dark_orange),

              TapTarget.forView(binding.layoutPoints, "Aqui onde está a sua pontuação dos jogos")
                      .tintTarget(false)
                      .outerCircleColor(R.color.blue_deep_dark)
                      .outerCircleAlpha(.9f)
                      .cancelable(false)
                      .textColor(R.color.white)
                      .targetRadius(60)
                      .targetCircleColor(R.color.white),

              TapTarget.forView(binding.perfilUser, "Clique aqui para abrir o perfil do usuário")
                      .tintTarget(false)
                      .outerCircleColor(R.color.blue_deep_dark)
                      .outerCircleAlpha(.9f)
                      .textColor(R.color.white)
                      .cancelable(false)
                      .targetCircleColor(R.color.blue_light)

      ).listener(new TapTargetSequence.Listener() {
         @Override
         public void onSequenceFinish() {
            startTutorial2();
         }

         @Override
         public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

         }

         @Override
         public void onSequenceCanceled(TapTarget lastTarget) {

         }
      });
      tapTargetSequence.start();


   }

   private void startTutorial2() {

      HomeFragment homeFragment = (HomeFragment) ((HomeViewPagerAdapter) Objects.requireNonNull(binding.homeViewpager.getAdapter())).createFragment(0);
      if (homeFragment.isAdded()) {
         HomeFragment.Tutorial3 tutorial3 = () -> {
            startTutorial3();
         };
         homeFragment.startTutorial2(tutorial3);
      }
   }

   private void startTutorial3() {


      new TapTargetSequence(requireActivity()).targets(
              TapTarget.forView(binding.homePage, "Clique aqui para abrir a pagina home")
                      .tintTarget(false)
                      .outerCircleColor(R.color.blue_deep_dark)
                      .outerCircleAlpha(.9f)
                      .cancelable(false)
                      .targetRadius(100)
                      .textColor(R.color.white)
                      .transparentTarget(true),

              TapTarget.forView(binding.rankPage, "Clique aqui para abrir a pagina rank")
                      .tintTarget(false)
                      .outerCircleColor(R.color.blue_deep_dark)
                      .outerCircleAlpha(.9f)
                      .cancelable(false)
                      .targetRadius(100)
                      .transparentTarget(true)
                      .textColor(R.color.white)


      ).listener(
              new TapTargetSequence.Listener() {
                 @Override
                 public void onSequenceFinish() {

                 }

                 @Override
                 public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                 }

                 @Override
                 public void onSequenceCanceled(TapTarget lastTarget) {

                 }
              }
      ).start();
      onTutorialFinished();
   }

   private boolean getTutorialShown() {
      return sharedPreferences.getBoolean("TUTORIAL_SHOWN", false);
   }

   public void onTutorialFinished() {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putBoolean("TUTORIAL_SHOWN", true);
      editor.apply();
   }


}