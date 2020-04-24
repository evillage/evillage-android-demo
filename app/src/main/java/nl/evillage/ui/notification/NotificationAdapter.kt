package nl.evillage.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.worth.clangnotifications.data.model.ClangNotification

class NotificationAdapter(private val list: List<ClangNotification.ClangAction>, private val actionClickListener: ActionClickListener)
    : RecyclerView.Adapter<ActionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ActionViewHolder(inflater, parent, actionClickListener)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val action: ClangNotification.ClangAction = list[position]
        holder.bind(action)
    }

    override fun getItemCount(): Int = list.size

    interface ActionClickListener{
        fun onActionClicked(action: ClangNotification.ClangAction)
    }
}