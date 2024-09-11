package br.com.geoskills.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import br.com.geoskills.model.User;
import br.com.geoskills.repository.AuthRepository;
import br.com.geoskills.repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class SplashViewModel extends AndroidViewModel {
    private final FirestoreRepository firestoreRepository;
    private final AuthRepository authRepository;
    private final MutableLiveData<User> userGetted;
    public SplashViewModel(@NonNull Application application) {
        super(application);
        firestoreRepository = FirestoreRepository.getInstance();
        authRepository = AuthRepository.getInstance();
        userGetted = new MutableLiveData<>();

    }

    public void getUserFromDb(){

       firestoreRepository.getUserOnDb(authRepository.getCurrentUserId(), task -> {
           if(task.isSuccessful()){
               if(task.getResult() != null){
                   User user = task.getResult().toObject(User.class);
                   if(user != null) {
                       userGetted.postValue(user);
                       firestoreRepository.getUserGetted().postValue(user);
                   }
               }else{
                   Log.i("ERROR_USER_NOT_FOUND","O usuario não foi encontrado no db" +  Objects.requireNonNull(task.getException()).getMessage());
               }
           }else{
               Log.i("ERROR_GET_USER","Erro ao buscar usuario no db" +  Objects.requireNonNull(task.getException()).getMessage());
           }
       });

    }

    public MutableLiveData<User> getUserGetted() {
        return userGetted;
    }

    public MutableLiveData<FirebaseUser> getCurrentUser(){
        return authRepository.getCurrentUser();
    }

}
