package com.harishannam.autootpread;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "PhoneHintActivity";
    private static final int RC_PHONE_HINT = 22;

    private EditText phoneNumber;
    private Button btnReset;
    private Button btnClear;
    private Button btnVerify;

    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showPhoneAutoCompleteHint();

        phoneNumber = (EditText) findViewById(R.id.editText2);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnVerify = (Button) findViewById(R.id.btnVerifyPhone);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SMSActivity.class));
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.getText().clear();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneAutoCompleteHint();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        client.stopAutoManage(this);
        client.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHONE_HINT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    com.google.android.gms.auth.api.credentials.Credential cred = data.getParcelableExtra(com.google.android.gms.auth.api.credentials.Credential.EXTRA_KEY);
                    if (cred != null) {
                        final String unformattedPhone = cred.getId();
                        phoneNumber.getText().clear();
                        phoneNumber.setText(unformattedPhone);
                        client.stopAutoManage(this);
                        client.disconnect();
                    }
                }
            }
        }
    }

    private void showPhoneAutoCompleteHint() {
        try {
            startIntentSenderForResult(getPhoneHintIntent().getIntentSender(), RC_PHONE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Unable to start hint intent", e);
        }
    }

    private PendingIntent getPhoneHintIntent() {
        client = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .enableAutoManage(
                        this,
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Log.e("MAIN ACTIVITY",
                                        "Client connection failed: " + connectionResult.getErrorMessage());
                            }
                        })
                .build();

        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(
                        new CredentialPickerConfig.Builder().setShowCancelButton(true).build())
                .setPhoneNumberIdentifierSupported(true)
                .setEmailAddressIdentifierSupported(false)
                .build();

        return Auth.CredentialsApi.getHintPickerIntent(client, hintRequest);
    }
}
