package com.pandora_latest.QubeAuth;

public class QubeAuthCallbacks {

    public interface SignInCallback {
        void onSignInSuccess(String userId);
        void onSignInFailure(String message);
    }

}
