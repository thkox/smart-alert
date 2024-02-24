package eu.tkacas.smartalert.app

import android.content.Context
import android.content.SharedPreferences
import eu.tkacas.smartalert.models.CriticalWeatherPhenomenon

class SharedPrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun setIsEmployee(isEmployee: Boolean) {
        sharedPreferences.edit().putBoolean("is_employee", isEmployee).apply()
    }

    fun isEmployee(): Boolean {
        return sharedPreferences.getBoolean("is_employee", false)
    }

    fun removeIsEmployee() {
        sharedPreferences.edit().remove("is_employee").apply()
    }

    fun setCriticalWeatherPhenomenon(name: String) {
        sharedPreferences.edit().putString("critical_weather_phenomenon", name).apply()
    }

    fun getCriticalWeatherPhenomenon(): CriticalWeatherPhenomenon {
        val name = sharedPreferences.getString("critical_weather_phenomenon", "WILDFIRE")
        return CriticalWeatherPhenomenon.valueOf(name!!)
    }
}