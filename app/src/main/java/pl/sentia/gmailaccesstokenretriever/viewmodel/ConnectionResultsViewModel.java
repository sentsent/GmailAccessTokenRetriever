package pl.sentia.gmailaccesstokenretriever.viewmodel;

import android.accounts.Account;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import pl.sentia.gmailaccesstokenretriever.BR;
import pl.sentia.gmailaccesstokenretriever.RetrieveTokenTask;
import pl.sentia.gmailaccesstokenretriever.model.ConnectionResults;

public class ConnectionResultsViewModel extends BaseObservable {

    private ConnectionResults connectionResults;
    public static Account AUTHORISED_ACCOUNT;
    public static GoogleApiClient GOOGLE_API_CLIENT;

    public ConnectionResultsViewModel(ConnectionResults connectionResults) {
        this.connectionResults = connectionResults;
    }

    @Bindable
    public boolean isConnected() {
        return connectionResults.isConnected();
    }

    @Bindable
    public String getObtainedToken() {
      return connectionResults.getObtainedToken();
    }

    public void setConnected(boolean connected) {
        connectionResults.setConnected(connected);
        notifyPropertyChanged(BR.connected);
    }

    public void setObtainedToken(String token) {
        connectionResults.setObtainedToken(token);
        notifyPropertyChanged(BR.obtainedToken);
    }

    public void connect() {
        RetrieveTokenTask currentRetrieveTokenTask = new RetrieveTokenTask(this);
        currentRetrieveTokenTask.execute(AUTHORISED_ACCOUNT);
    }

}
