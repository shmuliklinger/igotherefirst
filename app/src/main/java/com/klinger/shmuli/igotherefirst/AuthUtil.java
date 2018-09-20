package com.klinger.shmuli.igotherefirst;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

class AuthUtil {
    private static final AuthUtil ourInstance = new AuthUtil();

    private AuthUtil() {
    }

    static AuthUtil getInstance() {
        return ourInstance;
    }

    public void signIn(Context _context) {

        final Context context = _context;
        AWSMobileClient.getInstance().initialize(context, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                AuthUIConfiguration config =
                        new AuthUIConfiguration.Builder()
                                .userPools(true)  // true? show the Email and Password UI
                                .logoResId(R.drawable.my_logo) // Change the logo
                                .backgroundColor(Color.BLUE) // Change the backgroundColor
                                .isBackgroundColorFullScreen(true) // Full screen backgroundColor the backgroundColor full screenff
                                .fontFamily("sans-serif-light") // Apply sans-serif-light as the global font
                                .canCancel(true)
                                .build();
                SignInUI signinUI = (SignInUI) AWSMobileClient.getInstance().getClient(context, SignInUI.class);
                signinUI.login((Activity) context, MainActivity.class).authUIConfiguration(config).execute();
            }
        }).execute();
    }

    public void signOut(Context _context) {
        IdentityManager.getDefaultIdentityManager().signOut();
        signIn(_context);
    }
}
