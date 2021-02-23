/*
 * Copyright 2014 A.C.R. Development
 */
package acr.browser.lightning.settings.fragment

import acr.browser.lightning.AppTheme
import acr.browser.lightning.R
import acr.browser.lightning.database.dao.AppDatabase
import acr.browser.lightning.di.injector
import acr.browser.lightning.dialog.BrowserDialog
import acr.browser.lightning.extensions.resizeAndShow
import acr.browser.lightning.extensions.withSingleChoiceItems
import acr.browser.lightning.preference.UserPreferences
import acr.browser.lightning.search.SearchEngineProvider
import acr.browser.lightning.search.engine.BaseSearchEngine
import acr.browser.lightning.search.engine.CustomSearch
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import javax.inject.Inject
import kotlin.collections.toTypedArray as collectionsToTypedArray

class TranslateSettingsFragment : AbstractSettingsFragment() {

    @Inject internal lateinit var userPreferences: UserPreferences
    @Inject lateinit var searchEngineProvider: SearchEngineProvider
    override fun providePreferencesXmlResource() = R.xml.preference_translate
    private lateinit var db: AppDatabase;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
        db = AppDatabase(this.activity);
        // preferences storage
        val translatePref = findPreference(SETTINGS_TRANSLATE)
        var countryId =userPreferences.translateChoice
        if (countryId == -1) {
            val locale = getActivity().getResources().getConfiguration().locale.getLanguage();
            val country = db.countryDao?.findByCode(locale)
            countryId = country!!.id;
            userPreferences.translateChoice = countryId
        }
        val country = db.countryDao?.findById(countryId)
        clickableDynamicPreference(
                preference = SETTINGS_TRANSLATE,
                summary = country?.name,
                onClick = ::showSearchProviderDialog
        )

    }

    private fun getSearchEngineSummary(baseSearchEngine: BaseSearchEngine): String {
        return if (baseSearchEngine is CustomSearch) {
            baseSearchEngine.queryUrl
        } else {
            getString(baseSearchEngine.titleRes)
        }
    }
    private fun showSearchProviderDialog(summaryUpdater: SummaryUpdater) {
        BrowserDialog.showCustomDialog(activity) {
            setTitle(resources.getString(R.string.title_translate))

            val countryDao = db.countryDao;
            val countries = countryDao?.getAll()
            val chars: Array<CharSequence?> = countries?.map { it.name }!!.collectionsToTypedArray()

            //val chars = convertSearchEngineToStringx(searchEngineList)

            val n = userPreferences.translateChoice-1

            setSingleChoiceItems(chars, n) { _, which ->
                val country = countries[which]

                // Store the search engine preference
                val preferencesIndex = country.id
                userPreferences.translateChoice = preferencesIndex
                //showCustomSearchDialog(country)
                country.name?.let { it1 -> summaryUpdater.updateSummary(it1) }
/*                if (searchEngine is CustomSearch) {
                    // Show the URL picker
                    showCustomSearchDialog(searchEngine, summaryUpdater)
                } else {
                    // Set the new search engine summary
                    summaryUpdater.updateSummary(getSearchEngineSummary(searchEngine))
                }*/
            }
            setPositiveButton(R.string.action_ok, null)
        }
    }
    private fun convertSearchEngineToString(searchEngines: List<BaseSearchEngine>): Array<CharSequence> =
            searchEngines.map { getString(it.titleRes) }.collectionsToTypedArray()
    private fun showCustomSearchDialog(customSearch: CustomSearch, summaryUpdater: SummaryUpdater) {
        activity?.let {
            BrowserDialog.showEditText(
                    it,
                    R.string.search_engine_custom,
                    R.string.search_engine_custom,
                    userPreferences.searchUrl,
                    R.string.action_ok
            ) { searchUrl ->
                userPreferences.searchUrl = searchUrl
                summaryUpdater.updateSummary(getSearchEngineSummary(customSearch))
            }

        }
    }
    companion object {
        private const val SETTINGS_TRANSLATE = "translate"
    }
}
