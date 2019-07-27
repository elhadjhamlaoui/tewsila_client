package com.tewsila.client;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by neo on 28/03/17.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        UserLocalStore local = new UserLocalStore(this);

        User user = local.getLoggedInUser();
        if (user!=null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


            // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();


            databaseReference.child(user.phone).child("token").setValue(refreshedToken);
        }
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       // sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

}