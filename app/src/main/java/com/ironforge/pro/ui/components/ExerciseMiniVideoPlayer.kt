package com.ironforge.pro.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.VideoView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.ironforge.pro.data.ExerciseVideoCatalog
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.ui.theme.LocalIronPalette
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ExerciseMiniVideoPlayer(
    exercise: Exercise,
    customVideoUri: String? = null,
    height: Dp = 195.dp,
    compact: Boolean = false,
    onAttachVideoClick: (() -> Unit)? = null
) {
    val palette = LocalIronPalette.current
    val context = LocalContext.current
    val videoDemo = remember(exercise.id) { ExerciseVideoCatalog.resolveForExercise(exercise) }

    // Load real YouTube video frame thumbnail asynchronously so there's never a blank box or Error 153
    var thumbBitmap by remember(videoDemo.youtubeVideoId) { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(videoDemo.youtubeVideoId) {
        thumbBitmap = withContext(Dispatchers.IO) {
            runCatching {
                val conn = (URL(videoDemo.thumbnailUrl).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 5000
                    readTimeout = 5000
                    setRequestProperty("User-Agent", "Mozilla/5.0")
                }
                conn.inputStream.use { BitmapFactory.decodeStream(it) }
            }.getOrNull()
        }
    }

    // Start on the real HD video thumbnail card; tapping Play activates the Referer-authenticated player
    var inlinePlayerActive by remember(exercise.id) { mutableStateOf(false) }
    var useCustomMp4 by remember(exercise.id, customVideoUri) { mutableStateOf(customVideoUri != null) }

    fun openExternalYouTube() {
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoDemo.watchUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    val embedHtml = remember(videoDemo.youtubeVideoId) {
        """
        <!DOCTYPE html>
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
          <meta name="referrer" content="strict-origin-when-cross-origin">
          <style>
            * { margin:0; padding:0; box-sizing:border-box; background:#070A10; }
            html, body { width:100%; height:100%; overflow:hidden; }
            iframe { width:100%; height:100%; border:0; }
          </style>
        </head>
        <body>
          <iframe
            src="https://www.youtube.com/embed/${videoDemo.youtubeVideoId}?autoplay=1&playsinline=1&rel=0&modestbranding=1&origin=https://www.youtube.com"
            referrerpolicy="strict-origin-when-cross-origin"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
            allowfullscreen>
          </iframe>
        </body>
        </html>
        """.trimIndent()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF070A10),
        border = BorderStroke(1.2.dp, palette.primary.copy(alpha = 0.45f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                useCustomMp4 && customVideoUri != null -> {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setVideoURI(Uri.parse(customVideoUri))
                                setOnPreparedListener { mp ->
                                    mp.isLooping = true
                                    mp.setVolume(0f, 0f)
                                    start()
                                }
                            }
                        },
                        onRelease = { vv ->
                            runCatching { vv.stopPlayback() }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                inlinePlayerActive -> {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                setBackgroundColor(AndroidColor.parseColor("#070A10"))
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    mediaPlaybackRequiresUserGesture = false
                                    loadWithOverviewMode = true
                                    useWideViewPort = true
                                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    userAgentString =
                                        "Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36"
                                }
                                webChromeClient = WebChromeClient()
                                webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(
                                        view: WebView?,
                                        request: WebResourceRequest?
                                    ): Boolean {
                                        val urlStr = request?.url?.toString() ?: return false
                                        if (urlStr.contains("watch?v=") || urlStr.contains("youtu.be/")) {
                                            openExternalYouTube()
                                            return true
                                        }
                                        return false
                                    }
                                }
                                loadDataWithBaseURL(
                                    "https://www.youtube.com",
                                    embedHtml,
                                    "text/html",
                                    "UTF-8",
                                    "https://www.youtube.com"
                                )
                            }
                        },
                        onRelease = { wv ->
                            runCatching {
                                wv.stopLoading()
                                wv.loadUrl("about:blank")
                                wv.destroy()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    // Real YouTube Thumbnail Preview Stage — zero Error 153!
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF0A0F1A))
                            .clickable { inlinePlayerActive = true }
                    ) {
                        val bmp = thumbBitmap
                        if (bmp != null) {
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = videoDemo.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0x88060910),
                                                Color(0x55060910),
                                                Color(0xCC060910)
                                            )
                                        )
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF0F172A),
                                                Color(0xFF1E293B),
                                                Color(0xFF0B0F17)
                                            )
                                        )
                                    )
                            )
                        }

                        // Center Play Trigger
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF0000)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Reproduzir Vídeo",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xCC0A0E17))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Executar Vídeo • ${videoDemo.durationLabel}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Bottom Video Title Caption
                        Text(
                            text = videoDemo.title,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Top Compact Studio Badge Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(listOf(Color(0xCC060910), Color.Transparent))
                    )
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xE6111622))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.OndemandVideo,
                                contentDescription = null,
                                tint = if (useCustomMp4) palette.secondary else Color(0xFFFF3D3D),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (useCustomMp4) "VÍDEO LOCAL" else "YOUTUBE • ${videoDemo.instructor.uppercase()}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = Color.White,
                                fontSize = 9.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (customVideoUri != null) {
                        VideoHudActionPill(
                            text = if (useCustomMp4) "YouTube" else "Meu MP4",
                            accent = palette.secondary,
                            onClick = { useCustomMp4 = !useCustomMp4 }
                        )
                    } else if (onAttachVideoClick != null && !compact) {
                        VideoHudActionPill(
                            text = "+ MP4",
                            accent = palette.secondary,
                            onClick = onAttachVideoClick
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xE6111622))
                            .clickable { openExternalYouTube() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "Abrir no YouTube",
                                tint = palette.primaryGlow,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "App YT",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = palette.primaryGlow,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoHudActionPill(
    text: String,
    accent: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xE6111622))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.VideoLibrary,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = accent,
                fontSize = 10.sp
            )
        }
    }
}
