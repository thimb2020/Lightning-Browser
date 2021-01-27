package acr.browser.lightning

import acr.browser.lightning.database.dao.model.Book
import acr.browser.lightning.database.dao.model.Chapter

class BookItem {
    lateinit var books: List<Book>
    lateinit var chapters: List<Chapter>
}