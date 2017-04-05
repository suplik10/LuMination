package cz.lumination.fitapp;

import android.content.Context;

/**
 * Created by mkasl on 03.04.2017.
 */

public class Application extends android.app.Application{
        private static Application instance;

        public static Application getInstance() {
            return instance;
        }

        public static Context getContext(){
            return instance;
        }

        @Override
        public void onCreate() {
            instance = this;
            super.onCreate();
        }
}
