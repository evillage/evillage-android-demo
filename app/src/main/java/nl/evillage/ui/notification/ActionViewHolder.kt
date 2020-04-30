package nl.evillage.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import nl.evillage.R
import nl.worth.clangnotifications.data.model.ClangNotification

class ActionViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val actionClickListener: NotificationAdapter.ActionClickListener
) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.action_item, parent, false)) {

    private var button: Button = itemView.findViewById(R.id.btn)
    lateinit var action: ClangNotification.ClangAction

    init {
        button.setOnClickListener { actionClickListener.onActionClicked(action) }
    }

    fun bind(action: ClangNotification.ClangAction) {
        this.action = action
        button.text = action.title
    }

}