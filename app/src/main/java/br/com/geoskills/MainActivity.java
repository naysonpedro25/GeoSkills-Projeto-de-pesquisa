package br.com.geoskills;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import br.com.geoskills.ultil.NetworkUtil;

public class MainActivity extends AppCompatActivity {
   private NavController navController;
   private ConnectivityReceiver connectivityReceiver;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //        EdgeToEdge.enable(this);
//        SplashScreen.installSplashScreen(this);
      setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
      NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
      navController = Objects.requireNonNull(navHostFragment).getNavController();

      SharedPreferences sharedPreferences = getSharedPreferences("mainShared", MODE_PRIVATE);
      if (sharedPreferences.getAll().isEmpty()) {
         SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putBoolean("TUTORIAL_SHOWN", false);
         editor.putBoolean("SLIDER_SHOWN", false);
         editor.apply();
      }

   }

   private class ConnectivityReceiver extends BroadcastReceiver {
      private final NavController navController;

      public ConnectivityReceiver(NavController navController) {
         this.navController = navController;
      }

      @Override
      public void onReceive(Context context, Intent intent) {
         if (!NetworkUtil.isConnected(MainActivity.this)) {
            NetworkUtil.navOnNetworkOffFrag(navController);
         }
      }
   }

   @Override
   protected void onStart() {
      super.onStart();
      connectivityReceiver = new ConnectivityReceiver(navController);
      IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
      registerReceiver(connectivityReceiver, filter);
   }

   @Override
   protected void onStop() {
      super.onStop();
      unregisterReceiver(connectivityReceiver);
   }
}