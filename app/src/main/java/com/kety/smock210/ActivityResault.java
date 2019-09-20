package com.kety.smock210.ovnsicorrectqrfull;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityResault extends Activity {
    public static final String eroor = "";
    public static final String qr = "";
    public static  String isxqr = "";
    public static  String parqr = "";

    private TextView wv;
    private TextView scanOk;
    private ListView qrResult;
    private Button btnBack;
    ArrayList<Errors> str;
    BoxAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resault);

        Bundle extras = getIntent().getExtras();

        wv = (TextView) findViewById(R.id.webResult);
        scanOk = (TextView) findViewById(R.id.scanOk);
        str =  getIntent().getParcelableArrayListExtra("eroor");
        btnBack = (Button) findViewById(R.id.back);

        boxAdapter = new BoxAdapter(this, str);

        // настраиваем список
        qrResult = (ListView) findViewById((R.id.qrResult));
        qrResult.setAdapter(boxAdapter);

        if (str.size()<1){
            scanOk.setText("Структура ДШК корректная" +System.getProperty("line.separator") + "Длина ДШК "+extras.getString("lenqr") + " символов" );
        } else {
            scanOk.setText("Длина ДШК "+extras.getString("lenqr") + " символов" );
        }
        isxqr = extras.getString("qr");
        parqr = extras.getString("origQr");



        wv.setText(Html.fromHtml(extras.getString("qr")));
        wv.setMovementMethod(new ScrollingMovementMethod());

        wv.setOnClickListener(new View.OnClickListener(){
            @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
            @SuppressWarnings("deprecation")
            @TargetApi(11)
            @Override
            public void onClick(View v) {   // User-defined onClick Listener
                int sdk_Version = android.os.Build.VERSION.SDK_INT;
                //Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                StringBuilder strErrorFull = new StringBuilder();
                for (Errors error:str
                        ) {
                    strErrorFull.append(" - " + error.text).append(System.getProperty("line.separator"));
                    if (error.fuulText.length()>0){
                        strErrorFull.append(error.fuulText).append(System.getProperty("line.separator"));
                    }

                }
                String txt = "";
                if (str.size()>0){
                    txt =  System.getProperty("line.separator") + "Текст ошибок"+ System.getProperty("line.separator") + strErrorFull.toString();
                } else {
                    txt = System.getProperty("line.separator") + "Структура ДШК корректная";
                }
                if(sdk_Version < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(wv.getText().toString() + txt);   // Assuming that you are copying the text from a TextView
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                }
                else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", wv.getText().toString() + txt);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ///qrResult.setText(extras.getString(eroor));

        View.OnClickListener oclButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivityTwo.this, MainActivity.class);
                //Передаем на следующую аквтиность слово в статическую переменную
                startActivity(intent);*/
                onBackPressed();
                //return;
            }
        };
        btnBack.setOnClickListener(oclButton);
    }

    void setQrCode(){
        Spanned spannedText = Html.fromHtml(qr);
        Spannable reversedText = revertSpanned(spannedText);
    }

    final Spannable revertSpanned(Spanned stext) {
        Object[] spans = stext.getSpans(0, stext.length(), Object.class);
        Spannable ret = Spannable.Factory.getInstance().newSpannable(stext.toString());
        if (spans != null && spans.length > 0) {
            for(int i = spans.length - 1; i >= 0; --i) {
                ret.setSpan(spans[i], stext.getSpanStart(spans[i]), stext.getSpanEnd(spans[i]), stext.getSpanFlags(spans[i]));
            }
        }
        return ret;
    }



    public void visibleIsx(View view) {

        wv.setText(Html.fromHtml(parqr));
    }

    public void visibleCheck(View view) {
        wv.setText(Html.fromHtml(isxqr));
    }
}
