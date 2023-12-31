package com.pandora_latest.RewardCenter;

public class InviteListenerCallbacks {

    public interface RewardedCallback {
        void onRewardedSuccess(String userId);
        void onRewardedFailure(String message);
    }




}
