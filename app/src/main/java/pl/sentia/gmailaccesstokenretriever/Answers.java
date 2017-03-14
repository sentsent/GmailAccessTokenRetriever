package pl.sentia.gmailaccesstokenretriever;


import static pl.sentia.gmailaccesstokenretriever.MainActivity.CURRENT_ACTIVITY;

public enum Answers {

    YES {
        @Override
        public String toString() {
            return CURRENT_ACTIVITY.getString(R.string.yes);
        }
    },
    NO{
        @Override
        public String toString() {
            return CURRENT_ACTIVITY.getString(R.string.no);
        }
    };
}
