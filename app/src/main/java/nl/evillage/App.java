package nl.evillage;

import android.app.Application;
import android.app.Activity;
import android.util.Log;

import nl.evillage.ui.MainActivity;
import nl.evillage.ui.MainFragment;
import nl.worth.clangnotifications.Clang;

public class App extends Application {

    private MainActivity mCurrentActivity = null;
    private MainFragment mCurrentFragment = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Clang.Companion.setUp(this, "https://api.clang.cloud","46b6dfb6-d5fe-47b1-b4a2-b92cbb30f0a5" , "63f4bf70-2a0d-4eb2-b35a-531da0a61b20");
    }

    public MainActivity getCurrentActivity () {
        return mCurrentActivity;
    }
    public void setCurrentActivity (MainActivity mCurrentActivity) {
        this . mCurrentActivity = mCurrentActivity;
    }

    //addd for acces to MainFragment
    public MainFragment getCurrentFragment () {
        return mCurrentFragment;
    }
    public void setCurrentFragment (MainFragment mCurrentFragment) {
        this.mCurrentFragment = mCurrentFragment;
    }
}
