package nl.evillage.poll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.evillage.R

class QuestionsAdapter(private val answers: List<QuestionItem>): RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    var onItemClick: ((QuestionItem) -> Unit)? = null


    override fun getItemCount() = answers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.answer_row, parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(answers[position])
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.question_text)

        fun onBind(item: QuestionItem) {
            text.text = item.text
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(answers[adapterPosition])
            }
        }
    }
}