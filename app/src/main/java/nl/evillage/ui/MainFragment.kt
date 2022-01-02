package nl.evillage.ui

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import android.service.notification.StatusBarNotification
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import nl.evillage.App
import nl.evillage.R
import nl.evillage.views.Functions


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment()  {

    var mMyApp: App? = null
    private val mainActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private var ticketClickListener = View.OnClickListener { view ->


        val payload = "eyJraW5kIjoiaW4tYXBwLW1lc3NhZ2UiLCJ2ZXJzaW9uIjoxLCJtZXRhZGF0YSI6eyJpZCI6Il9hdlJocDZlbnhhRFNqTWFfNGtScllsRk9qb0V4S3QtWUJCSnZqbTZXZHFVS1hINkNfckowNlhXaWlUV2lPY0U5M3hWeDVlc0M2aXZ5OVQ1NUVGcEpaaGdDalRLdnBGb3Z1Ulh4RkZQQmkzSUo3Uy1UM01Jb3ZDOXNWbyIsIm5hbWUiOiJJbi1BcHAgTWVzc2FnZSBLbmFiIFJla2VuaW5nIiwidGltZXN0YW1wIjoiMjAyMS0xMC0yOVQxMjo0MzowOCswMjowMCJ9LCJib2R5Ijp7InR5cGUiOiJ0ZXh0XC9odG1sIiwiY29udGVudCI6IjwhRE9DVFlQRSBodG1sPjxodG1sPjxoZWFkPjxtZXRhIG5hbWU9XCJ2aWV3cG9ydFwiY29udGVudD1cIndpZHRoPWRldmljZS13aWR0aCxpbml0aWFsLXNjYWxlPTEuMCxtYXhpbXVtLXNjYWxlPTEuMCx1c2VyLXNjYWxhYmxlPW5vXCJcLz48bGluayBocmVmPVwiaHR0cHM6XC9cL2ZvbnRzLmdvb2dsZWFwaXMuY29tXC9jc3M/ZmFtaWx5PVBUK1NhbnM6NDAwLDcwMFwiIHJlbD1cInN0eWxlc2hlZXRcIj48c3R5bGU+dGFibGUge3dpZHRoOjEwMCU7Zm9udC1zaXplOjE3cHg7Zm9udC1mYW1pbHk6UFQgU2FucyxIZWx2ZXRpY2EsQXJpYWwsc2Fucy1zZXJpZjtjb2xvcjojMDAzYjVhO2xpbmUtaGVpZ2h0OjI0cHg7fS50aXRsZXtmb250LXNpemU6MjBweDt9LmJ1dHRvbnt0ZXh0LWFsaWduOmxlZnQ7Y29sb3I6IzAwYTRhNztmb250LXdlaWdodDpib2xkO3RleHQtZGVjb3JhdGlvbjpub25lO308XC9zdHlsZT48XC9oZWFkPjxib2R5Pjx0YWJsZT48dHI+PHRkPjx0YWJsZT48dHI+PHRkIHdpZHRoPVwiNDBweDtcIj48aW1nIHNyYz1cImh0dHBzOlwvXC9pLmliYi5jb1wvZ2diRnlUVFwvQXNzZXQtMzYwLTEwLVIucG5nXCIgd2lkdGg9XCIzNXB4XCI+PFwvdGQ+PHRkIGNsYXNzPVwidGl0bGVcIj48Yj5QYXJ0aWN1bGllcmUgcmVrZW5pbmcgb3BlbmVuPFwvYj48XC90ZD48XC90cj48XC90YWJsZT48XC90ZD48XC90cj48dHI+PHRkPk5hYXN0IHpha2VsaWprIG9vayBwcml2XHUwMGU5IGJpaiBvbnMgYmFua2llcmVuPyBEcnVrIGluIGRlIEtuYWIgQXBwIG9wIGhldCBwbHVzamUgcmVjaHRzYm92ZW4gZW4ga2llcyB2b29yIFx1MjAxOGJldGFhbHJla2VuaW5nXHUyMDE5LiBTdGFwIG51IG92ZXIgZW4gb250dmFuZyBcdTIwYWMgNTAhPFwvdGQ+PHRyPjx0ZCBjbGFzcz1cImJ1dHRvblwiPjxcL3RkPjxcL3RyPjxcL3RyPjxcL3RhYmxlPjxpbWcgc3JjPVwiZGF0YTppbWFnZVwvcG5nO2Jhc2U2NCxpVkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQUVBQUFBQkNBWUFBQUFmRmNTSkFBQUJoR2xEUTFCSlEwTWdjSEp2Wm1sc1pRQUFLSkY5a1QxSXcwQWN4VjlUcFNvVkJ6dUlPQVNzVGhaRWl6aHFGWXBRSWRRS3JUcVlYUG9oTkdsSVVsd2NCZGVDZ3grTFZRY1haMTBkWEFWQjhBUEV6YzFKMFVWS1wvRjlTYUJIandYRVwvM3QxNzNMMERoSHFaYVZiSE9LRHB0cGxPSnNSc2JrVU12YUliQWtJWVJseG1sakVyU1NuNGpxOTdCUGg2RitOWlwvdWYrSEwxcTNtSkFRQ1NlWVlacEU2OFRUMjNhQnVkOTRnZ3J5U3J4T2ZHWVNSY2tmdVM2NHZFYjU2TExBcytNbUpuMEhIR0VXQ3kyc2RMR3JHUnF4SEhpcUtycGxDOWtQVlk1YjNIV3lsWFd2Q2RcL1lUaXZMeTl4bmVZUWtsakFJaVNJVUZERkJzcXdFYU5WSjhWQ212WVRQdjVCMXkrUlN5SFhCaGc1NWxHQkJ0bjFnXC9cL0I3MjZ0d3VTRWx4Uk9BSjB2anZNeEFvUjJnVWJOY2I2UEhhZHhBZ1NmZ1N1OTVhXC9VZ2VsUDBtc3RMWG9FOUcwREY5Y3RUZGtETG5lQWdTZERObVZYQ3RJVUNnWGdcL1l5K0tRZjAzd0k5cTE1dnpYMmNQZ0FaNmlwMUF4d2NBcU5GeWw3emVYZFhlMlwvXC9ubW4yOXdOeW9YS25HOUFraXdBQUFBWmlTMGRFQVA4QVwvd0RcL29MMm5rd0FBQUFsd1NGbHpBQUF1SXdBQUxpTUJlS1VcL2RnQUFBQWQwU1UxRkIrVUpBZ2NrSzNWZmhBZ0FBQUFaZEVWWWRFTnZiVzFsYm5RQVEzSmxZWFJsWkNCM2FYUm9JRWRKVFZCWGdRNFhBQUFBQzBsRVFWUUkxMk5nQUFJQUFBVUFBZUltQlpzQUFBQUFTVVZPUks1Q1lJST1cIiB3aWR0aD1cIjFcIiBoZWlnaHQ9XCIxXCIgYm9yZGVyPVwiMFwiIGFsdD1cIlwiIHN0eWxlPVwiaGVpZ2h0OjFweCAhaW1wb3J0YW50O3dpZHRoOjFweCAhaW1wb3J0YW50O2JvcmRlcjogMCAhaW1wb3J0YW50O21hcmdpbjogMCAhaW1wb3J0YW50O3BhZGRpbmc6IDAgIWltcG9ydGFudFwiIFwvPjxcL2JvZHk+PFwvaHRtbD4ifSwiYWN0aW9ucyI6W3siaWQiOiJjbG9zZSIsImljb24iOiJkYXRhOmltYWdlXC9wbmc7YmFzZTY0LCIsInRpdGxlIjoiQ2xvc2UgbWVzc2FnZSJ9XX0="
        val mainConstraintLayout = this.layoutInflater.inflate(nl.evillage.R.layout.fragment_main, null) as ConstraintLayout
        Functions.buildTheTickets(parent = mainActivity, toAdd = payload, mainConstraintLayout)


    }

    open fun callTicket(payload : String) {
        val mainConstraintLayout = this.layoutInflater.inflate(nl.evillage.R.layout.fragment_main, null) as ConstraintLayout
        Handler(Looper.getMainLooper()).post(Runnable { //do stuff like remove view etc
            Functions.buildTheTickets(parent = mainActivity, toAdd = payload, mainConstraintLayout)
        })

    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mMyApp = mainActivity.applicationContext as App
        mMyApp?.currentFragment = this

        btn_register.setOnClickListener(registerClickListener)
        btn_login.setOnClickListener(loginClickListener)
        btn_poll.setOnClickListener(pollClickListener)
        btn_property.setOnClickListener(propertyClickistener)
        btn_callticket.setOnClickListener(ticketClickListener)






        super.onViewCreated(view, savedInstanceState)
    }



    private var registerClickListener = View.OnClickListener { view ->
        // UNIQUE DEVICE IDENTIFIER: prefer to use AdvertisingId
        val deviceId: String = Secure.ANDROID_ID
        mainActivity.clang.createAccount(deviceId, {
            Toast.makeText(requireContext(), it.id, Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        })
    }

    private var loginClickListener = View.OnClickListener { view ->
        val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
        view.findNavController().navigate(action)
    }

    private var pollClickListener = View.OnClickListener { view ->
        val action = MainFragmentDirections.actionMainFragmentToPollFragment()
        view.findNavController().navigate(action)
    }

    private var propertyClickistener = View.OnClickListener {
        mainActivity.clang.updateProperties(mapOf("pizzaPreference" to "Calzone"), {
            Toast.makeText(requireContext(), "Pizza preference submitted", Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        })
    }
}



