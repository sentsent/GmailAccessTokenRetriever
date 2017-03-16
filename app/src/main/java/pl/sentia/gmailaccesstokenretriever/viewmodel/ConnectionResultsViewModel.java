package pl.sentia.gmailaccesstokenretriever.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import pl.sentia.gmailaccesstokenretriever.BR;
import pl.sentia.gmailaccesstokenretriever.model.ConnectionResults;

public class ConnectionResultsViewModel extends BaseObservable {

    private ConnectionResults connectionResults;

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
        GoogleTokenService googleTokenService = GoogleTokenService.getInstance();
        googleTokenService.authorise();
    }

    public void updateViewModel(String resultToken) {
        if (resultToken != null) {
            setConnected(true);
            setObtainedToken(resultToken);
        }
    }

}
