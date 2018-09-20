package com.klinger.shmuli.igotherefirst;

import android.app.Activity;
import android.os.Bundle;

public class AuthenticatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        AuthUtil authUtil = AuthUtil.getInstance();
        authUtil.signIn(this);
    }
}
