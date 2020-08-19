package com.wayproject.bwamov.sign.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.wayproject.bwamov.home.HomeActivity
import com.wayproject.bwamov.R
import com.wayproject.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {     //izin listener

    //1. Inisialisasi variabel yang akan digunakan menggunakan val1

    val REQUEST_IMAGE_CAPTURE = 1 //variabel untuk pencarian foto
    var statusAdd:Boolean = false //variabel untuk status pencarian foto, bila foto sudah ditambah, nilainya akan jadi true
    lateinit var filePath: Uri

    lateinit var storage : FirebaseStorage              //variabel untuk akses storage firebase
    lateinit var storageReferensi : StorageReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

     //2. Inisialisasi variabel yang belum mempunyai value

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReferensi = storage.getReference()

     //3. beri value nama

        tv_hello.text = "Selamat Datang\n"+intent.getStringExtra("nama")

        //cara lain bisa menggunakan share preference, karena nama sudah di input di page sebelumnya

        //untuk membuka browser ambil foto
        iv_add.setOnClickListener {
            if (statusAdd){
                statusAdd = false                                   // merubah ke status awal
                btn_save.visibility = View.VISIBLE                  // menampilkan button save foto
                iv_add.setImageResource(R.drawable.ic_btn_upload)   // merubah menjadi button hapus ketika ada foto
                iv_profile.setImageResource(R.drawable.user_pic)    // default foto
            } else{
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(this)                     //error karena listener belum extend/izin
                    .check()
            }
        }

     //4. beri fungsi button

        btn_home.setOnClickListener {
            finishAffinity()                                        //semua activity yang telah muncul, dihapus

            var goHome = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
            startActivity(goHome)
            // menuju Home Page
        }

        btn_save.setOnClickListener {
            if (filePath != null){                                  //jika file path tidak null masuk ke progress upload

                var progressDialog = ProgressDialog(this)   //progres DIALOG
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                var ref = storageReferensi.child("images/"+ UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()                                                    //progres dialog mati
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()     //info berhasil upload

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())
                        }                                                                           //save

                        finishAffinity()                                                            //hapus aktivitas progres

                        var goHome = Intent(this@SignUpPhotoScreenActivity, HomeActivity::class.java)
                        startActivity(goHome)
                        // menuju Home Page
                    }

                    .addOnFailureListener {
                        progressDialog.dismiss()                                                    //progres dialog mati
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()       //info gagal upload
                    }

                    .addOnProgressListener {
                        taskSnapshot ->  var progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload "+progress.toInt()+" %")
                    }                                                                               //info persentase progress

            } else{

            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)                    //jika disetujui
            }                                                                                       //harus ada onActivityResult**
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan photo Profile", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Klik tombol upload nanti saja", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {               //**onActivityResult dari startactivity
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){              //upload foto
           var bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filePath = data.getData()!!
            Glide.with(this)
                .load(bitmap)                                                                       //format bitmap
                .apply(RequestOptions.circleCropTransform())                                        //dijadikan bentuk lingkaran
                .into(iv_profile)

            btn_save.visibility = View.VISIBLE                                                      //tampil button save
            iv_add.setImageResource(R.drawable.ic_btn_delete)                                       //button add -> button delete
        }
    }
}
