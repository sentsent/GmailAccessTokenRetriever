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

public class RetrieveTokenTask extends AsyncTask<Account, Void, Void> {

    private ConnectionResultsViewModel connectionViewModel;
    private static final int RC_AUTHORIZE_CONTACTS = 500;
    public String resultToken;

    public RetrieveTokenTask(ConnectionResultsViewModel connectionViewModel) {
        this.connectionViewModel = connectionViewModel;
    }

    @Override
    protected Void doInBackground(Account... params) {
        Account account = params[0];
        getGoogleApiClient();
        if (account != null) {
            Log.i("RetrieveTokenTask", "Account name provided: " + account.name);
            obtainGmailToken(account);
            updateViewModel();
        }

        return null;
    }


    private void updateViewModel() {
        if (resultToken != null) {
            connectionViewModel.setConnected(true);
            connectionViewModel.setObtainedToken(resultToken);
        }
    }

    private void getGoogleApiClient() {
        if (GOOGLE_API_CLIENT == null) {
            GoogleSignInOptions gso =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                            .requestScopes(new Scope("https://www.googleapis.com/auth/contacts.readonly"))
                            .build();

            GOOGLE_API_CLIENT = new GoogleApiClient.Builder(MainActivity.CURRENT_ACTIVITY)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(GOOGLE_API_CLIENT);
            MainActivity.CURRENT_ACTIVITY.startActivityForResult(signInIntent, RC_AUTHORIZE_CONTACTS);
        }
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

    private void obtainGmailToken(Account account) {
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        MainActivity.CURRENT_ACTIVITY,
                        Collections.singleton(
                                "https://www.googleapis.com/auth/contacts.readonly")
                );
        credential.setSelectedAccount(account);
        try {
            resultToken = credential.getToken();
            Log.i("RetrieveTokenTask", "Result token: " + resultToken);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        }
    }

}