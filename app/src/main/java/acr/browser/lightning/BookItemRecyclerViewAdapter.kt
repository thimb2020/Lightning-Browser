package acr.browser.lightning

import acr.browser.lightning.database.dao.AppDatabase.Companion.invoke
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import acr.browser.lightning.dummy.DummyContent.DummyItem
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.two_line_autocomplete.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class BookItemRecyclerViewAdapter(
        private val values: List<BookItem>)
    : RecyclerView.Adapter<BookItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if (item.isBook) {
            holder.idView.text = item.book.id.toString()
            holder.contentView.text = item.book.name
        } else {
            holder.idView.text = item.chapter.id.toString()
            holder.contentView.text = item.chapter.name
        }
        holder.itemView.setOnClickListener {
            Log.d("Click Item:","")
            var intent = Intent (holder.itemView.getContext(), MainActivity::class.java).setData(item.chapter.url?.toUri())
           // var intent = Intent(holder.itemView.getContext(), IncognitoActivity::class.java);

           // val intent = IncognitoActivity.createIntent(holder.itemView.getContext(), values[position].chapter.url?.toUri())
            startActivity(holder.itemView.getContext(), intent, null)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}