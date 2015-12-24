package com.tony.mocadev.dolpicapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by chcjswo on 2015-08-05.
 * 클라인어트랑 웹뷰랑 통신하는 클래스
 */
public class WebViewInterface {

    private WebView _webView;
    private Activity _activity;
    private Button btnLeft, btnRight, btnDownload;
    private ProgressDialog mProgressDialog;

    private int _nLeftSeq, _nRightSeq;
    private short _nDownloadBtnChk;
    public int getLeftSeq() {
        return _nLeftSeq;
    }
    public void setLeftSeq(int _nLeftSeq) {
        this._nLeftSeq = _nLeftSeq;
    }
    public int getRightSeq() {
        return _nRightSeq;
    }
    public void setRightSeq(int _nRightSeq) {
        this._nRightSeq = _nRightSeq;
    }
    public short getDownloadBtnChk() {
        return _nDownloadBtnChk;
    }
    public void setDownloadBtnChk(short _nDownloadBtnChk) {
        this._nDownloadBtnChk = _nDownloadBtnChk;
    }

    /**
     * 생성자.
     * @param activity : context
     * @param view : 적용될 웹뷰
     */
    public WebViewInterface(Activity activity, WebView view) {
        _webView = view;
        _activity = activity;

        btnLeft = (Button) _activity.findViewById(R.id.btnLeft);
        btnRight = (Button) _activity.findViewById(R.id.btnRight);
        btnDownload = (Button) _activity.findViewById(R.id.btnDownload);
    }

    /**
     * 안드로이드 토스트를 출력한다. Time Long.
     * @param a_sMsg : 메시지
     */
    @JavascriptInterface
    public void toastLong(String a_sMsg) {
        Toast.makeText(_activity, a_sMsg, Toast.LENGTH_LONG).show();
    }

    /**
     * 안드로이드 토스트를 출력한다. Time Short.
     * @param a_sMsg : 메시지
     */
    @JavascriptInterface
    public void toastShort(String a_sMsg) throws IOException {
        Toast.makeText(_activity, a_sMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 네비버튼 컨트롤.
     * @param a_nLeft : 왼쪽 버튼 seq
     * @param a_nRight : 오른쪽 버트 seq
     * @param a_bDownload : 다운로드 버튼 활성화 여부
     */
    @JavascriptInterface
    public void navibtn(int a_nLeft, int a_nRight, short a_bDownload) {
        // 각 Seq값 셋팅
        setLeftSeq(a_nLeft);
        setRightSeq(a_nRight);
        setDownloadBtnChk(a_bDownload);

        // UI를 다시 그리기 위한 쓰레드
        new Thread() {
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = UiEnum.ImageLeftRight.value;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 이미지 다운로드.
     * @param a_sImgSrc : 이미지 경로
     */
    @JavascriptInterface
    public void imageDownload(String a_sImgSrc) {
        // UI를 다시 그리기 위한 쓰레드
        new Thread() {
            public void run(){
                Message msg = handler.obtainMessage();
                msg.what = UiEnum.DownBtnOn.value;
                handler.sendMessage(msg);
            }
        }.start();

        // 파일 이름 추출
        String sFileName = a_sImgSrc.substring(a_sImgSrc.lastIndexOf("/") + 1);

        // 저장 폴더 생성
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/DolPic");
        if (!folder.exists())
            folder.mkdirs();

        String extStorageDirectory = folder.toString();
        File file = new File(extStorageDirectory, sFileName);

        OutputStream outStream = null;

        try {
            outStream = new FileOutputStream(file);
            Bitmap bitmap = getBitmapFromURL(a_sImgSrc);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

            Toast.makeText(_activity, R.string.image_save_finish, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(_activity, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(_activity, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_activity, R.string.try_download, Toast.LENGTH_SHORT).show();
        }

        // UI를 다시 그리기 위한 쓰레드
        new Thread() {
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = UiEnum.DownBtnOff.value;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 저장할 bitmap 이미지.
     * @param a_sImgSrc : 이미지 경로
     */
    private Bitmap getBitmapFromURL(String a_sImgSrc) {
        InputStream in = null;
        Bitmap mBitmap = null;
        try {
            in = new java.net.URL(a_sImgSrc).openStream();
            mBitmap = BitmapFactory.decodeStream(in);
            in.close();

            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    // UI를 다시 그리기 위한 쓰레드
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            UiEnum status = UiEnum.fromValue(msg.what);

            Drawable drawableTop = null;

            switch(status) {
                // ui 쓰레드가 네비게이션 버튼인 경우
                case ImageLeftRight:

                    // 해당 값들이 있으면 활성화
                    if (getLeftSeq() > 0) {
                        btnLeft.setEnabled(true);
                        drawableTop = ContextCompat.getDrawable(_activity, R.drawable.icon_2);
                        btnLeft.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
                    } else {
                        btnLeft.setEnabled(false);
                        drawableTop = ContextCompat.getDrawable(_activity, R.drawable.icon_off_2);
                        btnLeft.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
                    }

                    // 해당 값들이 있으면 활성화
                    if (getRightSeq() > 0) {
                        btnRight.setEnabled(true);
                        drawableTop = ContextCompat.getDrawable(_activity, R.drawable.icon_3);
                        btnRight.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
                    } else {
                        btnRight.setEnabled(false);
                        drawableTop = ContextCompat.getDrawable(_activity, R.drawable.icon_off_3);
                        btnRight.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
                    }

                    // 해당 값들이 있으면 활성화
                    if (getDownloadBtnChk() > 0) {
                        btnDownload.setEnabled(true);
                        drawableTop = ContextCompat.getDrawable(_activity, R.drawable.icon_6);
                        btnDownload.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
                    } else {
                        btnDownload.setEnabled(false);
                        drawableTop = ContextCompat.getDrawable(_activity, R.drawable.icon_off_6);
                        btnDownload.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
                    }

                    break;

                // ui 쓰레드가 이미지 다운로드 버튼이 on 경우
                case DownBtnOn:
                    // 이미지 다운로드 버튼은 비활성화
                    btnDownload.setEnabled(false);
                    drawableTop = ContextCompat.getDrawable(_activity, R.drawable.icon_off_6);
                    btnDownload.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);

                    break;

                // ui 쓰레드가 이미지 다운로드 버튼이 off 경우
                case DownBtnOff:
                    // 이미지 다운로드 버튼은 활성화
                    btnDownload.setEnabled(true);
                    drawableTop = ContextCompat.getDrawable(_activity, R.drawable.icon_6);
                    btnDownload.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);

                    break;
            }
        }
    };

    // UI 변경에 사용 될 enum
    public enum UiEnum {
        ImageLeftRight(1),
        DownBtnOn(2),
        DownBtnOff(3);

        private int value;

        private UiEnum(int value) {
            this.value = value;
        }

        static UiEnum fromValue(int value) {
            for (UiEnum item : UiEnum.values()) {
                if (item.value == value) {
                    return item;
                }
            }

            return null;
        }

        int value() {
            return value;
        }
    }
}



