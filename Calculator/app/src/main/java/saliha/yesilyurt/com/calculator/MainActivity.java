package saliha.yesilyurt.com.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // binary islemler icin sabitler:
    private static final int opNone = 0;
    private static final int opAdd = 1;
    private static final int opSubtract = 2;
    private static final int opMultiply = 3;
    private static final int opDivide = 4;


    private TextView tvDisplay;

    // durumu gösteren bayraklar
    private boolean enteringDigits = false;
    private boolean decimalPointAllowed = true;

    // hesaplama değerleri:
    private double result = 0.0;
    private double lastNumberEntered = 0.0;

    // beklemedeki operasyonlar icin
    private int pendingOperation = opNone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // textviewi id ile buluyoruz
       tvDisplay = (TextView) findViewById(R.id.tvDisplay);

    }


   public void digitPressed(View view) {

        // tikladigimiz herhangi butonu alıyoruz
        Button btn = (Button) view;
        //ve onun icindeki sayiyi ( digiti) aliyoruz
        String digit = (String) btn.getText();

        // kullanici zaten onceden digit girdi ise
        if (enteringDigits) {
            if (digit.equals(".")) {
                //tek numara icin ondalık basamaga izin verilip verilmedigi
                if (decimalPointAllowed) {
                    // . yi ekledikten sonra stringin suanki durumunu gostermek icin
                    tvDisplay.setText(String.format("%s.", tvDisplay.getText()));
                    decimalPointAllowed = false;
                }
            } else {
                //. girmessek yani ondalik sayi degilse textviewde yazan sayinin yanina bir sayi daha yazıyoruz
                tvDisplay.setText(String.format("%s%s", tvDisplay.getText(), digit));
            }
        } else {
            // textview a ilk defa sayi giriyorsak
            tvDisplay.setText(digit);
            // enteringDigits i true yapıyoruz cunku 1 tane de olsa digit girdik.
            enteringDigits = true;
        }
    }


    //+,-,*,= gibi operasyon butonlarinan bastıgimizda
    public void operationPressed(View view) {

        // we're no longer entering digits, and the next number may
        // contain a decimal point:
        // doperasyon oldugu icin digit girisini false yaptik
        enteringDigits = false;
        decimalPointAllowed = true;

        // textviwede gorunen stringi double a cevirdik
        lastNumberEntered = Double.parseDouble((String) tvDisplay.getText());


        // Buton ustundeki yaziyi CharSequence attik
        Button btn = (Button) view;
        CharSequence opString = btn.getText();

        // butondaki textin ilk karakterini operation a attik
        char operation = opString.charAt(0);

        // eger bekleyen islem varsa hesapla, bunun icin hesapla() metoduna gir
        if (pendingOperation != opNone) {
            calculate();
        } else {
            // else, yoksa son girilen sayiyi sonuc olarak goster
            result = lastNumberEntered;
        }





        switch (operation) {
            case 'A':   //all clear
                result = 0.0;
                pendingOperation = opNone;
            case 'C':   //clear (all clear falls through to this):
                tvDisplay.setText("0");
                break;
            case '+':   //toplama ya da eksileme
                if (opString.length() > 1) {    //eger stringin uzunlugu 1 den buyukse demek ki bu bir sayi, o zaman eksile
                    result = -result;
                    tvDisplay.setText(String.format("%f", result));
                } else {                        //add
                    pendingOperation = opAdd; // 1 den buyuk degilse demk ki + operatoru
                }
                break;
            case '-':   //cikarma
                pendingOperation = opSubtract;
                break;
            case '*':   //carpma
                pendingOperation = opMultiply;
                break;
            case '/':   //bolme
                pendingOperation = opDivide;
                break;
            case '=':    //esittir
                pendingOperation = opNone;
                break;
            default:
                break;
        }
    }

    private void calculate() {
        // bekleyen isleme gore hesap yapma.
        switch (pendingOperation) {
            case 1:  //toplama
                result = result + lastNumberEntered;
                break;
            case 2:   //cikarma
                result = result - lastNumberEntered;
                break;
            case 3: // carpma
                result = result * lastNumberEntered;
                break;
            case 4:  //bolme
                result = result / lastNumberEntered;
                break;
            default:
                Log.e("calc", "ERROR: Attempted unknown operation in method \"calculate\"");
        }

        // hesaplama sonucunu textviewde gostermek icin
        tvDisplay.setText(String.format("%f", result));

    }
}



