package acr.browser.lightning

import acr.browser.lightning.database.dao.model.Link
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import acr.browser.lightning.dummy.DummyContent.DummyItem
import acr.browser.lightning.favicon.FaviconModel
import acr.browser.lightning.loader.Constants
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import javax.inject.Inject

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class LinkItemRecyclerViewAdapter(
        private var values: List<Link>)
    : RecyclerView.Adapter<LinkItemRecyclerViewAdapter.ViewHolder>() {
    @Inject
    internal lateinit var faviconModel: FaviconModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        Static.getIL(holder.itemView.getContext()).displayImage(
                Constants.GOOGLE_FAVIICON_URL +item.url, holder.thumbImg, true);
        holder.nameTv.text = item.name
        holder.urlTv.text = item.url
        holder.itemView.setOnClickListener {
            Log.d("Click Item:", "")
            var intent = Intent(holder.itemView.getContext(), MainActivity::class.java).setData(item.url?.toUri())
            startActivity(holder.itemView.getContext(), intent, null)
        }
    }
    fun setData(dataList: List<Link>) {
        this.values= dataList //this datalist is a list defined in your adapter
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbImg: ImageView = view.findViewById(R.id.thumbImg)
        val nameTv: TextView = view.findViewById(R.id.nameTv)
        val urlTv: TextView = view.findViewById(R.id.urlTv)

        override fun toString(): String {
            return super.toString() + " '" + nameTv.text + "'"
        }
    }
}