package pl.sentia.gmailaccesstokenretriever.viewmodel;

import android.content.Intent;

public interface TokenService {
    void putTokenIntoConnectionModel();
    void updateCredential(Intent data);
}
