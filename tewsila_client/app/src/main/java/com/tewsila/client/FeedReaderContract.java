package com.tewsila.client;

import android.provider.BaseColumns;

/**
 * Created by neo on 22/04/2017.
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "rides";
        public static final String pickup = "pickup";
        public static final String destination = "destination";
        public static final String platlng = "platlng";
        public static final String dlatlng = "dlatlng";
        public static final String vip = "vip";
    }
}