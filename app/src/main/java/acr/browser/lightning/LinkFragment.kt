package acr.browser.lightning

import acr.browser.lightning.database.Bookmark
import acr.browser.lightning.database.bookmark.BookmarkExporter
import acr.browser.lightning.database.bookmark.BookmarkRepository
import acr.browser.lightning.database.dao.AppDatabase
import acr.browser.lightning.database.dao.model.Link
import acr.browser.lightning.di.injector
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import javax.inject.Inject
import java.text.Collator
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/**
 * A fragment representing a list of Items.
 */
class LinkFragment : Fragment() {

    private var categoryId = -1
    private lateinit var db: AppDatabase;
    private var links: List<Link> = ArrayList()

    @Inject
    internal lateinit var bookmarkModel: BookmarkRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(CATEGORY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)
        // Set the adapter
        if (view is RecyclerView) {
            var links: List<Link> = ArrayList()
            with(view) {
                db = AppDatabase(context)
                val newsDao = db.newsDao
                if (categoryId == -1) {
                    links = newsDao?.findLinksStart()!!
                    if (links.isEmpty()) {
                        links = newsDao?.findAllLink()!!
                    }
                } else {
                    links = newsDao?.findLinksByCategory(categoryId)!!
                }
            }
            var bookmarks = links.map { Bookmark.Entry(it.url!!, it.name!!, 0, Bookmark.Folder.Root) }
            //val assetsBookmarks = BookmarkExporter.importBookmarksFromAssets(this@BrowserApp)
            bookmarkModel.addBookmarkList(bookmarks).subscribe()
            //links = links.sortedWith(Comparator { t1, t2 -> Collator.getInstance(Locale.FRANCE).compare(t1.name,t2.name) })
            view.adapter = LinkItemRecyclerViewAdapter(links)
        }
        return view
    }

    companion object {
        // TODO: Customize parameter argument names
        const val CATEGORY_ID = "CategoryId"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(CategoryId: Int) =
                LinkFragment().apply {
                    arguments = Bundle().apply {
                        putInt(CATEGORY_ID, CategoryId)
                    }
                }
    }
}