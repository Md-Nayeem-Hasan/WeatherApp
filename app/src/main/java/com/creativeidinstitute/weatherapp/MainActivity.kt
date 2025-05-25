package com.creativeidinstitute.weatherapp

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.creativeidinstitute.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

//382168b43ff772b1a387ffd4cf838a69
class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy {
    ActivityMainBinding.inflate(layoutInflater)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Dhaka")
        SearchCity()

    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true

            }

        } )
    }

    private fun fetchWeatherData(cityName:String) {
      val retrofit =Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create())
          .baseUrl("https://api.openweathermap.org/data/2.5/")
          .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityName, "382168b43ff772b1a387ffd4cf838a69","metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody!=null){
                    val temperature = responseBody.main.temp.toString()
                    val humidity= responseBody.main.humidity
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    binding.temp.text="$temperature ˚C"
                    binding.maxTemp.text= "Max Temp : $maxTemp ˚C"
                    binding.minTemp.text= "Min Temp : $minTemp ˚C"
                    binding.textHumidity.text="$humidity %"
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.cityName.text= "$cityName"
                //Log.d("TAG", "onResponse: $temperature")

                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        }
    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format((Date()))

    }

    fun dayName(timestamp:Long):String{
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            return sdf.format((Date()))
    }
}