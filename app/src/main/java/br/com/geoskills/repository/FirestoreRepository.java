package br.com.geoskills.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.geoskills.enums.AuthEnum;
import br.com.geoskills.model.User;

public class FirestoreRepository {
    private final FirebaseFirestore firestoreDb;
    private final CollectionReference collectionUser;
    private static FirestoreRepository instance;
    private final MutableLiveData<User> userGetted;


    private FirestoreRepository() {
        firestoreDb = FirebaseFirestore.getInstance();
        collectionUser = firestoreDb.collection(AuthEnum.COLLECTION_USERS.getValue());
        userGetted = new MutableLiveData<>();
    }

    public static FirestoreRepository getInstance() {
        if (instance == null) {
            instance = new FirestoreRepository();
        }
        return instance;
    }

    public FirebaseFirestore getFirestoreDb() {
        return firestoreDb;
    }

    public MutableLiveData<User> getUserGetted() {
        return userGetted;
    }

    public void checkUserNameExists(String name, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        collectionUser.whereEqualTo(AuthEnum.NAME.getValue(), name).get().addOnCompleteListener(onCompleteListener);
    }

    public void registerUserOnDb(@NonNull User user, OnCompleteListener<Void> onCompleteListener) {
        collectionUser.document(user.getUuid()).set(user).addOnCompleteListener(onCompleteListener);
    }

    public void updateNameUserOnDb(@NonNull String UuID, String newName, OnCompleteListener<Void> onCompleteListener) {
        collectionUser.document(UuID).update(AuthEnum.NAME.getValue(), newName).addOnCompleteListener(onCompleteListener);
    }

    public void updateProfileImgUserOnDb(String UuID, int img, OnCompleteListener<Void> onCompleteListener) {
        collectionUser.document(UuID).update(AuthEnum.PROFILE_SELECTED.getValue(), img).addOnCompleteListener(onCompleteListener);
    }

    public void getUserOnDb(@NonNull String UuID, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        collectionUser.document(UuID).get().addOnCompleteListener(onCompleteListener);
    }

    public void updatePointsUserOnDb(@NonNull String UuID, int points, OnCompleteListener<Void> onCompleteListener) {
        collectionUser.document(UuID).update(AuthEnum.POINTS.getValue(), FieldValue.increment(points)).addOnCompleteListener(onCompleteListener);
    }

    public void getAllUsersOnDb(EventListener<QuerySnapshot> eventListener){
        collectionUser.orderBy(AuthEnum.POINTS.getValue(), Query.Direction.DESCENDING).addSnapshotListener(eventListener);
    }



}
