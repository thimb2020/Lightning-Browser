package acr.browser.lightning

import acr.browser.lightning.database.bookmark.BookmarkDatabase
import acr.browser.lightning.database.bookmark.BookmarkRepository
import acr.browser.lightning.database.dao.AppDatabase
import acr.browser.lightning.database.dao.model.Book
import acr.browser.lightning.database.dao.model.Chapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acr.browser.lightning.dummy.DummyContent
import android.util.Log
import javax.inject.Inject
import acr.browser.lightning.browser.bookmarks.BookmarksViewModel
import acr.browser.lightning.database.Bookmark

/**
 * A fragment representing a list of Items.
 */
class BookFragment : Fragment() {

    private var categoryId = -1
    private lateinit var db: AppDatabase;
    private var bookItems: ArrayList<BookItem> = ArrayList()

    @Inject
    internal lateinit var bookmarkModel: BookmarkRepository
    override fun onCreate(savedInstanceState: Bundle?) {
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
            with(view) {
/*                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }*/
                db = AppDatabase(context)
                val bookDao = db.bookDao
                var books: List<Book> = ArrayList()
                var chapters: List<Chapter> = ArrayList()
                if (categoryId == -1) {
                    chapters = bookDao?.getStartChapters()!!
                } else {
                    books = bookDao?.getBooksByCategory(categoryId)!!
                }


                val forEach = books?.forEach {
                    val di = BookItem()
                    di.isBook = true;
                    di.book = it
                    bookItems.add(di)
                }
                chapters.forEach {
                    val di = BookItem()
                    di.isBook = false;
                    di.chapter = it
                    bookItems.add(di)
                }
            }
            view.adapter = BookItemRecyclerViewAdapter(bookItems)
        }
        return view
    }

    companion object {
        // TODO: Customize parameter argument names
        const val CATEGORY_ID = "CategoryId"
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(CategoryId: Int) =
                BookFragment().apply {
                    arguments = Bundle().apply {
                        putInt(CATEGORY_ID, CategoryId)
                    }
                }
    }
}