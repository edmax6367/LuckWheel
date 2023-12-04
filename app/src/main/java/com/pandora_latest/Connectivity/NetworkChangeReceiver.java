package com.pandora_latest.Connectivity;

import com.pandora_latest.pandora_spin.R;
/*
public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String CONNECTIVITY_CHANGE_ACTION = "com.wheel.Connectivity.CONNECTIVITY_CHANGE_ACTION";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = getConnectivityStatusString(context);

        Intent broadcastIntent = new Intent(CONNECTIVITY_CHANGE_ACTION);
        broadcastIntent.putExtra("status", status);

        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }

    private String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi connected";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data connected";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "No internet connection";
        }
        return status;
    }
}

 */
