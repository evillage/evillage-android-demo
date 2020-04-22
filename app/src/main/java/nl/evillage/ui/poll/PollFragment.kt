package nl.evillage.ui.poll

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_poll.*
import nl.evillage.ui.MainActivity
import nl.evillage.R

/**
 * A simple [Fragment] subclass.
 */
class PollFragment : Fragment() {

    private val mainActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    private var answer: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_question.text = "What is your favorite car color?"
        rg_poll.setOnCheckedChangeListener(radioCheckedChangeListener)
        btn_submit.setOnClickListener(submitClickListener)
    }

    private var radioCheckedChangeListener = RadioGroup.OnCheckedChangeListener { _, id ->
        answer = (view?.findViewById(id) as RadioButton).text.toString()
    }

    private var submitClickListener = View.OnClickListener {
        answer?.let { _answer ->
            btn_submit.isEnabled = false
            mainActivity.clang.logEvent(
                "pollSubmit",
                mapOf("title" to "FavoriteCarColor", "value" to _answer),
                {
                    showAlertDialog()
                },
                {
                    showAlertDialog(it)
                })
        } ?: Toast.makeText(requireContext(), "Please select a color", Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialog(throwable: Throwable? = null) {
        val builder = AlertDialog.Builder(requireContext())
        if (throwable != null) {
            builder.setTitle("Error!Error!Panic!")
            builder.setMessage(throwable.message)
            builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.cancel()
            }
        } else {
            builder.setTitle("Success")
            builder.setMessage("Favorite car color submitted")
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                findNavController().navigateUp()
            }
        }
        builder.show()
        btn_submit.isEnabled = true
    }
}
