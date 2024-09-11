package br.com.geoskills.viewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import br.com.geoskills.R;
import br.com.geoskills.model.User;
import br.com.geoskills.repository.AuthRepository;
import br.com.geoskills.repository.FirestoreRepository;
import br.com.geoskills.ultil.ErrorData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class AuthViewModel extends AndroidViewModel {
   private final FirestoreRepository firestoreRepository = FirestoreRepository.getInstance();
   private final AuthRepository authRepository = AuthRepository.getInstance();

   private final MutableLiveData<FirebaseUser> currentUserVM = new MutableLiveData<>();
   private final MutableLiveData<ErrorData> errorInLoginOrsignIn = new MutableLiveData<>(null);
   private final MutableLiveData<ErrorData> errorRecoverPassword = new MutableLiveData<>(null);

   private final MutableLiveData<Boolean> sendedEmail = new MutableLiveData<>(false);


   public AuthViewModel(@NonNull Application application) {
      super(application);
      currentUserVM.setValue(authRepository.getCurrentUser().getValue());
   }

   public void registerUser(User user, String password) {
      errorInLoginOrsignIn.setValue(null);

      firestoreRepository.checkUserNameExists(user.getName(), task -> {
         if (task.isSuccessful()) {

            QuerySnapshot queryDocumentSnapshots = task.getResult();

            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
               Log.i("USERNAME_EXISTS", "O nome de usuário " + user.getName() + " já está em uso.");
               errorInLoginOrsignIn.postValue(authRepository.errorHandling(new Exception("O nome de usuário já está em uso")));

            } else {
               authRepository.registerUser(user.getEmail(), password, task1 -> {
                  if (task1.isSuccessful()) {
                     authRepository.getCurrentUser().setValue(authRepository.getAuth().getCurrentUser());

                     user.setUuid(Objects.requireNonNull(authRepository.getCurrentUser().getValue()).getUid());
                     firestoreRepository.registerUserOnDb(user, task11 -> {
                        if (task11.isSuccessful()) {
                           currentUserVM.postValue(authRepository.getCurrentUser().getValue());
                        } else {
                           Log.d("ERRO_REGISTER_USER", "Erro ao registrar usuário NO BANCO", task11.getException());
                           errorInLoginOrsignIn.postValue(authRepository.errorHandling(Objects.requireNonNull(task11.getException())));
                        }

                     });
                  } else {
                     Log.d("ERRO_REGISTER_USER", "Erro ao registrar usuário", task1.getException());
                     errorInLoginOrsignIn.postValue(authRepository.errorHandling(Objects.requireNonNull(task1.getException())));
                  }

               });
            }

         } else {
            Log.d("ERRO_USERNAME", "Erro ao procurar nome de usuário", task.getException());
            errorInLoginOrsignIn.postValue(authRepository.errorHandling(Objects.requireNonNull(task.getException())));

         }

      });

   }

   public void loginUser(String email, String password) {
      errorInLoginOrsignIn.setValue(null);
      authRepository.loginUser(email, password, task -> {
         if (task.isSuccessful()) {
            currentUserVM.postValue(authRepository.getAuth().getCurrentUser());
         } else {
            Log.d("ERRO_LOGIN", "Erro ao fazer login", task.getException());
            errorInLoginOrsignIn.postValue(authRepository.errorHandling(Objects.requireNonNull(task.getException())));
         }

      });

   }

   public void recoverPassword(String email) {
      errorRecoverPassword.setValue(null);
      authRepository.revoverPassword(email, task -> {
         if (task.isSuccessful()) {
            sendedEmail.postValue(true);
         } else {
            Log.d("ERRO_RECOVER_PASSWORD", "Erro ao recuperar senha", task.getException());

         }

      });
   }


   public MutableLiveData<FirebaseUser> getCurrentUser() {
      return currentUserVM;
   }

   public MutableLiveData<Boolean> getSendedEmail() {
      return sendedEmail;
   }

   public MutableLiveData<ErrorData> getErrorInLoginOrsignIn() {
      return errorInLoginOrsignIn;
   }

   public MutableLiveData<ErrorData> getErrorRecoverPassword() {
      return errorRecoverPassword;
   }


   public void logout() {
      authRepository.logout();
      currentUserVM.postValue(null);
   }

   public AlertDialog makeLoadingAlert(Context context) {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      View view = LayoutInflater.from(context).inflate(R.layout.loading_alert, null);
      AlertDialog alertDialog = builder.setView(view).create();
      alertDialog.setCanceledOnTouchOutside(false);
      Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      return alertDialog;
   }
}
