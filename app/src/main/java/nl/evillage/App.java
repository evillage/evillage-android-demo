package nl.evillage;

import android.app.Application;

import nl.worth.clangnotifications.Clang;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Clang.Companion.setUp(this, "https://api.clang.cloud","46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5" , "63f4bf70-2a0d-4eb2-b35a-531da0a61b20");
    }
}
