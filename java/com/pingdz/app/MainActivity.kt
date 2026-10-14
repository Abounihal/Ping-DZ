package com.pingdz.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import java.net.InetAddress
import java.util.concurrent.Executors

class MainActivity : Activity() {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 18, 16)
        window.navigationBarColor = Color.BLACK

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 32, 28, 32)
            setBackgroundColor(Color.rgb(12, 18, 16))
        }
        fun label(text: String, size: Float, color: Int): TextView = TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            setPadding(8, 16, 8, 16)
        }
        layout.addView(label("PING DZ", 32f, Color.rgb(70, 230, 130)))
        layout.addView(label("مساعد ألعاب PUBG Mobile", 20f, Color.WHITE))
        result = label("اضغط لبدء اختبار الاتصال", 18f, Color.LTGRAY)
        layout.addView(result)

        val pingButton = Button(this).apply {
            text = "اختبار Ping"
            setOnClickListener { testPing() }
        }
        layout.addView(pingButton, LinearLayout.LayoutParams(-1, -2))

        val gameButton = Button(this).apply {
            text = "فتح PUBG Mobile"
            setOnClickListener {
                val intent = packageManager.getLaunchIntentForPackage("com.tencent.ig")
                if (intent != null) startActivity(intent)
                else Toast.makeText(this@MainActivity, "لم يتم العثور على PUBG Mobile Global", Toast.LENGTH_LONG).show()
            }
        }
        layout.addView(gameButton, LinearLayout.LayoutParams(-1, -2))
        layout.addView(label("نصيحة: استخدم شبكة مستقرة وأوقف التنزيلات أثناء اللعب.", 14f, Color.LTGRAY))
        setContentView(layout)
    }

    private fun testPing() {
        result.text = "جارٍ اختبار الاتصال..."
        executor.execute {
            val start = System.currentTimeMillis()
            val message = try {
                val address = InetAddress.getByName("8.8.8.8")
                if (address.isReachable(2500)) "زمن الاستجابة: ${System.currentTimeMillis() - start} ms" else "لم يصل رد؛ قد تمنع الشبكة اختبار ICMP"
            } catch (_: Exception) { "تعذر الاختبار؛ تحقق من الإنترنت" }
            handler.post { result.text = message }
        }
    }

    override fun onDestroy() {
        executor.shutdownNow()
        super.onDestroy()
    }
}
