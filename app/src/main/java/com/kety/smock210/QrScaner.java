package com.kety.smock210.ovnsicorrectqrfull;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QrScaner extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private FrameLayout preview;
    private TextView scanText;
    private ToggleButton startScan;
    private ImageScanner scanner;
    private boolean previewing = true;
    private String lastScannedCode;
    private Image codeImage;
    private  String retIma;
    private String strItog = "";
    private ImageButton zoomB;
    private ImageButton flash;
    private ImageButton back;
    private boolean isFlashOn;
    private boolean hasFlash;
    private int zoom = 1;
   // private ImageView imageV;




    static {
        System.loadLibrary("iconv");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scaner);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        autoFocusHandler = new Handler();

        preview = (FrameLayout) findViewById(R.id.cameraPreview);
        zoomB = (ImageButton) findViewById(R.id.zoomB);
        flash = (ImageButton) findViewById(R.id.flash);
        back = (ImageButton) findViewById(R.id.back);

        zoomB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCamera != null) {

                    Camera.Parameters p = mCamera.getParameters();
                    int maxZoom = p.getMaxZoom();
                    int minZoom = 1;
                    int stepZoom = maxZoom / 3;

                    zoom = p.getZoom();

                    if (p.isZoomSupported()) {
                        zoom += stepZoom;
                        if (zoom > maxZoom) {
                            zoom = 1;
                        }
                        p.setZoom(zoom);
                    }
                    mCamera.stopPreview();

                    mCamera.setParameters(p);

                   /* try {
                        mCamera.setPreviewDisplay();
                    } catch (Exception e) { }*/

                    mCamera.startPreview();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseCamera();
            }
        });

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCamera != null) {
                    hasFlash = getApplicationContext().getPackageManager()
                            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                    if (hasFlash) {
                        Camera.Parameters p = mCamera.getParameters();

                        mCamera.stopPreview();

                        //this.f36968s.setVisibility(0);
                        if (isFlashOn) {
                            p.setFlashMode(p.FLASH_MODE_OFF);
                            mCamera.setParameters(p);
                            mCamera.startPreview();
                            isFlashOn = false;
                            flash.setImageResource(R.drawable.flash_on);
                        } else {

                            p.setFlashMode(p.FLASH_MODE_TORCH);
                            mCamera.setParameters(p);
                            mCamera.startPreview();
                            isFlashOn = true;
                            flash.setImageResource(R.drawable.flash_off);
                        }
                        return;
                    }
                    //this.f36968s.setVisibility(8);

                }
            }
        });
        //imageV = (ImageView) findViewById(R.id.imageRes);


        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);



        scanText = (TextView) findViewById(R.id.scanText);


        scanText.setMovementMethod(new ScrollingMovementMethod());
        startScan = (ToggleButton) findViewById(R.id.startScan);

        startScan.setOnCheckedChangeListener(this);//
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            scanText.setText(getString(R.string.scanText));
            //codeImage.setData(null);
            resumeCamera();
        }
        else
            releaseCamera();

    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            //
        }
        return c;
    }


    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;

            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mPreview.refreshDrawableState();
            mCamera.stopPreview();
            mCamera.release();
            retIma = null;
            strItog = "";
            codeImage = null;
            mCamera = null;

        }
    }

    private void resumeCamera() {
        //scanText.setText(getString(R.string.scan_process_label));
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        //Camera.Size size = mCamera.getParameters().getPreviewSize();
        //mPreview.set
        preview.removeAllViews();
        preview.addView(mPreview);
        preview.addView(zoomB);
        preview.addView(flash);
        if (mCamera != null) {
            int parentX = preview.getWidth();
            int parentY = preview.getHeight();
            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            codeImage = new Image(parentX, parentY, "Y800");
            previewing = true;
            /**RectF rectDisplay = new RectF();
             RectF rectPreview = new RectF();

             // RectF экрана, соотвествует размерам экрана
             rectDisplay.set(10, 0, parentY,parentX );

             // RectF первью

             // превью в вертикальной ориентации
             rectPreview.set(0, 0, size.height, size.width);


             Matrix matrix = new Matrix();

             // если превью будет "втиснут" в экран (второй вариант из урока)
             matrix.setRectToRect(rectPreview, rectDisplay,
             Matrix.ScaleToFit.START);

             // преобразование
             matrix.mapRect(rectPreview);

             mPreview.getLayoutParams().height = (int) (rectPreview.bottom);
             mPreview.getLayoutParams().width = (int) (rectPreview.right);*/
            mPreview.refreshDrawableState();
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing && mCamera != null) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    private String m3362c(String str, int i) {
        String encodeToString;
        Throwable e;

        try {
            if (str.substring(0, 6).equals("ST0001")) {
                String str2;
                switch (str.charAt(6)) {
                    case 49 /*49*/:
                        str2 = new String(str.getBytes("ISO-8859-1"), "windows-1251");
                        encodeToString = Base64.encodeToString(str2.getBytes("windows-1251"), 0);
                        str = str2;
                        break;
                    case 50 /*50*/:
                        str2 = new String(str.getBytes("ISO-8859-1"), "UTF-8");
                        encodeToString = Base64.encodeToString(str2.getBytes("utf-8"), 0);

                        break;
                    case 51 /*51*/:
                        str2 = new String(str.getBytes("ISO-8859-1"), "KOI8-R");
                        encodeToString = Base64.encodeToString(str2.getBytes("KOI8-R"), 0);
                        str = str2;
                        break;
                    default:
                        encodeToString = Base64.encodeToString(str.getBytes(), 0);
                        break;
                }
            }
            encodeToString = Base64.encodeToString(str.getBytes(), 0);
            retIma = str;

        } catch (Throwable e3) {
            Throwable th = e3;
            encodeToString = null;
            e = th;
            return encodeToString;
        }
        return encodeToString;
    }

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            int parentX = preview.getWidth();
            int parentY = preview.getHeight();
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            codeImage = new Image(size.width, size.height, "Y800");
            Log.d("CameraTestActivity", "onPreviewFrame data length = " + parentX + "-" +parentY );
            codeImage.setData(data);
            Log.d("CameraTestActivity", "onPreviewFrame data length = 1" );
            int result = scanner.scanImage(codeImage);

            Log.d("CameraTestActivity", "onPreviewFrame data length = 2" );
            Iterator it = scanner.getResults().iterator();
            Log.d("CameraTestActivity", "onPreviewFrame data length = 3" );

            while (it.hasNext()) {
                String data2 = ((Symbol) it.next()).getData();
                if (!TextUtils.isEmpty(data2)) {
                    try {
                        //Toast.makeText(getApplicationContext(),
                        //        data2, Toast.LENGTH_SHORT).show();
                        if (data2.contains("ST000")) {
                            m3362c(data2,-1);
                            Log.d("CameraTestActivity", "onPreviewFrame data length = 3" );
                        } else if (data2.toUpperCase().contains("HTTP")) {
                            releaseCamera();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data2));
                            startActivity(browserIntent);
                            //Log.d("CameraTestActivity", "onPreviewFrame data length = 4" );
                        } else {
                            releaseCamera();
                            Intent intent = new Intent(QrScaner.this, CreateQr.class);
                            //Передаем на следующую аквтиность слово в статическую переменную
                            intent.putExtra("qr", data2);
                            startActivity(intent);


                        }
                        //Log.d("CameraTestActivity", data2 );


                        //scanText.setText(retIma);
                    } catch (Throwable ignored) {
                        scanText.setText("error");
                    }
                }
            }

            lastScannedCode = retIma;
            Log.d("CameraTestActivity", "onPreviewFrame data length = " + lastScannedCode);

            if (lastScannedCode != null) {


                Toast toast = Toast.makeText(getApplicationContext(),
                        lastScannedCode, Toast.LENGTH_SHORT);
                toast.show();
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(lastScannedCode);
                if (m.matches() ){
                    return;
                }
                startScan.setChecked(false);

                qrCheecken qrC = new qrCheecken();
                a1919 a = qrC.checkQr(lastScannedCode);
                releaseCamera();


                Intent intent = new Intent(QrScaner.this, ActivityResault.class);
                //Передаем на следующую аквтиность слово в статическую переменную

                intent.putExtra("eroor", a.products);
                intent.putExtra("qr", a.buf.toString());
                intent.putExtra("origQr", lastScannedCode);
                intent.putExtra("lenqr", lastScannedCode.length()+"");
                startActivity(intent);
            }
            camera.addCallbackBuffer(data);

        }
    };


    // Mimic continuous auto-focusing
    final Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}
