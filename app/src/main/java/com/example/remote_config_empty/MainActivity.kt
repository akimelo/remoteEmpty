package com.example.remote_config_empty

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.remote_config_empty.ui.theme.Remote_config_emptyTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import coil.compose.rememberAsyncImagePainter

import io.karte.android.tracking.Tracker
import io.karte.android.variables.Variables

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this.applicationContext
        val imgVal = Variables.get("app_top_krt_image_url")
        val linkVal = Variables.get("app_top_krt_link_url")
        val textVal = Variables.get("app_top_krt_label_text")
        val buttonVal = Variables.get("app_top_krt_button_text")
        val colorVal = Variables.get("app_top_krt_text_color")

        setContent {
            Remote_config_emptyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(Modifier.padding(12.dp)) {
                        BannerWithText(
                            imgVal?.string("foo.img")?:"",
                            textVal?.string("foo.text")?:"",
                            buttonVal?.string("foo.button")?:"",
                            colorVal?.string("foo.color")?:"",
                            Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(8.dp)
                                ),
                            onClick = {
                                // URLを開くためのIntentを発行
                                val data = Uri.parse(linkVal?.string("foo.link")?:"") ?: return@BannerWithText
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    this.data = data
                                    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (_: Throwable) {}
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Tracker.view("Fragment", "フラグメント")
        showToast("onResume()")
    }

    private fun showToast(lifecycle: String) {
        Toast.makeText(applicationContext, "call $lifecycle !", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun BannerWithText(img: String, labeltext: String, buttontext: String, colorcode: String, modifier: Modifier = Modifier, onClick: () -> Unit) {

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Coilライブラリを利用して、リモートの画像URLから画像を読み込み
        Image(
            modifier = Modifier.matchParentSize(),
            painter = rememberAsyncImagePainter(img),
            contentDescription = "Remote Banner Image",
            contentScale = ContentScale.Crop // 画像がBoxいっぱいに表示されるように調整
        )
        Column(horizontalAlignment = Alignment.Start)
        {
            Text(
                text = labeltext,
                style = MaterialTheme.typography.headlineMedium, // スタイルは必要に応じて調整
                color = Color(android.graphics.Color.parseColor(colorcode)) // テキストカラー
            )
            // 下線付きのテキストを追加
            Text(
                text = buttontext,
                style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                color = Color(android.graphics.Color.parseColor(colorcode)) // テキストカラー
            )
        }
    }
}
