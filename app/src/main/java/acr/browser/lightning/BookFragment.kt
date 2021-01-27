package acr.browser.lightning

import acr.browser.lightning.database.dao.AppDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acr.browser.lightning.dummy.DummyContent

/**
 * A fragment representing a list of Items.
 */
class BookFragment : Fragment() {

    private var categoryId = 1
    private lateinit var db: AppDatabase;
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
                val bookDao = db.bookDao;
                val books = bookDao?.getBooksByCategory(categoryId)
                val forEach = books?.forEach {
                    val di = it.name?.let { it1 -> DummyContent.DummyItem(it.id.toString(), it1, "") }

                }

                adapter = BookItemRecyclerViewAdapter(DummyContent.ITEMS)
            }
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