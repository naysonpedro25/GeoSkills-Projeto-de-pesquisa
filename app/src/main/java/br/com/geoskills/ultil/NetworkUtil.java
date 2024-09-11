package br.com.geoskills.ultil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;


import br.com.geoskills.R;

public class NetworkUtil {

   public static boolean isConnected(Context context) {
      try {
         ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = cm.getActiveNetworkInfo();
         return networkInfo != null && networkInfo.isConnectedOrConnecting();
      } catch (Exception e) {
         Log.i("INTERNET_ERROR", "Erro em verificar a conexão com a internet \n" + e.getMessage());
         return false;

      }
   }

   public static void navOnNetworkOffFrag(NavController navController){
      NavOptions navOptions = new NavOptions.Builder().setEnterAnim(android.R.anim.fade_in).setExitAnim(android.R.anim.fade_out).build();
      navController.navigate(R.id.noNetworkFragment, null, navOptions);
   }
}
