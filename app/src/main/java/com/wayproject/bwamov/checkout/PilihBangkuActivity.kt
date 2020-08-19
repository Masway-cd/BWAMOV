package com.wayproject.bwamov.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.wayproject.bwamov.R
import com.wayproject.bwamov.model.Checkout
import com.wayproject.bwamov.model.Film
import kotlinx.android.synthetic.main.activity_pilih_bangku.*

class PilihBangkuActivity : AppCompatActivity() {

    //2. buat status kursi

    var statusA1:Boolean = false
    var statusA2:Boolean = false
    var statusA3:Boolean = false
    var statusA4:Boolean = false

    var statusB1:Boolean = false
    var statusB2:Boolean = false
    var statusB3:Boolean = false
    var statusB4:Boolean = false

    var statusC1:Boolean = false
    var statusC2:Boolean = false
    var statusC3:Boolean = false
    var statusC4:Boolean = false

    var statusD1:Boolean = false
    var statusD2:Boolean = false
    var statusD3:Boolean = false
    var statusD4:Boolean = false
    var total:Int = 0

    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_bangku)

        //1. ambil data

        val data = intent.getParcelableExtra<Film>("data")
        tv_kursi.text = data.judul

        a3.setOnClickListener { 
            if (statusA3){
                a3.setImageResource(R.drawable.ic_rectangle_empty)                                  //kursi awal kosong
                statusA3 = false
                total -= 1
                beliTiket(total)
            } else {
                a3.setImageResource(R.drawable.ic_rectangle_selected)                               //kursi dipesan
                statusA3 = true
                total += 1
                beliTiket(total)

                val data = Checkout ("A3", "70000")
                dataList.add(data)
            }
        }

        a4.setOnClickListener {
            if (statusA4){
                a4.setImageResource(R.drawable.ic_rectangle_empty)                                  //kursi awal kosong
                statusA4 = false
                total -= 1
                beliTiket(total)
            } else {
                a4.setImageResource(R.drawable.ic_rectangle_selected)                               //kursi dipesan
                statusA4 = true
                total += 1
                beliTiket(total)

                val data = Checkout ("A4", "70000")
                dataList.add(data)
            }
        }

        btn_home.setOnClickListener {
            var intent = Intent(this, CheckoutActivity::class.java)
                .putExtra("data", dataList)                                                     //putExtra = membawa data dengan acuan paramater dan value
            startActivity(intent)
        }
    }

    private fun beliTiket(total: Int) {                                                             //untuk melakukan cek button, disable atau enable
        if (total==0){
            btn_home.setText("Beli Tiket")
            btn_home.visibility = View.INVISIBLE                                                    //jika tidak ada aksi, btn empty
        } else {
            btn_home.setText("Beli Tiket ("+total+")")
            btn_home.visibility = View.VISIBLE                                                      //jika ada aksi, ada total jumlah tiket
        }

    }
}