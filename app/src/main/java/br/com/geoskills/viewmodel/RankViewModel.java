package br.com.geoskills.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import br.com.geoskills.model.User;
import br.com.geoskills.repository.FirestoreRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class RankViewModel extends AndroidViewModel {
    private final FirestoreRepository repository;
    private final MutableLiveData<List<User>> listMutableLiveData;


    public RankViewModel(@NonNull Application application) {
        super(application);
        repository = FirestoreRepository.getInstance();
        listMutableLiveData = new MutableLiveData<>();
    }

    public void getAllUsersOnDb(View view){
        final List<User> list = new ArrayList<>();
        repository.getAllUsersOnDb((value, error) -> {
            if(error != null){
                // colocar tratar melhor isso aq
                return;
            }
            if (value != null) {
                list.clear();
                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    User user = documentSnapshot.toObject(User.class);
                    if(user != null && !list.contains(user)){
                        list.add(user);
                    }
                }

                listMutableLiveData.postValue(list);
            } else {
                Snackbar.make(view, "Erro ao carregar a lista de classificações : lista vazia", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public MutableLiveData<List<User>> getListMutableLiveData() {
        return listMutableLiveData;
    }
}
