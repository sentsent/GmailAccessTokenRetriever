package pl.sentia.gmailaccesstokenretriever;

import android.accounts.Account;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import pl.sentia.gmailaccesstokenretriever.model.ConnectionResults;
import pl.sentia.gmailaccesstokenretriever.viewmodel.ConnectionResultsViewModel;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static pl.sentia.gmailaccesstokenretriever.viewmodel.ConnectionResultsViewModel.GOOGLE_API_CLIENT;

public class RetrieveTokenTaskTest {
    @Test
    public void shouldReturnTokenWithAccount() throws Exception {
        //Given
        Account account = Mockito.mock(Account.class);
        GoogleAccountCredential credential = Mockito.mock(GoogleAccountCredential.class);
        GoogleSignInOptions googleSignInOptions = Mockito.mock(GoogleSignInOptions.class);
        RetrieveTokenTask retrieveTokenTask = new RetrieveTokenTask(
                new ConnectionResultsViewModel(new ConnectionResults()));
        retrieveTokenTask.setCredential(credential);
        retrieveTokenTask.setGoogleSignInOptions(googleSignInOptions);
        GOOGLE_API_CLIENT = Mockito.mock(GoogleApiClient.class);

        when(retrieveTokenTask.doInBackground(account)).thenReturn("sometoken");

        //When
        retrieveTokenTask.doInBackground(account);

        //Then
        assertNotNull(retrieveTokenTask.doInBackground(account));
    }

    @Test
    public void shouldNotSetConnectionResultsOnExceptions() throws Exception {
        //Given
        Account account = Mockito.mock(Account.class);
        GoogleAccountCredential credential = Mockito.mock(GoogleAccountCredential.class);
        GoogleSignInOptions googleSignInOptions = Mockito.mock(GoogleSignInOptions.class);
        ConnectionResults connectionResults = new ConnectionResults();
        RetrieveTokenTask retrieveTokenTask = new RetrieveTokenTask(
                new ConnectionResultsViewModel(connectionResults));
        retrieveTokenTask.setCredential(credential);
        retrieveTokenTask.setGoogleSignInOptions(googleSignInOptions);
        GOOGLE_API_CLIENT = Mockito.mock(GoogleApiClient.class);

        when(retrieveTokenTask.doInBackground(account)).thenThrow(new IOException());
        when(retrieveTokenTask.doInBackground(account)).thenThrow(new GoogleAuthException());

        //When
        retrieveTokenTask.doInBackground(account);

        //Then
        assertFalse(connectionResults.isConnected());
        assertNull(connectionResults.getObtainedToken());
    }

}