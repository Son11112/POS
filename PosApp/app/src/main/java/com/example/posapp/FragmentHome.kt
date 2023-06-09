package com.example.posapp

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapp.data.ShiftsHomeAdapter
import com.example.posapp.viewModel.ShiftsViewModel
import com.example.posapp.viewModel.ShiftsViewModelFactory
import com.example.posapp.data.MyRoomDatabase
import com.example.posapp.databinding.FragmentHomeBinding
import com.example.posapp.viewModel.OrderViewModel
import com.example.posapp.viewModel.OrderViewModelFactory
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class FragmentHome : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var shiftsViewModel: ShiftsViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val CITY: String = "TOKYO"
    val API: String = "95cc384a7b07fecdae34df05a1c43365"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dayView: TextView = binding.tvDay
        val weekView: TextView = binding.tvWeek
        val monthView: TextView = binding.tvMonth
        val calendars = Calendar.getInstance()
        val currentDateShift = String.format("%d-%d-%d", calendars.get(Calendar.YEAR), calendars.get(Calendar.MONTH) + 1, calendars.get(Calendar.DAY_OF_MONTH))
        val currentDateSold = Calendar.getInstance()
        val endPeriod = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(currentDateSold.time)
        currentDateSold.add(Calendar.DATE, -7) // Trừ đi 7 ngày
        val startPeriod = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(currentDateSold.time)

        // Khởi tạo ViewModel
        val factory =
            ShiftsViewModelFactory(MyRoomDatabase.getDatabase(requireContext()).shiftsDao())
        val orderFactory = OrderViewModelFactory(
            MyRoomDatabase.getDatabase(requireContext()).orderDao(),
            MyRoomDatabase.getDatabase(requireContext()).orderFoodItemDao(),
            MyRoomDatabase.getDatabase(requireContext()).menuDao()
        )
        shiftsViewModel = ViewModelProvider(this, factory).get(ShiftsViewModel::class.java)
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        // Khởi tạo adapter
        val recyclerView = binding.shiftsRecycleView
        val adapter = ShiftsHomeAdapter(mutableListOf())
        recyclerView.adapter = adapter

        shiftsViewModel.getShiftByDate(currentDateShift).observe(viewLifecycleOwner) { shifts ->
            if (shifts.isNotEmpty()) {
                (recyclerView.adapter as ShiftsHomeAdapter).updateData(shifts)
            }
        }

        val textViewSoldOne: TextView = binding.tvSoldOne
        val textViewSoldTwo: TextView = binding.tvSoldTwo
        val textViewSoldThree: TextView = binding.tvSoldThree

        lifecycleScope.launch {
            val topThreeSoldItems = orderViewModel.getTopThreeSoldItems()
            if (topThreeSoldItems.isNotEmpty()) {
                textViewSoldOne.text = "${topThreeSoldItems[0].name} - ${topThreeSoldItems[0].totalSold}"
            }
            if (topThreeSoldItems.size >= 2) {
                textViewSoldTwo.text = "${topThreeSoldItems[1].name} - ${topThreeSoldItems[1].totalSold}"
            }
            if (topThreeSoldItems.size >= 3) {
                textViewSoldThree.text = "${topThreeSoldItems[2].name} - ${topThreeSoldItems[2].totalSold}"
            }
        }

        orderViewModel.totalRevenueToday.observe(viewLifecycleOwner) { revenue ->
            dayView.text = "$revenue"+"円"
        }

        orderViewModel.totalRevenueThisWeek.observe(viewLifecycleOwner) { revenue ->
            weekView.text = "$revenue"+"円"
        }

        orderViewModel.totalRevenueThisMonth.observe(viewLifecycleOwner){ revenue ->
            monthView.text = "$revenue"+"円"
        }

        binding.btnLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentLogin)
        }
        binding.btnEmployeeManagement.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentUsers)
        }
        binding.btnMenuManagement.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentMenu)
        }
        binding.btnNotificationManagement.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentNotifications)
        }
        binding.btnShiftConfirmation.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentAddShift)
        }

        val textViewDate = view.findViewById<TextView>(R.id.tvDate)
        val calendar = Calendar.getInstance()
        val currentDate = "${calendar.get(Calendar.YEAR)}年${calendar.get(Calendar.MONTH) + 1}月${calendar.get(Calendar.DATE)}日"
        textViewDate.text = currentDate
        weatherTask().execute()
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

            view?.findViewById<RelativeLayout>(R.id.mainContainer)?.visibility = View.VISIBLE
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
                view?.findViewById<TextView>(R.id.updatedAt)?.text = updatedAtText
                view?.findViewById<TextView>(R.id.status)?.text =
                    weatherDescription.capitalize(Locale.JAPANESE)
                view?.findViewById<TextView>(R.id.temp)?.text = temp
                view?.findViewById<TextView>(R.id.tempMin)?.text = tempMin
                view?.findViewById<TextView>(R.id.tempMax)?.text = tempMax
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