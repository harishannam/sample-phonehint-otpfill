package com.harishannam.autootpread;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;

/**
 * TODO: Write Javadoc for SMSActivity.
 *
 * @author hannam
 */
public class SMSActivity extends AppCompatActivity implements SMSBroadcastReceiver.MessageBroadcastListener {

    private SMSBroadcastReceiver smsReceiver;

    @Override
    public void onMessageSuccess(String message) {
        Log.e("SMS ACTIVITY", message);

        TextView messageText = (TextView) findViewById(R.id.messageText);
        messageText.setText(message);

        TextView otpText = (TextView) findViewById(R.id.otpText);
        Pattern p = Pattern.compile("(\\d{6})");
        Matcher m = p.matcher(message);

        GifImageView imageView = (GifImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);

        GifImageView imageViewDone = (GifImageView) findViewById(R.id.imageViewDone);
        imageViewDone.setImageResource(R.drawable.done);
        imageViewDone.setVisibility(View.VISIBLE);

        TextView verifiedText = findViewById(R.id.verifiedText);
        verifiedText.setVisibility(View.VISIBLE);

        if(m.find())
            otpText.setText("Message Content : \n \n" + m.group(0));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        startSmsClient();
    }

    private void startSmsClient() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);

        Task<Void> task = client.startSmsRetriever();
        smsReceiver.setMessageBroadcastListener(this);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
