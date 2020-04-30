package nl.evillage.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import nl.evillage.ui.MainActivity
import nl.evillage.R

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener(submitClickListener)
    }

    private var submitClickListener = View.OnClickListener {
        when {
            et_email.text.isNullOrEmpty() -> {
                et_email.setError("Email address not filled in!", requireContext().getDrawable(
                    R.drawable.ic_error
                ))
                et_email.requestFocus()
            }
            et_password.text.isNullOrEmpty() -> {
                et_password.setError("Password not filled in!", requireContext().getDrawable(
                    R.drawable.ic_error
                ))
                et_password.requestFocus()
            }
            else -> {
                val mainActivity = activity as MainActivity
                mainActivity.clang.logEvent("login", mapOf("title" to "login","userEmail" to et_email.text.toString()), {
                    showAlertDialog()
                }, {
                    showAlertDialog(it)
                })
            }
        }
    }

    private fun showAlertDialog(throwable: Throwable? = null) {
        val builder = AlertDialog.Builder(requireContext())

        if (throwable == null) {
            builder.setTitle("Success")
            builder.setMessage("You're logged in successfully")
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                findNavController().popBackStack()
            }
        } else {
            builder.setTitle("Error!Error!Panic!")
            builder.setMessage(throwable.message)

            builder.setPositiveButton(android.R.string.yes) { dialog, _ ->
                dialog.cancel()
            }
        }
        builder.show()
    }
}
