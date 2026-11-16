package com.ironforge.pro.ui.components

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
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

    var useCustomMp4 by remember(exercise.id, customVideoUri) { mutableStateOf(customVideoUri != null) }

    // Fetch real YouTube HQ thumbnail with multi-resolution fallback (hqdefault -> mqdefault)
    var thumbBitmap by remember(videoDemo.youtubeVideoId) { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(videoDemo.youtubeVideoId) {
        thumbBitmap = withContext(Dispatchers.IO) {
            val urls = listOf(
                "https://i.ytimg.com/vi/${videoDemo.youtubeVideoId}/hqdefault.jpg",
                "https://i.ytimg.com/vi/${videoDemo.youtubeVideoId}/mqdefault.jpg"
            )
            for (u in urls) {
                val bmp = runCatching {
                    val conn = (URL(u).openConnection() as HttpURLConnection).apply {
                        connectTimeout = 6000
                        readTimeout = 6000
                        setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 14)")
                    }
                    conn.inputStream.use { BitmapFactory.decodeStream(it) }
                }.getOrNull()
                if (bmp != null && bmp.width > 120) return@withContext bmp
            }
            null
        }
    }

    // Subtle breathing glow on the Play button so the user immediately sees it's interactive
    val pulseTransition = rememberInfiniteTransition(label = "ytPlayPulse")
    val playScale by pulseTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "playScale"
    )

    fun launchYouTubeExecutionVideo() {
        // Try native YouTube app deep link first for instant zero-buffering playback, fallback to HTTPS browser
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${videoDemo.youtubeVideoId}")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoDemo.watchUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching {
            context.startActivity(appIntent)
        }.onFailure {
            runCatching { context.startActivity(webIntent) }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF0E1420),
        border = BorderStroke(1.2.dp, palette.primary.copy(alpha = 0.45f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (useCustomMp4 && customVideoUri != null) {
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
            } else {
                // Always-visible Cinema Video Stage — NEVER renders a black box!
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { launchYouTubeExecutionVideo() }
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
                                            Color(0x99080C14),
                                            Color(0x44080C14),
                                            Color(0xDD080C14)
                                        )
                                    )
                                )
                        )
                    } else {
                        // Rich Studio Backdrop with Biomechanical Grid & Glow (never pitch black!)
                        val primaryColor = palette.primary
                        val secondaryColor = palette.secondary
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFF10192B),
                                            Color(0xFF19263D),
                                            Color(0xFF0D1422)
                                        )
                                    )
                                )
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val step = 28.dp.toPx()
                                var x = 0f
                                while (x < size.width) {
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.04f),
                                        start = Offset(x, 0f),
                                        end = Offset(x, size.height),
                                        strokeWidth = 1f
                                    )
                                    x += step
                                }
                                var y = 0f
                                while (y < size.height) {
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.04f),
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = 1f
                                    )
                                    y += step
                                }
                                drawCircle(
                                    color = primaryColor.copy(alpha = 0.14f),
                                    radius = size.minDimension * 0.42f,
                                    center = center
                                )
                                drawCircle(
                                    color = secondaryColor.copy(alpha = 0.25f),
                                    radius = size.minDimension * 0.26f,
                                    center = center,
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                        }
                    }

                    // Center Interactive Play Button
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = playScale
                                    scaleY = playScale
                                }
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF0000)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Assistir Execução no YouTube",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xD90A0E17))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Assistir Execução (${videoDemo.durationLabel})",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Bottom Video Title & Muscle Cue
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = videoDemo.title,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.White.copy(alpha = 0.95f),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "HD • ${exercise.equipment.label}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = palette.primaryGlow,
                            fontSize = 10.sp
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
                            .clickable { launchYouTubeExecutionVideo() }
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
                                text = "Abrir YT",
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
