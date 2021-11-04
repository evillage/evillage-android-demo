# TeamITG - Android CanopyDeploy Demo
This app is the demo app of the CanopyDeploy Android library. It shows a simple UI to demo the uses of the CanopyDeploy library. For the library see: https://github.com/evillage/evillage-android

In this demo setup we used jitpack.io as the source for the library package.

## How to run the application
* Clone this repository and open it in Android Studio
* Run on a virtual device or connect Android phone with Android version 5.0 or higher

## How to use the application ticket system

* please look at the App.java where we make acces to the MainActivity and MainFragment for use and future possible use

        private MainActivity mCurrentActivity = null;
        private MainFragment mCurrentFragment = null;
    
        public MainActivity getCurrentActivity () {
             return mCurrentActivity;
         }
        public void setCurrentActivity (MainActivity mCurrentActivity) {
            this . mCurrentActivity = mCurrentActivity;
         }

        public MainFragment getCurrentFragment () {
               return mCurrentFragment;
        }
        public void setCurrentFragment (MainFragment mCurrentFragment) {
            this.mCurrentFragment = mCurrentFragment;
        }
    
* and in MainActivity setting the currentActivity in onCreate

        var mMyApp: App? = null
    
    (in onCreate)
    
              mMyApp = this.applicationContext as App
              mMyApp?.currentActivity = this
         
 * and in MainFragment setting the MainFragment in onViewCreated

        var mMyApp: App? = null
      
          (in onViewCreated)
          
                mMyApp = mainActivity.applicationContext as App
                mMyApp?.currentFragment = this
       
* then look at the fragment_main.xml where a LinearLayout is added to the content of the view to make suren the id="@+id/cp_Layout" can be found an filled

 <LinearLayout
        android:id="@+id/cp_Layout"
        android:layout_width="match_parent"
        android:layout_height="fill_parent"
        android:layout_gravity="top"
        android:layout_weight="1"
        android:gravity="center|top"
        android:orientation="vertical"
        android:tag="cp_Layout"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <LinearLayout

            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center|top"
            android:orientation="vertical">


            <androidx (all items inside this layout) />

        </LinearLayout>


    </LinearLayout>
    
* in ClangFirebaseMessagingService in onMessageReceived look at the connection between ClangFirebaseMessagingService and the currentFragment in App

                if (remoteMessage.data.keys.contains("cd_payload")) {
                        val mFragment: MainFragment = (applicationContext as App).currentFragment
                        mFragment.callTicket(remoteMessage.data["cd_payload"].toString())
                 }
 
 
* then the final callTicket and its handling of the payload

        open fun callTicket(payload : String) {
          val mainConstraintLayout = this.layoutInflater.inflate(nl.evillage.R.layout.fragment_main, null) as ConstraintLayout
         Handler(Looper.getMainLooper()).post(Runnable { //do stuff like remove view etc
                Functions.buildTheTickets(parent = mainActivity, toAdd = payload, mainConstraintLayout)
         })

        }

        
