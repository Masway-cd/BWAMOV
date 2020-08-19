package com.wayproject.bwamov.home.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.wayproject.bwamov.DetailActivity
import com.wayproject.bwamov.R
import com.wayproject.bwamov.model.Film
import com.wayproject.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    //2. buat parameter

    private lateinit var preferences: Preferences
    private lateinit var mDatabase : DatabaseReference

    private var dataList = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    //1. buat function onActivityCreated

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")

        tv_nama.setText(preferences.getValues("nama"))
        if (!preferences.getValues("saldo").equals("")){                                //ditambahin tanda ! sebelum preference
            currency(preferences.getValues("saldo")!!.toDouble(), tv_saldo)
        }
//
//        tv_nama.setText(preferences.getValues("nama"))
//        if(preferences.getValues("saldo").equals("")){
//            currency(preferences.getValues("saldo")!!.toDouble(), tv_saldo)                      //getValue ambigu ??trouble disini tadi
//        }

        Glide.with(this)                                                                    //tampil foto
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)

        rv_now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_coming_soon.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        getData()
    }

    private fun getData() {                                                                         // panggil data
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError : DatabaseError) {
                Toast.makeText(context, ""+databaseError.message, Toast.LENGTH_LONG).show()     //pesan ketika DB error
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()                                                                    //agar tidak ada data duplikat
                for(getdataSnapshot in dataSnapshot.children){
                    var film = getdataSnapshot.getValue(Film::class.java)                           //ambil data
                    dataList.add(film!!)
                }

                rv_now_playing.adapter = NowPlayingAdapter(dataList){
                    var intent = Intent(context, DetailActivity::class.java).putExtra("data", it)   //intent ke detail page
                    startActivity(intent)
                }

                rv_coming_soon.adapter = ComingSoonAdapter(dataList){
                    var intent = Intent(context, DetailActivity::class.java).putExtra("data", it)   //intent ke detail page
                    startActivity(intent)
                }
            }

        })
    }

    private fun currency(harga : Double, textview : TextView){                                      //convert mata uang
        val localID = Locale("in","ID")
        val format = NumberFormat.getCurrencyInstance(localID)
        textview.setText(format.format(harga))
    }

}