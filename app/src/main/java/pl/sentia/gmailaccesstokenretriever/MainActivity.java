package pl.sentia.gmailaccesstokenretriever;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import pl.sentia.gmailaccesstokenretriever.databinding.ActivityMainBinding;
import pl.sentia.gmailaccesstokenretriever.model.ConnectionResults;
import pl.sentia.gmailaccesstokenretriever.viewmodel.ConnectionResultsViewModel;

public class MainActivity extends AppCompatActivity {

    public static Activity CURRENT_ACTIVITY;
    private ConnectionResultsViewModel connectionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CURRENT_ACTIVITY = this;

        //Model
        ConnectionResults connectionResults = new ConnectionResults();

        //ViewModel
        connectionViewModel = new ConnectionResultsViewModel(connectionResults);

        //Binding View to ViewModel
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setConnectionViewModel(connectionViewModel);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        connectionViewModel.AUTHORISED_ACCOUNT = RetrieveTokenTask.getAuthorisedAccount(requestCode, data);
        connectionViewModel.connect();
    }

}
