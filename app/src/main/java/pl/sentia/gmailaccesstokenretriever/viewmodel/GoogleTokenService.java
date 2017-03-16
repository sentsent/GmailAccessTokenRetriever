package pl.sentia.gmailaccesstokenretriever.viewmodel;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import static pl.sentia.gmailaccesstokenretriever.MainActivity.CONNECTION_VIEW_MODEL;

public class GoogleTokenService implements TokenService {

    public static final int RC_AUTHORIZE_CONTACTS = 500;
    private static final GoogleTokenService INSTANCE = new GoogleTokenService();
    private static final String CONTACTS_SCOPE = "https://www.googleapis.com/auth/contacts.readonly";
    public GoogleAccountCredential credential;
    private GoogleApiClient googleApiClient;

    private GoogleTokenService() {
    }

    public static GoogleTokenService getInstance() {
        return INSTANCE;
    }

    public void authorise() {
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

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        MainActivity.CURRENT_ACTIVITY.startActivityForResult(signInIntent, RC_AUTHORIZE_CONTACTS);
    }

    @Override
    public void runTokenRetrieveTask() {
        getTokenTask().execute();
    }

    @NonNull
    private AsyncTask<Void, Void, String> getTokenTask() {
        return new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String resultToken = null;
                if (credential != null && credential.getSelectedAccountName() != null) {
                    try {
                        resultToken = credential.getToken();
                    } catch (IOException e) {
                        Log.i("GoogleTokenService", "Internet connection problem: " + e.getMessage());
                    } catch (GoogleAuthException e) {
                        Log.i("GoogleTokenService", "Authentication problem: " + e.getMessage());
                    }
                }
                return resultToken;
            }

            @Override
            protected void onPostExecute(String token) {
                CONNECTION_VIEW_MODEL.updateViewModel(token);
            }
        };
    }

    public void updateCredential(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        Log.i("GoogleTokenService", "sign in result: "+result.isSuccess());
        if (result.isSuccess()) {
            credential.setSelectedAccount(result.getSignInAccount().getAccount());
        }
    }
}
