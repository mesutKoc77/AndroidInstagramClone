package com.example.androidjavainstagramclone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.androidjavainstagramclone.databinding.ActivityUploadBinding;
import com.google.android.material.snackbar.Snackbar;

public class UploadActivity extends AppCompatActivity {

    ActivityResultLauncher <Intent> activityResultLauncher; //bu Launcher'lari eger onCreate icerisinde baslatmaz isem bu durum uygulamayi cokertir.
    ActivityResultLauncher<String> permissionLauncher;
    Uri imageData;
    private ActivityUploadBinding binding;
    Bitmap selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
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
            //kullanacagiz. ve burada neler yaocagimizi anlatacgiz kendirisine.

        }

    }


    public void registerLauncer() {
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //simdi kullanicinin gallery sine gittik, kullanici ne yapti ? Sonuc ne diyoruz? Birseyleri secti mi vaz mi gecti, sd kart mi cikti bunu da kontrol edecgz.
                if (result.getResultCode()==RESULT_OK)//hersey ok ise, yani kullanicinin gallerysine gitti isek
                {
                    Intent intentFromResult = result.getData(); //veriyi alirken bana donus bir intent olarak donecek.
                    //simdi bana birseyler dondu ama bu veri bos mu degil mi onu kontrrol ediyoruz.
                    if (intentFromResult!=null){
                        //simdi ben bir datayi aldim ama ben bu bunu bir uri ya, yani bir ilgili dosyanin  bulundugu yerin bilgisini Uri degiskenine kayit etmem gerekiyor
                        imageData = intentFromResult.getData();
                        //simdi kullanici ya image View'da almak istedigi goruntuyu gosterecegz
                        binding.imageView.setImageURI(imageData); //simdi imageView'e bunu attik.

                        //burada bize yani bu projede bize sadece uri yeterli oldugu icin Bitmap'i kullanmayacagz.
                        /*
                          //simdi ise, image de detayli manipulation islemi yapabilmek icin,  bu uri yi Bitmap'e cevirecegiz.
                        //sayfanin en altinda Uri ile Bitmap arasindaki fark anlatiliyor. Uri, bir dosyanin yolunu gosterirken Bitmap ise bunun piksel deger karsiligini verir ve
                        //image de manioulasyona izin verir.

                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                ImageDecoder.Source source=ImageDecoder.createSource(UploadActivity.this.getContentResolver(),imageData);
                                //aldigimiz bu source u yani kodu, Bitmap'e ceviriyoz
                                selectedImage =ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);
                            } else {
                                selectedImage =MediaStore.Images.Media.getBitmap(UploadActivity.this.getContentResolver(),imageData);
                                binding.imageView.setImageBitmap(selectedImage);
                            }



                        } catch (Exception e){
                            e.printStackTrace();
                        }


                         */
                    }
                }
            }
        }); //bana bir sonuc olacagi yani bir eylem yapacagim icin bana bir Activity baslat dedim; ama sonrada bunun sonucunda yani sonuc da ne olacagini da Callback ile anlatmam gerekiyor.

    }





}
/*
                        Eğer sadece URI'yi ImageView'de gösterirseniz ve Bitmap'e dönüştürmezseniz, uygulamanın işlemesi açısından bir fark olmayacaktır. Çünkü ImageView, URI'yi doğrudan gösterebilir. Ancak, bazı durumlarda Bitmap'e dönüştürmek daha fazla esneklik sağlayabilir:

Bellek Kullanımı: URI, dosyanın tamamını belleğe yüklemek zorunda değildir, bu nedenle bellek kullanımı daha az olabilir. Ancak, dosya büyükse veya çok sayıda dosya seçiliyorsa, URI'yi yüklemek bellek kullanımını artırabilir.

Görsel İşleme: Bitmap olarak yüklenen görüntüler, özelleştirilmiş görsel işleme işlemlerine tabi tutulabilir. Örneğin, resmi yeniden boyutlandırma, döndürme veya filtre uygulama gibi işlemler, Bitmap formatında daha kolay yapılabilir.

Uygulama Uyumluluğu: Bazı Android API'leri veya kütüphaneleri, Bitmap formatını bekleyebilir veya daha iyi performans için bu formatta işlem yapabilir. Bu durumda, URI'yi Bitmap'e dönüştürmek isteyebilirsiniz.

Genel olarak, küçük veya sadece birkaç resim seçimi için URI kullanmak uygun olabilir. Ancak, daha karmaşık veya büyük dosyalarla çalışıyorsanız, Bitmap'e dönüştürme daha iyi bir seçenek olabilir.
URI (Uniform Resource Identifier) ve Bitmap, her ikisi de Android uygulamalarında dosyalara erişmek ve işlemek için kullanılan kavramlardır, ancak farklı işlevlere sahiptirler.

URI (Uniform Resource Identifier):

URI, bir kaynağa (dosya, web adresi, veritabanı girdisi vb.) benzersiz bir şekilde referans veren bir tanımlayıcıdır.
URI, genellikle dosya yolunu, web URL'sini veya veritabanı kaydının ID'sini içerebilir.
URI, kaynağın konumunu ve erişim yöntemini belirtir, ancak kaynağın içeriğini temsil etmez.
URI'ler, dosya seçim işlemleri gibi işlemlerde kullanılabilir ve genellikle kullanıcı tarafından seçilen dosyanın konumunu belirtmek için kullanılır.
Bitmap:

Bitmap, bir görüntünün (resmin) piksel tabanlı bir temsilidir.
Bitmap, bir görüntünün her pikselinin rengini (RGB veya ARGB formatında) ve boyutunu içerir.
Bitmap, görüntü işleme, görsel gösterim ve manipülasyon için kullanılır.
Android'te, bir Bitmap nesnesi oluşturulduğunda, bir görüntü dosyasının belleğe yüklenmiş bir kopyası oluşturulur ve bu nesne üzerinde çeşitli işlemler yapılabilir (örneğin, yeniden boyutlandırma, döndürme, efektler uygulama vb.).
Temel fark, URI'nin bir kaynağa referans veren bir tanımlayıcı olduğu, Bitmap'in ise bir görüntünün piksel tabanlı temsilini içeren bir nesne olduğudur. URI, kaynağın konumunu belirtirken, Bitmap, bir görüntünün içeriğini temsil eder ve bu içeriği işlemek için kullanılır.

Bitmap ve URI, farklı senaryolarda kullanılır ve hangisinin kullanılacağı, uygulamanın gereksinimlerine ve kullanım senaryolarına bağlıdır. İşte her birinin ne zaman kullanılabileceğine dair bazı genel kılavuzlar:

1. **URI Kullanımı:**
   - Kullanıcı tarafından seçilen dosyanın konumunu belirtmek için genellikle URI kullanılır. Örneğin, bir galeriden bir resim seçildiğinde, bu resmin URI'si alınabilir ve bu URI, resmin konumunu belirtmek için kullanılabilir.
   - Dosya seçim işlemleri, genellikle URI kullanılarak gerçekleştirilir. Kullanıcı bir dosya seçtiğinde, bu dosyanın URI'si alınır ve bu URI, dosyanın konumunu belirtmek için kullanılır.
   - Eğer dosya sadece görüntülenmek veya diğer uygulamalarla paylaşılmak amacıyla kullanılacaksa ve herhangi bir görüntü işlemi yapılması gerekmeyecekse, URI kullanmak yeterli olabilir.

2. **Bitmap Kullanımı:**
   - Görüntü işleme, manipülasyon veya görsel efektler uygulanacaksa, genellikle Bitmap kullanılır. Örneğin, bir resmin boyutu değiştirilmek isteniyorsa veya üzerinde çeşitli efektler uygulanacaksa, bu işlemler Bitmap nesnesi üzerinde yapılır.
   - Bir ImageView veya diğer görsel bileşenlerde gösterilecek bir görüntü kullanılacaksa, genellikle bu görüntüyü bir Bitmap nesnesi olarak yüklemek ve bu nesneyi kullanarak görseli göstermek daha yaygındır.
   - Bir resmi belleğe yüklemek ve bu resmi işlemek veya manipüle etmek gerekiyorsa, bu durumda Bitmap kullanmak daha uygun olabilir.

Genel olarak, URI, dosyanın konumunu belirtmek ve dosya seçim işlemlerinde kullanmak için kullanılırken, Bitmap, bir görüntünün işlenmesi, manipülasyonu veya görsel olarak gösterilmesi gerektiğinde kullanılır. Hangisinin kullanılacağı, uygulamanın gereksinimlerine ve kullanım senaryolarına bağlı olarak değişir.
                         */
