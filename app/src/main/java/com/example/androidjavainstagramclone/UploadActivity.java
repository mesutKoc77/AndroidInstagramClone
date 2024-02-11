package com.example.androidjavainstagramclone;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class UploadActivity extends AppCompatActivity {

    ActivityResultLauncher <Intent>activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
    }

    public void uploadButtonClicked (View view) {

    }

    public void selectImage (View view) {
        //manifest e izin istedigimizi belirrten kodu ekledik
        //once izin var mi onu kontrol edecgz.
        //eger izin yoksa, asaguida oyzellikle opermissinlerin android. (nokta) dan secmek onemli.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //eger izni gostermemenizin mantigini kullaniciya aktarmamiz gerekiyorsa ki bununAndroid sistemin kendisi karar veriyor.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, "Izin Gerekli, Permission needed!",Snackbar.LENGTH_INDEFINITE) //yani kullanici tamam yani anladim diyecegi ana kadar goster demis olduk
                        .setAction("Izin Ver, Give Permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //simdi olusturdugmuz "Izin Ver, Give Permission" butonuna tikladi ve ne olacagini bu methodun icerisine yaziyoruz.
                                // yani biz burada "ask question ile kullanicdan izni istiyoruz."

                            }
                        }) // yani burada kullanici ya bir buton gosterecegz ve izin ver gibi bir buton. Bu butonu tiklayacak ve tikladiktan sonra ise ne olacagini "bir Listener"
                        //ile koda dokecegiz.
                        .show();
            } else //yani kullaniciya iznin gosterilmesinin "mantigi" yoksa, yine ask question ile kullanicdan izni istiyoruz.. Yani yukaridaki onClick methodun da da burada da izni iyteyecgz ama burdakinin farki
            //kullaniciya ifade gostermeyesimiz.
            {


            }
        } else //yani kullanici oncesinden zaten izin vermisse diger bir deyisle "ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED" false donerse
        {
            // o zaman zaten kullanicinin direkt galerisine gidecez.
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //direkt gidemiyorduk bu intent ile ancak bur result launcer ile biz ne yapacgimizi aciklayacagiz oncelikle. bununicin parametre olaak Intent isteyen activityresultlauncer
            //kullanacagiz.



        }

    }





}