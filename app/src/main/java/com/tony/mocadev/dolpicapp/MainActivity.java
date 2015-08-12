package com.tony.mocadev.dolpicapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.app.ProgressDialog;

public class MainActivity extends Activity {

    private WebView webViewMain;
    private Button btnNewUser, btnHome, btnLeft, btnRight, btnList, btnDownload;
    private View.OnClickListener cListener;
    private ProgressDialog mProgressDialog;

    // 웹뷰 통신 클래스
    private WebViewInterface _WebViewInterface;

    private final String CON_DOLPIC_URL = "http://m.dolpic.kr";
    private final String CON_DOLPIC_MAIN_URL = "/App/Main";
    private final String CON_NEWUSER_URL = "/App/LogIn";
    private final String CON_INITISAL_LIST_URL = "/App/InitialList";

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        if (webViewMain.canGoBack()) {
            webViewMain.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 처음 실행시 다이얼로그 보여주기
        ShowProgressDialog task = new ShowProgressDialog("돌픽 실행중");
        task.execute();

        webViewMain = (WebView) findViewById(R.id.webViewMain);
        webViewMain.setHorizontalScrollBarEnabled(true);

        btnNewUser = (Button) findViewById(R.id.btnNewUser);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnList = (Button) findViewById(R.id.btnList);
        btnDownload = (Button) findViewById(R.id.btnDownload);

        WebSettings webSettings = webViewMain.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        //JavascriptInterface 객체화
        _WebViewInterface = new WebViewInterface(MainActivity.this, webViewMain);
        //웹뷰에 JavascriptInterface를 연결
        webViewMain.addJavascriptInterface(_WebViewInterface, "dolpic");

        webViewMain.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

            }

        });
        webViewMain.setWebChromeClient(new WebChromeClient() {

        });

        // 첫화면 로딩
        webViewMain.loadUrl(String.format("%s%s", CON_DOLPIC_URL, CON_DOLPIC_MAIN_URL));

        // 버튼 이벤트 연결
        cListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.btnNewUser:
                        btnNewUserClicked();
                        break;
                    case R.id.btnHome:
                        btnHomeClicked();
                        break;
                    default:
                        break;
                }
            }
        };

        btnNewUser.setOnClickListener(cListener);
        btnHome.setOnClickListener(cListener);

        btnHome.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                btnImgChange(btnHome, R.drawable.icon_on_1);  //버튼이 선택되었을때 이미지를 교체
                                break;
                            case MotionEvent.ACTION_UP:
                                btnImgChange(btnHome, R.drawable.icon_1);  //버튼에서 손을 떼었을때 이미지를 복구
                                break;
                        }
                        return false;
                    }
                }
        );

        btnLeft.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                btnImgChange(btnLeft, R.drawable.icon_on_2);  //버튼이 선택되었을때 이미지를 교체
                                break;
                            case MotionEvent.ACTION_UP:
                                btnImgChange(btnLeft, R.drawable.icon_2);  //버튼에서 손을 떼었을때 이미지를 복구
                                break;
                        }
                        return false;
                    }
                }
        );

        btnRight.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                btnImgChange(btnRight, R.drawable.icon_on_3);  //버튼이 선택되었을때 이미지를 교체
                                break;
                            case MotionEvent.ACTION_UP:
                                btnImgChange(btnRight, R.drawable.icon_3);  //버튼에서 손을 떼었을때 이미지를 복구
                                break;
                        }
                        return false;
                    }
                }
        );

        btnList.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                btnImgChange(btnList, R.drawable.icon_on_4);  //버튼이 선택되었을때 이미지를 교체
                                break;
                            case MotionEvent.ACTION_UP:
                                btnImgChange(btnList, R.drawable.icon_4);  //버튼에서 손을 떼었을때 이미지를 복구
                                break;
                        }
                        return false;
                    }
                }
        );

        btnNewUser.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                btnImgChange(btnNewUser, R.drawable.icon_on_5);  //버튼이 선택되었을때 이미지를 교체
                                break;
                            case MotionEvent.ACTION_UP:
                                btnImgChange(btnNewUser, R.drawable.icon_5);  //버튼에서 손을 떼었을때 이미지를 복구
                                break;
                        }
                        return false;
                    }
                }
        );

        btnDownload.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                btnImgChange(btnDownload, R.drawable.icon_on_6);  //버튼이 선택되었을때 이미지를 교체
                                break;
                            case MotionEvent.ACTION_UP:
                                btnImgChange(btnDownload, R.drawable.icon_6);  //버튼에서 손을 떼었을때 이미지를 복구
                                break;
                        }
                        return false;
                    }
                }
        );
    }

    /**
     * 버튼 이미지 변경.
     * @param btn : 변경할 버튼
     * @param icon_on_1 : 변경할 이미지
     */
    private void btnImgChange(Button btn, int icon_on_1) {
        Drawable drawableTop = null;

        drawableTop = ContextCompat.getDrawable(getApplicationContext(), icon_on_1);
        btn.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
    }

    /**
     * 회원가입 버튼 클릭 이벤트
     */
    void btnNewUserClicked() {
        webViewMain.loadUrl(String.format("%s%s", CON_DOLPIC_URL, CON_NEWUSER_URL));
    }

    /**
     * 홈 버튼 클릭 이벤트
     */
    void btnHomeClicked() {
        webViewMain.loadUrl(String.format("%s%s", CON_DOLPIC_URL, CON_DOLPIC_MAIN_URL));
    }

    /**
     * 아이돌 리스트 버튼 클릭 이벤트
     */
    public void onBtnListClicked(View v) {
        webViewMain.loadUrl(String.format("%s%s", CON_DOLPIC_URL, CON_INITISAL_LIST_URL));
    }

    /**
     * 이미지 다운로드 버튼 클릭 이벤트
     */
    public void onBtnDownloadClicked(View v) {
        ShowProgressDialog task = new ShowProgressDialog("이미지 다운로드 중");
        task.execute();
        webViewMain.loadUrl("javascript:fnImageDownload()");
    }

    /**
     * 이전 이미지 보기 버튼 클릭 이벤트
     */
    public void onBtnLeftClicked(View v) {
        webViewMain.loadUrl("javascript:fnLeftView()");
    }

    /**
     * 다음 이미지 보기 버튼 클릭 이벤트
     */
    public void onBtnRightClicked(View v) {
        webViewMain.loadUrl("javascript:fnRightView()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 프로그레스 다이얼로그 보여주기
     */
    private class ShowProgressDialog extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                MainActivity.this);

        public String getMsg() {
            return _msg;
        }

        public void setMsg(String _msg) {
            this._msg = _msg;
        }

        String _msg;

        public ShowProgressDialog(String msg) {
            super();
            setMsg(msg);
        }

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage(getMsg());

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
//                for (int i = 0; i < 5; i++) {
//                    //asyncDialog.setProgress(i * 30);
//                    Thread.sleep(500);
//                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

//    public void timeThread(String msg) {
//
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setMessage(msg);
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.setCancelable(true);
//        mProgressDialog.show();
//        new Thread(new Runnable() {
//            public void run() {
//                // TODO Auto-generated method stub
//                try {
//                    Thread.sleep(3000);
//                } catch (Throwable ex) {
//                    ex.printStackTrace();
//                }
//                mProgressDialog.dismiss();
//            }
//        }).start();
//    }
}
