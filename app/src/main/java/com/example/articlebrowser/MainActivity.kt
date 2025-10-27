package com.example.articlebrowser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.*
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlBar: EditText

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        urlBar = findViewById(R.id.urlBar)

        val ws = webView.settings
        ws.javaScriptEnabled = true
        ws.domStorageEnabled = true
        ws.mediaPlaybackRequiresUserGesture = true

        ws.allowFileAccess = false
        ws.allowContentAccess = false

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                // Block entering fullscreen for media by doing nothing
            }
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                urlBar.setText(url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                val reqUrl = request?.url?.toString() ?: return super.shouldInterceptRequest(view, request)

                val blockedExtensions = listOf(".mp4", ".webm", ".m3u8", ".mkv", ".flv", ".mov")
                for (ext in blockedExtensions) {
                    if (reqUrl.contains(ext, ignoreCase = true)) {
                        return WebResourceResponse("text/plain", "UTF-8", null)
                    }
                }

                val blockedDomains = listOf("youtube.com", "youtu.be", "vimeo.com", "dailymotion.com", "cdn.video", "akamaihd.net")
                for (d in blockedDomains) {
                    if (reqUrl.contains(d, ignoreCase = true)) {
                        return WebResourceResponse("text/plain", "UTF-8", null)
                    }
                }

                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                val js = """(function(){
                    try{
                        const makePlaceholder = function(text){
                            const el = document.createElement('div');
                            el.style.background = '#f1f5f9';
                            el.style.color = '#0f172a';
                            el.style.border = '1px solid #cbd5e1';
                            el.style.padding = '12px';
                            el.style.margin = '8px 0';
                            el.style.borderRadius = '6px';
                            el.style.fontFamily = 'sans-serif';
                            el.style.fontSize = '14px';
                            el.innerText = text;
                            return el;
                        };
                        document.querySelectorAll('video, audio, iframe').forEach(function(el){
                            const ph = makePlaceholder('Video playback disabled by Article Browser');
                            el.parentNode.replaceChild(ph, el);
                        });
                        document.querySelectorAll('[class*=video],[id*=video],[data-video]').forEach(function(el){
                            const ph = makePlaceholder('Video playback disabled by Article Browser');
                            el.parentNode.replaceChild(ph, el);
                        });
                    }catch(e){}
                })();""".trimIndent()

                view?.evaluateJavascript(js, null)
            }
        }

        webView.loadUrl("https://en.wikipedia.org/wiki/Main_Page")

        urlBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                var text = urlBar.text.toString().trim()
                if (!text.startsWith("http://") && !text.startsWith("https://")) {
                    text = "https://www.google.com/search?q=${Uri.encode(text)}"
                }
                webView.loadUrl(text)
                true
            } else false
        }

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            if (webView.canGoBack()) webView.goBack()
        }
        findViewById<Button>(R.id.forwardBtn).setOnClickListener {
            if (webView.canGoForward()) webView.goForward()
        }
        findViewById<Button>(R.id.refreshBtn).setOnClickListener {
            webView.reload()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
