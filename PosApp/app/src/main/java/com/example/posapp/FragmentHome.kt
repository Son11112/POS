package com.example.posapp

import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.data.ShiftsAdapter
import com.example.posapp.viewModel.ShiftsViewModel
import com.example.posapp.viewModel.ShiftsViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class FragmentHome : Fragment() {

    private lateinit var shiftsAdapter: ShiftsAdapter
    private lateinit var shiftsViewModel: ShiftsViewModel
    val CITY: String = "TOKYO"
    val API: String = "95cc384a7b07fecdae34df05a1c43365"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val buttonLogOut = view.findViewById<Button>(R.id.btnLogOut)
        buttonLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentLogin)
        }
        val buttonEmployeeManagement = view.findViewById<Button>(R.id.btnEmployeeManagement)
        buttonEmployeeManagement.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentUsers)
        }
        val buttonMenuManagement = view.findViewById<Button>(R.id.btnMenuManagement)
        buttonMenuManagement.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentMenu)
        }
        val buttonNotificationManagement = view.findViewById<Button>(R.id.btnNotificationManagement)
        buttonNotificationManagement.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentNotifications)
        }
        val buttonShiftConfirmation = view.findViewById<Button>(R.id.btnShiftConfirmation)
        buttonShiftConfirmation.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentShifts)
        }
        val buttonSalesAnalysis = view.findViewById<Button>(R.id.btnSalesAnalysis)
        buttonSalesAnalysis.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentSalesAnalysis)
        }

        val textViewDate = view.findViewById<TextView>(R.id.tvDate)
        val calendar = Calendar.getInstance()
        val currentDate =
            "${calendar.get(Calendar.YEAR)}年${calendar.get(Calendar.MONTH) + 1}月${
                calendar.get(
                    Calendar.DATE
                )
            }日"
        textViewDate.text = currentDate
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherTask().execute()

        // Khởi tạo ViewModel
        val factory = ShiftsViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).shiftsDao()
        )
        shiftsViewModel = ViewModelProvider(this, factory).get(ShiftsViewModel::class.java)

        // Khởi tạo adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.Shiftsrecycleview)

        // Lấy dữ liệu từ ViewModel và cập nhật lên RecyclerView
        shiftsViewModel.getAllShifts().observe(viewLifecycleOwner) { shifts ->
            Log.d(ContentValues.TAG, "Shifts $shifts")
            val adapter = ShiftsAdapter(requireContext(), shifts)
            recyclerView.adapter = adapter
        }
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

            view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.VISIBLE
            view?.findViewById<RelativeLayout>(R.id.mainContainer)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.errorText)?.visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result!!)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "更新時: " + SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.JAPANESE).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "最低気温: " + main.getString("temp_min") + "°C"
                val tempMax = "最高気温: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                view?.findViewById<TextView>(R.id.address)?.text = address
                view?.findViewById<TextView>(R.id.updated_at)?.text = updatedAtText
                view?.findViewById<TextView>(R.id.status)?.text =
                    weatherDescription.capitalize(Locale.JAPANESE)
                view?.findViewById<TextView>(R.id.temp)?.text = temp
                view?.findViewById<TextView>(R.id.temp_min)?.text = tempMin
                view?.findViewById<TextView>(R.id.temp_max)?.text = tempMax
                view?.findViewById<TextView>(R.id.sunrise)?.text =
                    SimpleDateFormat("hh:mm a", Locale.JAPANESE).format(Date(sunrise * 1000))
                view?.findViewById<TextView>(R.id.sunset)?.text =
                    SimpleDateFormat("hh:mm a", Locale.JAPANESE).format(Date(sunset * 1000))
                view?.findViewById<TextView>(R.id.wind)?.text = windSpeed
                view?.findViewById<TextView>(R.id.pressure)?.text = pressure
                view?.findViewById<TextView>(R.id.humidity)?.text = humidity
                view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.GONE
                view?.findViewById<RelativeLayout>(R.id.mainContainer)?.visibility = View.VISIBLE

            } catch (e: Exception) {
                view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.errorText)?.visibility = View.VISIBLE
            }
        }
    }
}
