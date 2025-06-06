package com.oppenablers.jobhub;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class AuthManager {

    private static final FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();

    public static FirebaseUser getCurrentUser() {
        return FIREBASE_AUTH.getCurrentUser();
    }

    public static Task<GetTokenResult> getIdToken(boolean forceRefresh) {
        return getCurrentUser().getIdToken(forceRefresh);
    }

    public static boolean isLoggedIn() {
        return FIREBASE_AUTH.getCurrentUser() != null;
    }

    public static Task<AuthResult> login(String email, String password) {
        return FIREBASE_AUTH.signInWithEmailAndPassword(email, password);
    }

    public static Task<AuthResult> signup(String email, String password) {
        return FIREBASE_AUTH.createUserWithEmailAndPassword(email, password);
    }
}
