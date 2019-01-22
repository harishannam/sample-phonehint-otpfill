package com.harishannam.autootpread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.lang.ref.WeakReference;

/**
 * TODO: Write Javadoc for SMSBroadcastReceiver.
 *
 * @author hannam
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

    public interface MessageBroadcastListener {
        void onMessageSuccess(String message);
    }

    private static WeakReference<MessageBroadcastListener> mListener;

    public static void setMessageBroadcastListener(MessageBroadcastListener listener) {
        mListener = new WeakReference<MessageBroadcastListener>(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    if (mListener != null) {
                        mListener.get().onMessageSuccess(message);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    break;
            }
        }
    }
}
