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

import br.com.geoskills.model.Question;
import br.com.geoskills.repository.AuthRepository;
import br.com.geoskills.repository.FirestoreRepository;

import java.util.List;
import java.util.Objects;

public class GameViewModel extends AndroidViewModel {
    private final FirestoreRepository firestoreRepository;
    private final MutableLiveData<Boolean> isEndedQuiz;
    private final MutableLiveData<Boolean> isEndedCoordinate;
    private final MutableLiveData<Boolean> isEndedPuzzle;
    private final AuthRepository authRepository;
    private final MutableLiveData<List<Question>> listMutableLiveData;

    public GameViewModel(@NonNull Application application) {
        super(application);
        firestoreRepository = FirestoreRepository.getInstance();
        authRepository = AuthRepository.getInstance();
        isEndedQuiz = new MutableLiveData<>(false);
        isEndedCoordinate = new MutableLiveData<>(false);
        isEndedPuzzle = new MutableLiveData<>(false);
        listMutableLiveData = new MutableLiveData<>(null);
    }

    public void updatePointsUser(int points) {
        firestoreRepository.updatePointsUserOnDb(Objects.requireNonNull(authRepository.getCurrentUser().getValue()).getUid(), points, task -> {
            if (task.isSuccessful()) {
                isEndedQuiz.postValue(true);
                isEndedCoordinate.postValue(true);
                isEndedPuzzle.postValue(true);
            } else {
                Log.i("ERROR_UPDATE_POINTS", "Error : " + Objects.requireNonNull(task.getException()).getMessage());

            }
        });
    }

    public MutableLiveData<Boolean> getIsEndedQuiz() {
        return isEndedQuiz;
    }
    public MutableLiveData<Boolean> getIsEndedCoordinate() {
        return isEndedCoordinate;
    }

    public AlertDialog makeLoadingAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.loading_alert, null);
        AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    public void getAllQuestionsOnDb(View view) {
//        List<Question> list = new ArrayList<>();
//        firestoreRepository.getAllQuestionsOnDb(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    // colocar tratar melhor isso aq
//                    Snackbar.make(view, "Erro ao carregar a lista de classificações", Snackbar.LENGTH_LONG);
//                    return;
//                }
//                if (value != null) {
//                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
//                        Question question = documentSnapshot.toObject(Question.class);
//                        list.add(question);
//                    }
//                    listMutableLiveData.postValue(list);
//                } else {
//                    Snackbar.make(view, "Erro ao carregar a lista de classificações : lista vazia", Snackbar.LENGTH_LONG);
//                }
//
//            }
//        });

    }

    public MutableLiveData<List<Question>> getListMutableLiveData() {
        return listMutableLiveData;
    }
    public MutableLiveData<Boolean> getIsEndedPuzzle() {
        return isEndedPuzzle;
    }

}
