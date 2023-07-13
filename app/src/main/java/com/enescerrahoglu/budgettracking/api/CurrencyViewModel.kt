package com.enescerrahoglu.budgettracking.api
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class CurrencyViewModel : ViewModel() {
    val currencyList = MutableLiveData<List<Currency>>()

    fun fetchCurrencyData() {
        val url = "https://api.currencyapi.com/v3/latest?apikey=h60lwkZYtxpkY3yK2CY3ydEBKHG3B0htQ2tPiDyp"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonData = response.body()?.string()
                val currencyData = parseJson(jsonData)
                currencyList.postValue(currencyData)
            }
        })
    }

    private fun parseJson(jsonData: String?): List<Currency> {
        val currencyList = mutableListOf<Currency>()

        jsonData?.let {
            try {
                val jsonObject = JSONObject(it)
                val dataObject = jsonObject.getJSONObject("data")
                val keys = dataObject.keys()

                while (keys.hasNext()) {
                    val key = keys.next()
                    val currencyObject = dataObject.getJSONObject(key)
                    val code = currencyObject.getString("code")
                    val value = currencyObject.getDouble("value")
                    val currency = Currency(code, value)
                    currencyList.add(currency)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return currencyList
    }

    fun getTryValue(): Double? {
        return currencyList.value?.firstOrNull { it.code == "TRY" }?.value
    }
}