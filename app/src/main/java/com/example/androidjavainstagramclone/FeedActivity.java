package com.example.androidjavainstagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        auth=FirebaseAuth.getInstance();

    }

    //simdi gittik bir menu item layout u olusturduk ve olusturdugumuz o layotu asinda biz burda kullanacgimiz icin, buraya baglamamiz gerekiyr
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //burada baglama islemini yani, menu.xml i Feed Activity e bagliyoruz.
        //simdi menu yu inflater ile buraya cagiriyoruz.
        //menu infkater, bizim xml de yazdiklarimiz ile burada yani activity e yazdigimiz kodu birbrine baglayan bir methiod idi
        MenuInflater menuInflater=getMenuInflater();
        //simdi menuyu buraya baglayalim yani olusturdugumuz option menu xml imizi buraya baglayalim yani inflate edelim.
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //burada ise menulerde bulunan seceneklere tiklanmasi durumunda ne olacagini seciyoruz.

        if (item.getItemId() ==R.id.add_post){
            //Upload Activity e gidecegz
            Intent intentToUpload = new Intent(FeedActivity.this,UploadActivity.class);
            startActivity(intentToUpload);
            //finish(); //finish demedim cunku belki kullanici upload etmekten vazgecebilir ve post larin oldugu feed activity e donmek ister.

        } else if (item.getItemId() ==R.id.signout){
            //cikis yapacagiz
            auth.signOut();
            Intent intentToMain = new Intent(FeedActivity.this,MainActivity.class);
            startActivity(intentToMain);
            finish(); //cikis yaptigina gore artik buraya donemez.
        }
        return super.onOptionsItemSelected(item);
    }
}