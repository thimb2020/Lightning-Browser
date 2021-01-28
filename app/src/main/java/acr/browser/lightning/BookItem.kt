package acr.browser.lightning

import acr.browser.lightning.database.dao.model.Book
import acr.browser.lightning.database.dao.model.Chapter

class BookItem {
    lateinit  var book: Book
    lateinit var chapter: Chapter
    var isBook: Boolean=true
}