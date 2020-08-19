package com.wayproject.bwamov.home.tiket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.wayproject.bwamov.R
import com.wayproject.bwamov.model.Checkout
import com.wayproject.bwamov.model.Film
import kotlinx.android.synthetic.main.activity_tiket.*

class TiketActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket)

        //1.ambil data dari intent
        var data = intent.getParcelableExtra<Film>("data")

        tv_title.text = data.judul
        tv_genre.text = data.genre
        tv_rate.text = data.rating

        Glide.with(this)            //ambil gambar
            .load(data.poster)              //load gambar
            .into((iv_poster_image))        //gambar di letakkan di iv_poster_image

        rv_checkout.layoutManager = LinearLayoutManager(this)
        dataList.add(Checkout("C1",""))
        dataList.add(Checkout("C2",""))

        rv_checkout.adapter = TiketAdapter(dataList){       //kirim ke tiket adapter

        }
    }
}