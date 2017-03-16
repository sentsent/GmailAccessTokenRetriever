package pl.sentia.gmailaccesstokenretriever.viewmodel;

import android.content.Intent;

public interface TokenService {
    void runTokenRetrieveTask();
    void updateCredential(Intent data);
}
