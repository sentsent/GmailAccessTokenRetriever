package pl.sentia.gmailaccesstokenretriever;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import pl.sentia.gmailaccesstokenretriever.databinding.ActivityMainBinding;
import pl.sentia.gmailaccesstokenretriever.model.ConnectionResults;
import pl.sentia.gmailaccesstokenretriever.viewmodel.ConnectionResultsViewModel;
import pl.sentia.gmailaccesstokenretriever.viewmodel.GoogleTokenService;
import pl.sentia.gmailaccesstokenretriever.viewmodel.TokenService;

public class MainActivity extends AppCompatActivity {

    public static Activity CURRENT_ACTIVITY;
    public static ConnectionResultsViewModel CONNECTION_VIEW_MODEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CURRENT_ACTIVITY = this;

        //Model
        ConnectionResults connectionResults = new ConnectionResults();

        //ViewModel
        CONNECTION_VIEW_MODEL = new ConnectionResultsViewModel(connectionResults);

        //Binding View to ViewModel
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setConnectionViewModel(CONNECTION_VIEW_MODEL);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TokenService googleTokenService = GoogleTokenService.getInstance();
        if (requestCode == GoogleTokenService.RC_AUTHORIZE_CONTACTS) {
            super.onActivityResult(requestCode, resultCode, data);
            googleTokenService.updateCredential(data);
            googleTokenService.runTokenRetrieveTask();
        }
    }



}
