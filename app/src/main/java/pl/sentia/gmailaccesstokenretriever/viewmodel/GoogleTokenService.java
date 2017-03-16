package pl.sentia.gmailaccesstokenretriever.viewmodel;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import java.io.IOException;
import java.util.Collections;
import pl.sentia.gmailaccesstokenretriever.MainActivity;

public class GoogleTokenService implements TokenService {

    private static TokenService service;
    private GoogleApiClient googleApiClient;
    public GoogleAccountCredential credential;
    private static final String CONTACTS_SCOPE = "https://www.googleapis.com/auth/contacts.readonly";
    public static final int RC_AUTHORIZE_CONTACTS = 500;

    private GoogleTokenService() {
        googleApiClient = new GoogleApiClient.Builder(MainActivity.CURRENT_ACTIVITY)
                .addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(CONTACTS_SCOPE))
                        .build())
                .build();

        credential = GoogleAccountCredential
                .usingOAuth2(
                        MainActivity.CURRENT_ACTIVITY,
                        Collections.singleton(CONTACTS_SCOPE));

        authorise();
    }

    public static synchronized TokenService getInstance() {
        if (service == null) {
            service = new GoogleTokenService();
        }
        return service;
    }

    private void authorise() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        MainActivity.CURRENT_ACTIVITY.startActivityForResult(signInIntent, RC_AUTHORIZE_CONTACTS);
    }

    @Override
    public void putTokenIntoConnectionModel() {
        AsyncTask<ConnectionResultsViewModel, Void, ConnectionViewTokenHolder> task
                = new AsyncTask<ConnectionResultsViewModel, Void, ConnectionViewTokenHolder>() {
            @Override
            protected ConnectionViewTokenHolder doInBackground(ConnectionResultsViewModel... params) {
                String resultToken = null;
                ConnectionResultsViewModel connectionResultsViewModel = params[0];

                if (credential != null && credential.getSelectedAccountName() != null) {
                    try {
                        resultToken = credential.getToken();
                    } catch (IOException e) {
                        Log.i("GoogleTokenService", "Internet connection problem: " + e.getMessage());
                    } catch (GoogleAuthException e) {
                        Log.i("GoogleTokenService", "Authentication problem: " + e.getMessage());
                    }
                }

                ConnectionViewTokenHolder connectionViewTokenHolder = new ConnectionViewTokenHolder();
                connectionViewTokenHolder.connectionResultsViewModel = connectionResultsViewModel;
                connectionViewTokenHolder.token = resultToken;

                return connectionViewTokenHolder;
            }

            @Override
            protected void onPostExecute(ConnectionViewTokenHolder connectionViewTokenHolder) {
                connectionViewTokenHolder.connectionResultsViewModel.updateViewModel(connectionViewTokenHolder.token);
            }
        };
        task.execute();
    }

    public void updateCredential(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        Log.i("GoogleTokenService", "sign in result: "+result.isSuccess());
        if (result.isSuccess()) {
            credential.setSelectedAccount(result.getSignInAccount().getAccount());
        }
    }

    private class ConnectionViewTokenHolder {
        public String token;
        public ConnectionResultsViewModel connectionResultsViewModel;
    }

}
