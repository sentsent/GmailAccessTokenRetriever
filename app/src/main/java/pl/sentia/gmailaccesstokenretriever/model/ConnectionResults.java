package pl.sentia.gmailaccesstokenretriever.model;

public class ConnectionResults {

    private boolean connected = false;
    private String obtainedToken;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getObtainedToken() {
        return obtainedToken;
    }

    public void setObtainedToken(String obtainedToken) {
        this.obtainedToken = obtainedToken;
    }
}
