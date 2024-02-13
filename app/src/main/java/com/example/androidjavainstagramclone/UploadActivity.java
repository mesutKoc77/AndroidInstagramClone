package com.example.androidjavainstagramclone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.androidjavainstagramclone.databinding.ActivityUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {

    ActivityResultLauncher <Intent> activityResultLauncher; //bu Launcher'lari eger onCreate icerisinde baslatmaz isem bu durum uygulamayi cokertir.
    ActivityResultLauncher<String> permissionLauncher;
    /*
    Birincisi (activityResultLauncher) Intent başlatmak için kullanılırken, ikincisi (permissionLauncher) izin istemek için kullanılır.
     */
    Uri imageData;
    private ActivityUploadBinding binding;
    //Bitmap selectedImage; //isternirse, bitmap ile de yapilabilir bu.

    private FirebaseStorage fireBaseStorage; //FirebaseStorage nesnesini kullanarak uygulama, dosyaları güvenli bir şekilde depolayabilir ve bu dosyalara erişim sağlayabilir. Bu sayede uygulama, kullanıcıların dosyalarını saklamak ve paylaşmak için güvenilir bir çözüm sunar.daha ayrintili bilgi en altta 5. bolumde
    private FirebaseAuth auth; //Bu nesne, kullanıcıların uygulamaya giriş yapması, kaydolması, şifrelerini sıfırlaması gibi kimlik doğrulama işlemlerini yönetir.daha ayrintili bilgi en altta 4. bolumde
    private FirebaseFirestore firebaseFirestore; //Firebase Firestore nesnesini oluşturmanın temel amacı, Firebase bulut tabanlı veritabanına erişmek ve bu veritabanında veri işlemleri yapmaktır. Bu nesne, Firebase Firestore veritabanına erişmek, belirli bir koleksiyon içinde belirli belgeleri okumak, yazmak, güncellemek veya silmek için kullanılır.
    //DAta Ayrintili bilgi, sayfanin en altinda. 3. bolumde

    private StorageReference storageReference;//6.bolumde ayrinti var. Örneğin, bir ürün resmi yüklemek istediğinizde, bu referansı kullanarak ilgili klasöre erişebilir ve resmi yükleyebilirsiniz. Bu sayede Firebase Storage'da dosyaları organize etmek ve yönetmek daha kolay hale gelir.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        registerLauncer();
        fireBaseStorage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference=fireBaseStorage.getReference();//suan bize bombos bir referan veriyor. Baz bir referans veriyor bize. Referans, biz ilgili oge yi
        //nereye koyacgimizi takip edecegimiz bir degiskendir.

    }

    public void uploadButtonClicked (View view) {
        //firebase de epolama ile veritabani farkli kavramlar olarak adlandiriliyor ve farkli islevleri var.
        //Depolama, genelde kullanicinin foto, video vb yani buyuk dosyalarinin kayit edildgi bir alan iken
        //Ver tabani ise, kullanicinin mail, sipasiin url i, sohbet uygulamasininda kullanicinin mesajlari vb.
        //yani daha dinamik olanlari veri tababnindas sakliyor firebase.
        //BUNUMLA ilgili notlara bu class in en altinda ulasabilirisnn.

        if (imageData != null) {
            storageReference.child("/images/image.png").putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }); //burasi hakkinda detayli mantik bilgisi asagida Not 7'de.


        }





    }

    public void selectImage (View view) {
        //manifest e izin istedigimizi belirrten kodu ekledik
        //once izin var mi onu kontrol edecgz.
        //eger izin yoksa, asaguida oyzellikle opermissinlerin android. (nokta) dan secmek onemli.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED){
                //eger izni gostermemenizin mantigini kullaniciya aktarmamiz gerekiyorsa ki bununAndroid sistemin kendisi karar veriyor.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view, "Izin Gerekli, Permission needed! Snackbar",Snackbar.LENGTH_INDEFINITE) //yani kullanici tamam yani anladim diyecegi ana kadar goster demis olduk
                            .setAction("Izin Ver, Give Permission Butonu", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //simdi olusturdugmuz "Izin Ver, Give Permission" butonuna tikladi ve ne olacagini bu methodun icerisine yaziyoruz.
                                    // yani biz burada "ask question ile kullanicdan izni istiyoruz."
                                    //ask permission //izin iste
                                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);


                                }
                            }) // yani burada kullanici ya bir buton gosterecegz ve izin ver gibi bir buton. Bu butonu tiklayacak ve tikladiktan sonra ise ne olacagini "bir Listener"
                            //ile koda dokecegiz.
                            .show();
                } else //yani kullaniciya iznin gosterilmesinin "mantigi" yoksa, yine ask question ile kullanicdan izni istiyoruz.. Yani yukaridaki onClick methodun da da burada da izni iyteyecgz ama burdakinin farki
                //kullaniciya ifade gostermeyesimiz.
                //ask permission //izin iste
                {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            } else //yani kullanici oncesinden zaten izin vermisse diger bir deyisle "ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED" false donerse
            {
                // o zaman zaten kullanicinin direkt galerisine gidecez.
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //direkt gidemiyorduk bu intent ile ancak bur result launcer ile biz ne yapacgimizi aciklayacagiz oncelikle. bununicin parametre olaak Intent isteyen activityresultlauncer
                //kullanacagiz. ve burada neler yaocagimizi anlatacgiz kendirisine.
                //ve ne yapmak istedigimizi asagida anlattik o halde gidelim  ve methodumuzu cagirqalim.
                activityResultLauncher.launch(intentToGallery); //burda izin devreden ciktigi icin yani izin zaten verilmis oldugu icin, kullaniciyi direkt aktiviteye yonlendiriyouz.
                //yukarida ise, kullanicdan henuz izin alamadik.

            }

        }
        //yani Android Surumu 33 ve altinda ise asagii yap. Yukaridakinin aynisi ama tek far su:
        //İlk blok, Manifest.permission.READ_MEDIA_IMAGES iznini kontrol eder.
        //İkinci blok ise Manifest.permission.READ_EXTERNAL_STORAGE iznini kontrol eder.
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view, "Izin Gerekli, Permission needed! Snackbar",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Izin Ver, Give Permission Butonu", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                                }
                            })
                            .show();
                } else
                {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else
            {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }
    }


    public void registerLauncer() {

        //1. register sonuc ve ne yapmak istedigimiz.
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //simdi kullanicinin gallery sine gittik, kullanici ne yapti ? Sonuc ne diyoruz? Birseyleri secti mi vaz mi gecti, sd kart mi cikti bunu da kontrol edecgz.
                if (result.getResultCode()== Activity.RESULT_OK)//hersey ok ise, yani kullanicinin gallerysine gitti isek
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
        });//bana bir sonuc olacagi yani bir eylem yapacagim icin bana bir Activity baslat dedim; ama sonrada bunun sonucunda yani sonuc da ne olacagini da Callback ile anlatmam gerekiyor.


        //2. register sonuc ve ne yapmak istedigimiz.
        // aslinda kodu yazarken ikinci yi yazmaya ilkin baslarsak daha mantikli olabilri. Yni permissionLauncher ve sonrasinda activityResultLauncher
        //methoduna devam etmek daha mantikli gorunuyor.
        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                //evet izin verildikten sonraki durumu anlatmak icin buradaki callback i kullandik.
                //buradaki paramtree baklildiginda bir result arguman olarak Result verilmi yani,. bize bir izin verildi mi ?
                //result=true demek bize izin verildigi anlamina geliyor.
                if (result){
                    //yukarida da belittigim gibi. kullanicidan izin alirsak ne yapacagimizi Result Launcher larda anlattik. Yani galeriye gidecek, ordan bilgilweri alcak ve simdi de
                    //o methidu burada cagiracagz. Ki izin Launcher ile activityLauncher imizi irtibatlandirmis olalim-
                    Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery); // ve yukarida activityResultLauncher'da ne yapmak isteidigmizi anlatmis ve buradad da bu intenti baslt<mis olduk.



                } else //yani izin verilmez ise?
                {
                    Toast.makeText(UploadActivity.this,"Permission needed! permissionLauncher", Toast.LENGTH_LONG).show();

                }

            }
        });




    }





}
//1.
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
//2.

/*
Evet, tabii ki! Firebase'de depolama ve veritabanı kavramları farklı ama birbirini tamamlayan iki önemli bileşendir. İşlevsellikleri ve kullanım alanları birbirinden oldukça farklıdır. İşte her ikisi arasındaki farkları ve örnekler:

Firebase Depolama (Firebase Storage):

Firebase Depolama, kullanıcıların medya dosyalarını (resimler, videolar, ses dosyaları vb.) saklamak için kullanılır.
Bu depolama alanı, genellikle kullanıcıların yüklediği veya indirdiği dosyaları barındırmak için kullanılır.
Örneğin, bir kullanıcının profil resmini yüklemesi veya uygulamaya ait resimlerin, videoların depolanması Firebase Depolama ile gerçekleştirilir.
Firebase Veritabanı (Firebase Realtime Database veya Firebase Firestore):

Firebase Veritabanı, uygulamanın dinamik verilerini saklamak ve senkronize etmek için kullanılır.
Veritabanı, uygulamanın kullanıcı bilgileri, mesajlar, siparişler, puanlar gibi dinamik verilerini depolar.
Örneğin, bir sohbet uygulamasında kullanıcıların mesajları veya bir e-ticaret uygulamasında siparişler Firebase Veritabanı'nda saklanır.
Örnek:

Bir fotoğraf paylaşım uygulaması düşünelim. Kullanıcılar bu uygulama aracılığıyla fotoğraflar yükleyebilirler. Kullanıcının fotoğrafı yüklendikten sonra Firebase Depolama'da saklanır. Fotoğrafın URL'si daha sonra Firebase Veritabanı'na kaydedilir. Bu sayede uygulama, kullanıcının yüklediği fotoğrafları depolama ve veritabanı arasında verimli bir şekilde yönetebilir.
Kısacası, Firebase Depolama genellikle büyük medya dosyalarını saklamak için kullanılırken, Firebase Veritabanı uygulamanın dinamik verilerini yönetmek ve senkronize etmek için kullanılır. Her ikisi de Firebase platformunda uygulama geliştirme sürecinde önemli roller üstlenir.
 */

//3.

/*
Firebase Firestore nesnesini oluşturmanın temel amacı, Firebase bulut tabanlı veritabanına erişmek ve bu veritabanında veri işlemleri yapmaktır. Bu nesne, Firebase Firestore veritabanına erişmek, belirli bir koleksiyon içinde belirli belgeleri okumak, yazmak, güncellemek veya silmek için kullanılır.

Yukarıdaki kodda, `FirebaseFirestore` nesnesi, `onCreate` metodunda Firebase bağlantısını oluşturmak için kullanılır. Bu nesne, veri tabanına erişim sağlar ve uygulamanın çeşitli bölümlerinde veri tabanı işlemlerini gerçekleştirmek için kullanılır. Örneğin, bu uygulamada, kullanıcıların yüklediği medya dosyalarını Firebase Firestore veritabanında saklamak için kullanılabilir.

Kısacası, `FirebaseFirestore` nesnesini oluşturarak uygulama, Firebase Firestore veritabanına erişmeyi sağlar ve bu veritabanında veri işlemlerini gerçekleştirebilir. Bu sayede uygulama, bulut tabanlı bir veri depolama ve yönetim çözümü üzerinde veri saklayabilir, güncelleyebilir ve silebilir.

 */

//4.
/*
`FirebaseAuth` nesnesi, Firebase Authentication servisine erişim sağlar ve kullanıcı kimlik doğrulama işlemlerini gerçekleştirmek için kullanılır. Bu nesne, kullanıcıların uygulamaya giriş yapması, kaydolması, şifrelerini sıfırlaması gibi kimlik doğrulama işlemlerini yönetir.

Yukarıdaki kodda, `FirebaseAuth` nesnesi, `onCreate` metodunda Firebase Authentication servisine erişim sağlamak için oluşturulmuştur. Bu nesne, kullanıcıların kimlik doğrulama işlemlerini yönetmek için kullanılabilir. Örneğin, kullanıcıların e-posta ve şifreleriyle giriş yapmasını sağlamak veya sosyal medya hesaplarıyla kimlik doğrulaması yapmalarını sağlamak için bu nesne kullanılabilir.

Kısacası, `FirebaseAuth` nesnesini oluşturarak uygulama, Firebase Authentication servisine erişim sağlar ve kullanıcı kimlik doğrulama işlemlerini gerçekleştirebilir. Bu sayede uygulama, kullanıcıların güvenli bir şekilde giriş yapmasını ve kayıt olmasını sağlayabilir.

 */

//5.
/*
`FirebaseStorage` nesnesi, Firebase Storage servisine erişim sağlar ve dosyaların yüklenmesi, indirilmesi ve depolanması gibi işlemleri gerçekleştirmek için kullanılır. Bu nesne, uygulamanın dosya tabanlı verileri güvenli ve ölçeklenebilir bir şekilde saklamasını sağlar.

Yukarıdaki kodda, `FirebaseStorage` nesnesi, `onCreate` metodunda Firebase Storage servisine erişim sağlamak için oluşturulmuştur. Bu nesne, kullanıcıların uygulamaya yüklediği dosyaları depolamak için kullanılabilir. Örneğin, kullanıcıların profil fotoğraflarını veya uygulamada paylaşacakları resimleri Firebase Storage üzerine yükleyebilir ve bu dosyalara erişim sağlayabilirler.

Kısacası, `FirebaseStorage` nesnesini kullanarak uygulama, dosyaları güvenli bir şekilde depolayabilir ve bu dosyalara erişim sağlayabilir. Bu sayede uygulama, kullanıcıların dosyalarını saklamak ve paylaşmak için güvenilir bir çözüm sunar.
 */
//6.
/*
`storageReference = fireBaseStorage.getReference();` kod satırı, Firebase Storage'da bir referans oluşturmak için kullanılır.

Firebase Storage, hiyerarşik bir yapıya sahip bir bulut depolama hizmetidir. Depolama birimleri, depolama alanı içinde düzenli bir şekilde düzenlenmiş "küpler" şeklinde organize edilir. Bu depolama birimlerine erişmek ve üzerlerinde işlemler yapmak için bir referansa ihtiyaç duyulur. Bu referanslar, belirli bir depolama birimine veya belirli bir dosyaya işaret eder.

`fireBaseStorage.getReference()` metodu, varsayılan depolama birimine bir referans döndürür. Bu referans, depolama birimine genel erişim sağlar. Daha sonra bu genel referans üzerinden belirli bir klasöre veya dosyaya erişmek için alt klasörler veya dosya adları eklenir.

Yukarıdaki kod satırında, `storageReference` değişkeni, Firebase Storage'da işlem yapmak için kullanılacak genel bir referansa atanır. Bu referans, Firebase Storage'da dosyaları yüklemek, indirmek veya silmek gibi işlemleri gerçekleştirmek için kullanılabilir.
Tabii, ```storageReference = fireBaseStorage.getReference();``` kodu, Firebase Storage'da kullanılacak bir referans nesnesi oluşturur. Bu nesne, Firebase Storage'da belirli bir konumu temsil eder.

Tabii, aşağıdaki örnekte storageReference nesnesi, Firebase Storage'da bir konumu temsil eder. Bu konum, yüklenen resmin nereye kaydedileceğini belirtir.

Örneğin, kullanıcı bir resim seçtiğinde ve yüklemeyi onayladığında, bu seçilen resim Firebase Storage'da belirli bir konuma yüklenir. Bu konumu belirlemek için storageReference nesnesi kullanılır.

storageReference = fireBaseStorage.getReference().child("images").child(imageData.getLastPathSegment());

Bu kod parçası, storageReference nesnesini oluştururken, Firebase Storage'da "images" adlı bir klasör altında ve resmin orijinal adıyla birlikte belirli bir konumu hedefler. Bu, yüklenen resmin "images" klasörü altında orijinal adıyla saklanacağını belirtir.

Bu şekilde, yüklenen her resim Firebase Storage'da "images" klasörü altında saklanır ve bu konumu belirlemek için storageReference nesnesi kullanılır. Bu sayede, yüklenen dosyaları düzenli bir şekilde saklamak ve yönetmek kolaylaşır.



//
Firebase Storage, bulut tabanlı bir depolama çözümüdür ve dosyaları saklamak için kullanılır. Her dosya veya klasör, bir konuma sahiptir. Bu konumlar, Firebase Storage'da birer referans olarak adlandırılır.

Yukarıdaki kod, Firebase Storage'ın başlangıç noktasına bir referans oluşturur. Bu başlangıç ​​noktası, genellikle Firebase projesinin kök klasörüdür. Bu referans, Firebase Storage'da dosya veya klasörler oluşturmak, erişmek veya silmek için kullanılır.

Örneğin, bir ürün resmi yüklemek istediğinizde, bu referansı kullanarak ilgili klasöre erişebilir ve resmi yükleyebilirsiniz. Bu sayede Firebase Storage'da dosyaları organize etmek ve yönetmek daha kolay hale gelir.


Tabii, bir e-ticaret uygulamasını düşünelim. Kullanıcılar uygulama üzerinden ürün resimlerini ve bilgilerini görüntüleyebilirler. Satıcılar da ürünlerini uygulamaya yükleyebilirler. Bu durumda Firebase Storage ve Firebase Authentication kullanılabilir.

Firebase Authentication:
Kullanıcılar, uygulamaya giriş yapmak ve hesaplarını yönetmek için Firebase Authentication kullanabilirler. Kullanıcı adı ve şifreyle giriş yapabilirler veya Google, Facebook gibi üçüncü taraf kimlik doğrulama sağlayıcılarıyla giriş yapabilirler. Bu şekilde, kullanıcıların kimlikleri güvenle doğrulanır ve uygulamaya giriş yapar yapmaz kendi profillerine erişebilirler.

Firebase Storage:
Satıcılar, ürünlerinin resimlerini Firebase Storage'a yükleyebilirler. Örneğin, bir satıcı yeni bir ürün eklemek istediğinde, uygulama üzerinden ürün bilgilerini girer ve ürün resimlerini seçer. Ardından, bu resimler Firebase Storage'a yüklenir ve her bir ürün için birer URL oluşturulur. Bu URL'ler, uygulamadaki ilgili ürün sayfasında görüntülenebilir.

Dolayısıyla, Firebase Authentication kullanıcıların kimlik doğrulamasını sağlar ve Firebase Storage ürün resimlerinin güvenli bir şekilde saklanmasını ve erişilmesini sağlar. Bu şekilde, kullanıcılar uygulama üzerinden güvenle ürünleri inceleyebilir ve satın alabilirler.
 */

//7
/*
´´´´´´´´´
Bu yöntem, bir resmi Firebase Storage'a yüklemek için çağrılır. İşlevin adı "uploadButtonClicked", bu nedenle bu yöntem, bir yükleme düğmesine tıklandığında gerçekleşen olayları işler.

İşlevin adım adım açıklaması şu şekildedir:

1. `uploadButtonClicked` adlı bir metodumuz var ve bu metod bir `View` parametresi alır. Bu metod, bir düğmeye tıklandığında çağrılır.

2. İlk olarak, metod içinde `if (imageData != null)` koşulu kontrol edilir. Bu koşul, imageData adlı bir değişkenin null olup olmadığını kontrol eder. imageData, yüklenecek resmin verilerini içerir. Eğer imageData null değilse, yani yüklenecek bir resim varsa, yükleme işlemi başlatılır.

3. Yükleme işlemi başlatıldığında, `storageReference` adlı bir nesne üzerinde `child` metodu kullanılarak yüklenecek dosyanın yolunu belirleriz. Bu örnekte, "/images/image.png" olarak belirlenmiştir. Bu, Firebase Storage'da "images" klasörü içinde "image.png" adlı bir dosyanın oluşturulacağını belirtir.

4. Ardından, `putFile` metodu ile imageData, belirtilen yola yüklenir. Bu işlem, bir `UploadTask` nesnesi döndürür ve bu işlem asenkron olarak gerçekleşir.

5. `addOnSuccessListener` metodu, yükleme işlemi başarılı olduğunda çağrılır. Bu metot, `OnSuccessListener` arayüzünü implemente eden bir nesne alır ve başarılı bir yükleme işlemi gerçekleştiğinde bu nesnenin `onSuccess` metodu çağrılır. Bu kısımda, genellikle başarılı yükleme sonrası yapılacak işlemler yer alır.

6. `addOnFailureListener` metodu, yükleme işlemi başarısız olduğunda çağrılır. Bu metot, `OnFailureListener` arayüzünü implemente eden bir nesne alır ve yükleme işlemi başarısız olduğunda bu nesnenin `onFailure` metodu çağrılır. Bu kısımda, başarısız yükleme sonrası yapılacak işlemler yer alır.

Yukarıdaki açıklamalar, resim yükleme senaryosunu adım adım açıklar. Kullanıcı bir resim seçer ve ardından "Yükle" düğmesine tıklar. Ardından, seçilen resim Firebase Storage'a yüklenir. Eğer yükleme işlemi başarılıysa, `onSuccess` metodu ile ilgili işlemler gerçekleştirilir. Eğer yükleme işlemi başarısız olursa, `onFailure` metodu ile ilgili işlemler gerçekleştirilir.
´´´´´´´´´´
Tabii, simitçi örneğiyle bu ifadeyi açıklayalım:

Diyelim ki bir simitçi dükkânı var ve müşteriler sipariş veriyorlar. Siparişlerini veren müşteriler, simitçiye istedikleri simidi belirtirler ve simitçi de bu siparişi hazırlar. Simitçi, siparişi hazırlarken başka işleri de aynı anda yapabilir, örneğin başka bir müşteriye servis yapabilir veya yeni simitler hazırlayabilir.

İşte burada, sipariş veren müşteri bir "UploadTask" nesnesine benzetilebilir. Müşteri, simitçiye bir sipariş verir ve ardından beklemeye başlar. Sipariş vermek, asenkron bir işlemdir; yani müşteri siparişi verdikten sonra beklerken simitçi başka işleri de yapabilir.

Simitçi, müşterinin siparişini alır ve hazırlamaya başlar. Bu süreç de asenkron olarak gerçekleşir; çünkü simitçi bir siparişi hazırlarken diğer müşterilere de hizmet verebilir veya yeni siparişler alabilir.

Sonuç olarak, "Bu işlem, bir UploadTask nesnesi döndürür ve bu işlem asenkron olarak gerçekleşir" ifadesi, simitçi örneğindeki gibi bir durumu açıklar. Müşteri sipariş verir (UploadTask nesnesi oluşturulur) ve ardından beklemeye geçer. Sipariş hazırlanırken (asenkron olarak), müşteri beklemeye devam eder ve simitçi diğer işlerini yapar. Bu süreç tamamlandığında, müşteriye siparişi teslim edilir (başarılı veya başarısız) ve müşteriye geri dönülür.
´´´´´´´´´´´´´
Tabii, somutlaştırmak için simitçi örneğini daha detaylı bir şekilde ele alalım:

Diyelim ki bir simitçi dükkânında bir müşteri var ve bu müşteri bir simit siparişi veriyor. Simitçi dükkânında, simit pişirme alanı ve siparişleri hazırlamak için bir tezgah var.

1. Müşteri, simitçiye giderek "Bir simit almak istiyorum." diyor. Bu, müşterinin siparişi verdiği andır.
2. Simitçi, müşterinin siparişini alır ve simit pişirme alanına gider. Siparişin hazırlanması (simitin pişirilmesi ve istenilen şekilde hazırlanması) bir süre alabilir. Bu süreç, asenkron olarak gerçekleşir; çünkü simitçi aynı anda başka müşterilere de hizmet verebilir veya dükkânla ilgili başka işlerle ilgilenebilir.
3. Simit hazırlandıktan sonra, simitçi siparişi tezgaha getirir ve müşteriye sunar. Müşteri, siparişini alır ve dükkândan ayrılır.

Şimdi bu örneği, Firebase'de bir dosyanın yüklenmesi süreciyle ilişkilendirelim:

1. Kullanıcı, uygulamada bir fotoğrafı yüklemek istiyor ve "Yükle" düğmesine basıyor. Bu, kullanıcının Firebase Storage'a bir dosya yüklemek istediği andır.
2. Uygulama, kullanıcının seçtiği fotoğrafı alır ve Firebase Storage'a yüklemek için bir işlem başlatır. Bu işlem, bir "UploadTask" nesnesi oluşturur ve dosyanın yüklenmesi (asenkron olarak) başlar.
3. Dosyanın yüklenmesi bir süre alabilir ve bu süreç arka planda gerçekleşir. Bu süreçte, kullanıcı uygulamayı kullanmaya devam edebilir veya başka işlemler yapabilir.
4. Dosya başarıyla yüklendikten sonra veya bir hata oluştuğunda, Firebase Storage işlemi bu durumu bildirir (başarı veya başarısızlık) ve uygulama bu duruma uygun şekilde tepki verir.

Bu şekilde, Firebase Storage'da bir dosyanın yüklenmesi, simitçi dükkânındaki siparişin hazırlanması gibi asenkron bir süreç olarak işler. Müşteri/sipariş sahibi (kullanıcı) siparişi verir ve ardından beklerken (asenkron olarak), simitçi/dosya yükleme işlemi diğer işlerini yapar. Sonunda, sipariş/dosya hazır olduğunda, müşteriye/sipariş sahibine geri bildirim verilir.
´´´´´´´´´´
Dosya yükleme işlemi sırasında, uygulama kullanıcının deneyimini geliştirmek veya işlevselliği artırmak için çeşitli işlemler yapabilir. İşte dosya yükleme işlemi sırasında yapılabilecek bazı işlemler:

1. **İlerleme Göstergesi:** Kullanıcıya dosyanın yüklendiğini göstermek için bir ilerleme göstergesi veya yükleme çubuğu sağlanabilir. Bu, kullanıcının dosya yükleme işleminin ilerleyişini görmesine yardımcı olur.

2. **Hata İşleme:** Dosya yükleme sırasında herhangi bir hata oluşabilir. Bu hatalar kullanıcıya bildirilmeli ve uygun bir şekilde ele alınmalıdır. Kullanıcıya neyin yanlış gittiği hakkında bilgi vermek, kullanıcı deneyimini iyileştirebilir.

3. **Arka Planda Diğer İşlemler:** Dosya yükleme işlemi arka planda gerçekleştiğinden, kullanıcı uygulama içinde başka işlemler yapabilir veya uygulamadan ayrılabilir. Uygulama, dosya yükleme işlemi tamamlanana kadar diğer işlemlere devam edebilir.

4. **Yükleme İptali veya Duraklatma:** Kullanıcı dosya yükleme işlemini iptal etmek veya duraklatmak isteyebilir. Bu durumda, uygulama bu isteği karşılayarak dosya yükleme işlemini duraklatabilir veya iptal edebilir.

5. **Ek Bilgilerin Toplanması:** Dosya yükleme işlemi sırasında, kullanıcıdan dosya hakkında ek bilgiler veya açıklama istenebilir. Bu bilgiler dosyanın adı, açıklaması, kategorisi vb. olabilir ve bu bilgiler Firebase veritabanına veya başka bir yere kaydedilebilir.

6. **Dosya Türüne Bağlı İşlemler:** Dosyanın türüne bağlı olarak, dosya yükleme işlemi sırasında farklı işlemler yapılabilir. Örneğin, bir fotoğraf yüklendiğinde, uygulama otomatik olarak bir önizleme gösterebilir veya fotoğrafın boyutunu veya formatını değiştirebilir.

Bu işlemler, dosya yükleme işlemi sırasında kullanıcının deneyimini zenginleştirmek ve daha etkili bir şekilde yönetmek için uygulanabilir.
´´´´´´´´´´´´´´´
Evet, dosya yükleme gibi işlemler genellikle asenkron işlemler olarak adlandırılır. Asenkron işlemler, işlemlerin paralel olarak ve birbirine bağlı olmayan şekilde gerçekleştirildiği durumları ifade eder. Dosya yükleme işlemi sırasında, uygulama dosyanın yüklenmesini beklerken diğer işlemleri gerçekleştirebilir ve kullanıcı etkileşimini sürdürebilir. Bu sayede uygulama daha hızlı ve verimli bir şekilde çalışabilir.

Asenkron işlemler, özellikle ağ çağrıları, dosya işlemleri, veritabanı işlemleri gibi zaman alıcı veya giriş/çıkış gerektiren işlemler için yaygın olarak kullanılır. Bu işlemler tamamlandığında genellikle bir geri çağrı veya bir promise gibi bir mekanizma kullanılarak sonuçlar işlenir veya kullanıcıya bildirilir.

Dosya yükleme işlemi sırasında, işlem tamamlandığında veya bir hata oluştuğunda geri çağrılar kullanılarak bu durumlar işlenir ve uygulama duruma göre tepki verir. Bu esneklik, kullanıcı deneyimini iyileştirmek ve uygulamanın daha güvenilir ve etkili olmasını sağlamak için önemlidir.
´´´´´´´´´´´´´´´´´´´´
 */