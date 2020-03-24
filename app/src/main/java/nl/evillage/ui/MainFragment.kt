package nl.evillage.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.provider.Settings.Secure
import android.widget.Toast
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import nl.evillage.R

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private val mainActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_register.setOnClickListener(registerClickListener)
        btn_login.setOnClickListener(loginClickListener)
        btn_poll.setOnClickListener(pollClickListener)
        btn_property.setOnClickListener(propertyClickistener)
    }

    private var registerClickListener = View.OnClickListener { view ->
        // UNIQUE DEVICE IDENTIFIER: prefer to use AdvertisingId
        val deviceId: String = Secure.ANDROID_ID
        mainActivity.clang.createAccount(deviceId, {
            Toast.makeText(requireContext(), view.id, Toast.LENGTH_LONG).show()
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



