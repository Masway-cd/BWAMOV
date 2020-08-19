package com.wayproject.bwamov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.wayproject.bwamov.checkout.PilihBangkuActivity
import com.wayproject.bwamov.home.dashboard.PlaysAdapter
import com.wayproject.bwamov.model.Film
import com.wayproject.bwamov.model.Plays
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    //2. inisialisasi

    private lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<Plays>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //1. ambil data yang tadi sudah dikirim dari Dashboard Fragment

        val data = intent.getParcelableExtra<Film>("data")

        //inisialisasi
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")                       //load data firebase
            .child(data.judul.toString())
            .child("play")                                                                //load child (subfolder)

        tv_kursi.text = data.judul
        tv_genre.text = data.genre
        tv_desc.text = data.desc
        tv_rate.text = data.rating

        Glide.with(this)                                                                    //load gambar
            .load(data.poster)
            .into(iv_poster)

        //inisialisasi
        rv_who_play.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getData()

        btn_pilih_bangku.setOnClickListener {
            var intent = Intent(this@DetailActivity, PilihBangkuActivity::class.java)
                    .putExtra("data", data)
            startActivity(intent)
        }
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@DetailActivity, ""+p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                dataList.clear()                                                                    //hapus data agar tidak bentrok

                for (getDataSnapshot in p0.children){
                    var Film = getDataSnapshot.getValue(Plays::class.java)
                    dataList.add(Film!!)
                }

                rv_who_play.adapter = PlaysAdapter(dataList){

                }
            }

        })

    }
}