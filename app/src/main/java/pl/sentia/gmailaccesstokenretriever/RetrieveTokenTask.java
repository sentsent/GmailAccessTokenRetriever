package pl.sentia.gmailaccesstokenretriever;

import android.accounts.Account;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import java.io.IOException;
import java.util.Collections;
import pl.sentia.gmailaccesstokenretriever.viewmodel.ConnectionResultsViewModel;

import static pl.sentia.gmailaccesstokenretriever.viewmodel.ConnectionResultsViewModel.GOOGLE_API_CLIENT;

public class RetrieveTokenTask extends AsyncTask<Account, Void, String> {

    private ConnectionResultsViewModel connectionViewModel;
    private static final int RC_AUTHORIZE_CONTACTS = 500;
    private static final String CONTACTS_SCOPE = "https://www.googleapis.com/auth/contacts.readonly";
    private GoogleAccountCredential credential;
    private GoogleSignInOptions googleSignInOptions;

    public RetrieveTokenTask(ConnectionResultsViewModel connectionViewModel) {
        this.connectionViewModel = connectionViewModel;
    }

    @Override
    protected String doInBackground(Account... params) {
        Account account = params[0];
        initGoogleApiClient();
        return obtainGmailToken(account);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        updateViewModel(s);
    }

    public static Account getAuthorisedAccount(int requestCode, Intent data) {
        Log.i("RetrieveTokenTask", "After sign in request code: " + requestCode);
        if (requestCode == RC_AUTHORIZE_CONTACTS) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.i("RetrieveTokenTask", "After sign in result: " + result.isSuccess());
            if (result.isSuccess()) {
                GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
                return googleSignInAccount.getAccount();
            }
        }
        return null;
    }

    private void updateViewModel(String resultToken) {
        if (resultToken != null) {
            connectionViewModel.setConnected(true);
            connectionViewModel.setObtainedToken(resultToken);
        }
    }

    private void initGoogleApiClient() {
        if (GOOGLE_API_CLIENT == null) {
            googleSignInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .requestScopes(new Scope(CONTACTS_SCOPE))
                            .build();

            GOOGLE_API_CLIENT = new GoogleApiClient.Builder(MainActivity.CURRENT_ACTIVITY)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(GOOGLE_API_CLIENT);
            MainActivity.CURRENT_ACTIVITY.startActivityForResult(signInIntent, RC_AUTHORIZE_CONTACTS);
        }
    }

    private String obtainGmailToken(Account account) {
        String resultToken = null;
        if (account != null) {
            Log.i("RetrieveTokenTask", "Account name provided: " + account.name);

            if (credential == null) {
                credential = GoogleAccountCredential
                        .usingOAuth2(
                                MainActivity.CURRENT_ACTIVITY,
                                Collections.singleton(CONTACTS_SCOPE));
            }

            credential.setSelectedAccount(account);
            try {
                resultToken = credential.getToken();
                Log.i("RetrieveTokenTask", "Result token: " + resultToken);
            } catch (IOException e) {
                Log.i("RetrieveTokenTask", "Internet connection problem: " + e.getMessage());
            } catch (GoogleAuthException e) {
                Log.i("RetrieveTokenTask", "Authentication problem: " + e.getMessage());
            }
        }
        return resultToken;
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void setCredential(GoogleAccountCredential credential) {
        this.credential = credential;
    }

    public GoogleSignInOptions getGoogleSignInOptions() {
        return googleSignInOptions;
    }

    public void setGoogleSignInOptions(GoogleSignInOptions googleSignInOptions) {
        this.googleSignInOptions = googleSignInOptions;
    }
}