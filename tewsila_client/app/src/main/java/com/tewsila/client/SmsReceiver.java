package com.tewsila.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by elhadj on 03/02/2018.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            Bundle data  = intent.getExtras();
            if (data!=null){
                Object[] pdus = (Object[]) data.get("pdus");
                if (pdus!=null)
                    for(int i=0;i<pdus.length;i++){
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        //You must check here if the sender is your provider and not another one with same text.

                        String messageBody = smsMessage.getMessageBody();

                        //Pass on the text to our listener.
                        if (messageBody!=null)
                            mListener.messageReceived(messageBody);
                    }

            }
        }catch (NullPointerException e){

        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}