package com.stripe.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.core.os.ConfigurationCompat
import com.stripe.android.R
import java.util.Locale
import kotlinx.android.synthetic.main.country_autocomplete_textview.view.*

internal class CountryAutoCompleteTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    @VisibleForTesting
    internal val countryAutocomplete: AutoCompleteTextView

    /**
     * @return 2 digit country code of the country selected by this input.
     */
    @VisibleForTesting
    var selectedCountry: Country?

    @JvmSynthetic
    internal var countryChangeCallback: (Country) -> Unit = {}

    private val countryAdapter: CountryAdapter

    init {
        View.inflate(getContext(), R.layout.country_autocomplete_textview, this)

        countryAdapter = CountryAdapter(
            getContext(),
            CountryUtils.getOrderedCountries(
                ConfigurationCompat.getLocales(context.resources.configuration)[0]
            )
        )

        countryAutocomplete = findViewById(R.id.autocomplete_country_cat)
        countryAutocomplete.threshold = 0
        countryAutocomplete.setAdapter(countryAdapter)
        countryAutocomplete.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                updatedSelectedCountryCode(countryAdapter.getItem(position))
            }
        countryAutocomplete.onFocusChangeListener = OnFocusChangeListener { _, focused ->
            if (focused) {
                countryAutocomplete.showDropDown()
            } else {
                val countryEntered = countryAutocomplete.text.toString()
                updateUiForCountryEntered(countryEntered)
            }
        }

        selectedCountry = countryAdapter.firstItem
        updateInitialCountry()

        val errorMessage = resources.getString(R.string.address_country_invalid)
        countryAutocomplete.validator = object : AutoCompleteTextView.Validator {
            override fun fixText(invalidText: CharSequence?): CharSequence {
                return invalidText ?: ""
            }

            override fun isValid(text: CharSequence?): Boolean {
                val validCountry = countryAdapter.unfilteredCountries.firstOrNull {
                    it.name == text.toString()
                }

                selectedCountry = validCountry

                if (validCountry != null) {
                    clearError()
                } else {
                    tl_country_cat.error = errorMessage
                    tl_country_cat.isErrorEnabled = true
                }

                return validCountry != null
            }
        }
    }

    private fun updateInitialCountry() {
        val initialCountry = countryAdapter.firstItem
        countryAutocomplete.setText(initialCountry.name)
        selectedCountry = initialCountry
        countryChangeCallback(initialCountry)
    }

    /**
     * @param allowedCountryCodes A set of allowed country codes. Will be ignored if empty.
     */
    internal fun setAllowedCountryCodes(allowedCountryCodes: Set<String>) {
        val isUpdated = countryAdapter.updateUnfilteredCountries(allowedCountryCodes)
        if (isUpdated) {
            updateInitialCountry()
        }
    }

    /**
     * @param countryCode specify a country code to display in the input. The input will display
     * the full country display name.
     */
    internal fun setCountrySelected(countryCode: String) {
        updateUiForCountryEntered(getDisplayCountry(countryCode))
    }

    @VisibleForTesting
    internal fun updateUiForCountryEntered(displayCountryEntered: String) {
        val country = CountryUtils.getCountryByName(displayCountryEntered)

        // If the user-typed country matches a valid country, update the selected country
        // Otherwise, revert back to last valid country if country is not recognized.
        val displayCountry = country?.let {
            updatedSelectedCountryCode(it)
            displayCountryEntered
        } ?: selectedCountry?.name

        countryAutocomplete.setText(displayCountry)
    }

    private fun updatedSelectedCountryCode(country: Country) {
        clearError()
        if (selectedCountry != country) {
            selectedCountry = country
            countryChangeCallback(country)
        }
    }

    private fun getDisplayCountry(countryCode: String): String {
        return CountryUtils.getCountryByCode(countryCode)?.name
            ?: Locale("", countryCode).displayCountry
    }

    internal fun validateCountry() {
        countryAutocomplete.performValidation()
    }

    private fun clearError() {
        tl_country_cat.error = null
        tl_country_cat.isErrorEnabled = false
    }
}
