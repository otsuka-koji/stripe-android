package com.stripe.android.model

import com.stripe.android.CardNumberFixtures.VALID_VISA_NO_SPACES
import com.stripe.android.model.ConfirmPaymentIntentParams.Companion.PARAM_PAYMENT_METHOD_OPTIONS
import com.stripe.android.model.ConfirmPaymentIntentParams.Companion.PARAM_SAVE_PAYMENT_METHOD
import com.stripe.android.model.ConfirmPaymentIntentParams.Companion.PARAM_SOURCE_ID
import com.stripe.android.model.ConfirmStripeIntentParams.Companion.PARAM_CLIENT_SECRET
import com.stripe.android.model.ConfirmStripeIntentParams.Companion.PARAM_PAYMENT_METHOD_ID
import com.stripe.android.model.ConfirmStripeIntentParams.Companion.PARAM_RETURN_URL
import com.stripe.android.model.ConfirmStripeIntentParams.Companion.PARAM_USE_STRIPE_SDK
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConfirmPaymentIntentParamsTest {

    @Test
    fun createConfirmPaymentIntentWithSourceDataParams_withAllFields_hasExpectedFields() {
        val sourceParams = SourceParams.createCardParams(FULL_FIELDS_VISA_CARD)
        val params = ConfirmPaymentIntentParams
            .createWithSourceParams(sourceParams, CLIENT_SECRET, RETURN_URL)

        assertEquals(CLIENT_SECRET, params.clientSecret)
        assertEquals(RETURN_URL, params.returnUrl)
        assertEquals(sourceParams, params.sourceParams)
        assertFalse(params.shouldSavePaymentMethod())
    }

    @Test
    fun createConfirmPaymentIntentWithSourceIdParams_withAllFields_hasExpectedFields() {
        val params = ConfirmPaymentIntentParams
            .createWithSourceId(SOURCE_ID, CLIENT_SECRET, RETURN_URL)

        assertEquals(CLIENT_SECRET, params.clientSecret)
        assertEquals(RETURN_URL, params.returnUrl)
        assertEquals(SOURCE_ID, params.sourceId)
        assertFalse(params.shouldSavePaymentMethod())
    }

    @Test
    fun createConfirmPaymentIntentWithSourceIdParams_withSavePaymentMethod_hasExpectedFields() {
        val params = ConfirmPaymentIntentParams
            .createWithSourceId(SOURCE_ID, CLIENT_SECRET, RETURN_URL, true)

        assertEquals(CLIENT_SECRET, params.clientSecret)
        assertEquals(RETURN_URL, params.returnUrl)
        assertEquals(SOURCE_ID, params.sourceId)
        assertTrue(params.shouldSavePaymentMethod())

        assertTrue(params.toParamMap()[PARAM_SAVE_PAYMENT_METHOD] == true)
    }

    @Test
    fun createWithPaymentMethodCreateParams_hasExpectedFields() {
        val paymentMethodCreateParams = PaymentMethodCreateParamsFixtures.DEFAULT_CARD
        val params = ConfirmPaymentIntentParams
            .createWithPaymentMethodCreateParams(paymentMethodCreateParams, CLIENT_SECRET, RETURN_URL)

        assertEquals(CLIENT_SECRET, params.clientSecret)
        assertEquals(RETURN_URL, params.returnUrl)
        assertEquals(paymentMethodCreateParams, params.paymentMethodCreateParams)
        assertFalse(params.shouldSavePaymentMethod())
    }

    @Test
    fun createConfirmPaymentIntentWithPaymentMethodId_hasExpectedFields() {
        val params = ConfirmPaymentIntentParams
            .createWithPaymentMethodId(PM_ID, CLIENT_SECRET, RETURN_URL)

        assertEquals(CLIENT_SECRET, params.clientSecret)
        assertEquals(RETURN_URL, params.returnUrl)
        assertEquals(PM_ID, params.paymentMethodId)
        assertFalse(params.shouldSavePaymentMethod())
    }

    @Test
    fun createConfirmPaymentIntentWithPaymentMethodCreateParams_withSavePaymentMethod_hasExpectedFields() {
        val paymentMethodCreateParams = PaymentMethodCreateParamsFixtures.DEFAULT_CARD
        val params = ConfirmPaymentIntentParams
            .createWithPaymentMethodCreateParams(paymentMethodCreateParams,
                CLIENT_SECRET, RETURN_URL, true)

        assertEquals(CLIENT_SECRET, params.clientSecret)
        assertEquals(RETURN_URL, params.returnUrl)
        assertEquals(paymentMethodCreateParams, params.paymentMethodCreateParams)
        assertTrue(params.shouldSavePaymentMethod())
    }

    @Test
    fun createConfirmPaymentIntentWithPaymentMethodId_withSavePaymentMethod_hasExpectedFields() {
        val params = ConfirmPaymentIntentParams
            .createWithPaymentMethodId(PM_ID, CLIENT_SECRET, RETURN_URL, true)

        assertEquals(CLIENT_SECRET, params.clientSecret)
        assertEquals(RETURN_URL, params.returnUrl)
        assertEquals(PM_ID, params.paymentMethodId)
        assertTrue(params.shouldSavePaymentMethod())

        assertTrue(params.toParamMap()[PARAM_SAVE_PAYMENT_METHOD] == true)
    }

    @Test
    fun createWithSourceId_toParamMap_createsExpectedMap() {
        val confirmPaymentIntentParams = ConfirmPaymentIntentParams
            .createWithSourceId(SOURCE_ID, CLIENT_SECRET, RETURN_URL)

        val paramMap = confirmPaymentIntentParams.toParamMap()
        assertEquals(paramMap[PARAM_SOURCE_ID], SOURCE_ID)
        assertEquals(paramMap[PARAM_CLIENT_SECRET], CLIENT_SECRET)
        assertEquals(paramMap[PARAM_RETURN_URL], RETURN_URL)
        assertEquals(false, paramMap[PARAM_SAVE_PAYMENT_METHOD])
    }

    @Test
    fun createWithPaymentMethodId_withoutReturnUrl_toParamMap_createsExpectedMap() {
        val confirmPaymentIntentParams = ConfirmPaymentIntentParams
            .createWithPaymentMethodId(PM_ID, CLIENT_SECRET)

        val paramMap = confirmPaymentIntentParams.toParamMap()

        assertEquals(paramMap[PARAM_PAYMENT_METHOD_ID], PM_ID)
        assertEquals(paramMap[PARAM_CLIENT_SECRET], CLIENT_SECRET)
        assertFalse(paramMap.containsKey(PARAM_RETURN_URL))
        assertEquals(false, paramMap[PARAM_SAVE_PAYMENT_METHOD])
    }

    @Test
    fun createWithPaymentMethodId_withReturnUrl_toParamMap_createsExpectedMap() {
        val confirmPaymentIntentParams = ConfirmPaymentIntentParams
            .createWithPaymentMethodId(PM_ID, CLIENT_SECRET, RETURN_URL)

        val paramMap = confirmPaymentIntentParams.toParamMap()

        assertEquals(paramMap[PARAM_PAYMENT_METHOD_ID], PM_ID)
        assertEquals(paramMap[PARAM_CLIENT_SECRET], CLIENT_SECRET)
        assertEquals(paramMap[PARAM_RETURN_URL], RETURN_URL)
        assertEquals(false, paramMap[PARAM_SAVE_PAYMENT_METHOD])
    }

    @Test
    fun toParamMap_whenExtraParamsProvided_createsExpectedMap() {
        val extraParamKey1 = "extra_param_key_1"
        val extraParamKey2 = "extra_param_key_2"
        val extraParamValue1 = "extra_param_value_1"
        val extraParamValue2 = "extra_param_value_2"
        val extraParams = mapOf(
            extraParamKey1 to extraParamValue1,
            extraParamKey2 to extraParamValue2
        )

        val confirmPaymentIntentParams = ConfirmPaymentIntentParams
            .createWithPaymentMethodId("pm_123", CLIENT_SECRET,
                RETURN_URL, false, extraParams)

        val paramMap = confirmPaymentIntentParams.toParamMap()

        assertEquals(paramMap[PARAM_CLIENT_SECRET], CLIENT_SECRET)
        assertEquals(paramMap[extraParamKey1], extraParamValue1)
        assertEquals(paramMap[extraParamKey2], extraParamValue2)
        assertEquals(false, paramMap[PARAM_SAVE_PAYMENT_METHOD])
    }

    @Test
    fun create_withClientSecret() {
        assertEquals("client_secret",
            ConfirmPaymentIntentParams.create("client_secret", "")
                .clientSecret)
    }

    @Test
    fun shouldUseStripeSdk() {
        val confirmPaymentIntentParams =
            ConfirmPaymentIntentParams.create("client_secret", "return_url")
        assertFalse(confirmPaymentIntentParams.shouldUseStripeSdk())

        assertTrue(confirmPaymentIntentParams
            .withShouldUseStripeSdk(true)
            .shouldUseStripeSdk())
    }

    @Test
    fun toBuilder_withPaymentMethodCreateParams_shouldCreateEqualObject() {
        val extraParams = mapOf("key" to "value")
        val params = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
            PaymentMethodCreateParamsFixtures.DEFAULT_CARD, CLIENT_SECRET, RETURN_URL, true, extraParams
        )

        assertEquals(params, params.toBuilder().build())
    }

    @Test
    fun create_withMandatePaymentMethodType_addsMandateDataToParams() {
        val params = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
            PaymentMethodCreateParamsFixtures.DEFAULT_SEPA_DEBIT,
            CLIENT_SECRET,
            RETURN_URL
        ).toParamMap()
        assertTrue(params.containsKey(MandateData.PARAM_MANDATE_DATA))
    }

    @Test
    fun create_withPaymentMethodOptions() {
        val params = ConfirmPaymentIntentParams(
            paymentMethodId = "pm_123",
            paymentMethodOptions = PaymentMethodOptionsParams.Card(
                cvc = "123"
            ),
            clientSecret = "client_secret"
        ).toParamMap()

        assertEquals(
            mapOf(
                PARAM_PAYMENT_METHOD_ID to "pm_123",
                PARAM_PAYMENT_METHOD_OPTIONS to mapOf("card" to mapOf("cvc" to "123")),
                PARAM_CLIENT_SECRET to "client_secret",
                PARAM_SAVE_PAYMENT_METHOD to false,
                PARAM_USE_STRIPE_SDK to false
            ),
            params
        )
    }

    private companion object {
        private val FULL_FIELDS_VISA_CARD =
            Card.Builder(VALID_VISA_NO_SPACES, 12, 2050, "123")
                .name("Captain Cardholder")
                .addressLine1("1 ABC Street")
                .addressLine2("Apt. 123")
                .addressCity("San Francisco")
                .addressState("CA")
                .addressZip("94107")
                .addressState("US")
                .currency("usd")
                .build()

        private const val CLIENT_SECRET = "pi_1CkiBMLENEVhOs7YMtUehLau_secret_s4O8SDh7s6spSmHDw1VaYPGZA"

        private const val RETURN_URL = "stripe://return_url"
        private const val SOURCE_ID = "src_123testsourceid"
        private const val PM_ID = "pm_123456789"
    }
}
