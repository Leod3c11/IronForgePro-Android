package com.ironforge.pro.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.VideoView
import androidx.compose.foundation.BorderStroke
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

    // In compact list mode, user taps to activate inline YouTube player so 59 WebViews don't load simultaneously
    var inlinePlayerActive by remember(exercise.id, compact) { mutableStateOf(!compact) }
    var useCustomMp4 by remember(exercise.id, customVideoUri) { mutableStateOf(customVideoUri != null) }

    fun openExternalYouTube() {
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoDemo.watchUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
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
                                    cacheMode = WebSettings.LOAD_DEFAULT
                                }
                                webChromeClient = WebChromeClient()
                                webViewClient = WebViewClient()
                                loadUrl(videoDemo.embedUrl)
                            }
                        },
                        update = { wv ->
                            if (wv.url != videoDemo.embedUrl) {
                                wv.loadUrl(videoDemo.embedUrl)
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
                    // Sleek Real-Video Stage Card (no stick-figure / no sticker!)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF0D1320),
                                        Color(0xFF131C2E),
                                        Color(0xFF080B12)
                                    )
                                )
                            )
                            .clickable { inlinePlayerActive = true }
                            .padding(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(palette.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Reproduzir Vídeo",
                                    tint = palette.onPrimaryAccent,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "ASSISTIR EXECUÇÃO REAL",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${videoDemo.instructor} • ${videoDemo.durationLabel}",
                                style = MaterialTheme.typography.labelSmall,
                                color = palette.primaryGlow,
                                fontSize = 11.sp
                            )
                        }
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

                    // Always offer direct YouTube app button for full-screen / guaranteed playback
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
                                text = "YouTube",
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
