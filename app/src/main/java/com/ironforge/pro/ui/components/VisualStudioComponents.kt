package com.ironforge.pro.ui.components

import android.graphics.ImageDecoder
import android.graphics.RuntimeShader
import android.graphics.drawable.AnimatedImageDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.VideoView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.compose.*
import com.ironforge.pro.R
import com.ironforge.pro.model.Exercise
import com.ironforge.pro.model.FitnessGoal
import com.ironforge.pro.model.MotionPattern
import com.ironforge.pro.ui.theme.LocalIronPalette
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LottieStudioAnimation(
    assetPath: String,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f,
    fallbackTint: Color = LocalIronPalette.current.primary
) {
    val compositionResult = rememberLottieComposition(LottieCompositionSpec.Asset(assetPath))
    val composition = compositionResult.value
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = composition != null,
        iterations = iterations,
        speed = speed
    )
    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier
        )
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = fallbackTint,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * Renders animated GIFs from assets/ (e.g., gifs/plan_review_confetti.gif)
 * using Android 9+ AnimatedImageDrawable with graceful Lottie fallback on older APIs.
 */
@Composable
fun AnimatedGifView(
    assetPath: String,
    modifier: Modifier = Modifier,
    fallbackLottieAsset: String = "lotties/confetti.json"
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    runCatching {
                        val source = ImageDecoder.createSource(ctx.assets, assetPath)
                        val drawable = ImageDecoder.decodeDrawable(source)
                        setImageDrawable(drawable)
                        (drawable as? AnimatedImageDrawable)?.start()
                    }
                }
            },
            onRelease = { iv ->
                runCatching {
                    (iv.drawable as? AnimatedImageDrawable)?.stop()
                    iv.setImageDrawable(null)
                }
            },
            modifier = modifier
        )
    } else {
        LottieStudioAnimation(
            assetPath = fallbackLottieAsset,
            modifier = modifier
        )
    }
}

@DrawableRes
fun resolveExerciseThumbRes(exercise: Exercise): Int {
    return when (exercise.motionPattern) {
        MotionPattern.BENCH_PRESS, MotionPattern.INCLINE_PRESS -> R.drawable.thumb_bench_press
        MotionPattern.FLY_CROSSOVER, MotionPattern.REVERSE_FLY -> R.drawable.thumb_peck_deck
        MotionPattern.TRICEP_PUSHDOWN, MotionPattern.OVERHEAD_TRICEP, MotionPattern.SKULLCRUSHER, MotionPattern.DIPS -> R.drawable.thumb_triceps_rope
        MotionPattern.LEG_CURL, MotionPattern.RDL_HINGE, MotionPattern.DEADLIFT, MotionPattern.HIP_THRUST -> R.drawable.thumb_leg_curl
        MotionPattern.SQUAT, MotionPattern.LUNGE_SPLIT, MotionPattern.LEG_PRESS, MotionPattern.LEG_EXTENSION, MotionPattern.CALF_RAISE -> R.drawable.thumb_squat_pro
        MotionPattern.LAT_PULLDOWN, MotionPattern.PULL_UP, MotionPattern.ROW_HORIZONTAL, MotionPattern.STRAIGHT_ARM_PULL -> R.drawable.thumb_pull_pro
        MotionPattern.OVERHEAD_PRESS, MotionPattern.LATERAL_RAISE -> R.drawable.thumb_shoulder_pro
        MotionPattern.BICEP_CURL, MotionPattern.PREACHER_CURL -> R.drawable.thumb_bicep_pro
        MotionPattern.CRUNCH_FLEXION, MotionPattern.LEG_RAISE, MotionPattern.AB_WHEEL -> R.drawable.thumb_core_pro
    }
}

@DrawableRes
fun resolveGoalIllustrationRes(goal: FitnessGoal, isMale: Boolean): Int {
    return when (goal) {
        FitnessGoal.LEAN_BULK -> if (isMale) R.drawable.visual_goal_bulk_male else R.drawable.visual_goal_bulk_female
        FitnessGoal.CUTTING_MODERATE -> if (isMale) R.drawable.visual_goal_cut_male else R.drawable.visual_goal_cut_female
        FitnessGoal.STRENGTH -> if (isMale) R.drawable.visual_goal_strength_male else R.drawable.visual_goal_strength_female
        FitnessGoal.RECOMP -> if (isMale) R.drawable.visual_goal_recomp_male else R.drawable.visual_goal_recomp_female
    }
}

@Composable
fun ExerciseVisualThumb(
    exercise: Exercise,
    size: Dp = 52.dp,
    modifier: Modifier = Modifier
) {
    val palette = LocalIronPalette.current
    val resId = remember(exercise.id) { resolveExerciseThumbRes(exercise) }
    Surface(
        modifier = modifier.size(size),
        shape = RoundedCornerShape(14.dp),
        color = palette.tileElevated,
        border = BorderStroke(1.dp, palette.hairline)
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = exercise.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun OrbitingGlowBanner(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val palette = LocalIronPalette.current
    val infinite = rememberInfiniteTransition(label = "orbitingGlow")
    val angle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitAngle"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(palette.card)
            .drawWithCache {
                val strokePx = 2.dp.toPx()
                val rPx = cornerRadius.toPx()
                val w = size.width.coerceAtLeast(1f)
                val h = size.height.coerceAtLeast(1f)
                val orbX = w * 0.5f + cos(angle) * (w * 0.45f)
                val orbY = h * 0.5f + sin(angle) * (h * 0.45f)

                val fallbackBrush = Brush.radialGradient(
                    colors = listOf(palette.primary, palette.secondary, palette.hairline),
                    center = Offset(orbX, orbY),
                    radius = maxOf(w, h) * 0.65f
                )

                val glowBrush = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && size.width > 1f && size.height > 1f) {
                    runCatching {
                        val agsl = RuntimeShader("uniform float2 uRes; uniform float uTime; layout(color) uniform half4 uColA; layout(color) uniform half4 uColB; half4 main(float2 fragCoord) { float2 uv = fragCoord / max(uRes, float2(1.0)); float2 p = uv - 0.5; float a = atan(p.y, p.x) + uTime; float wave = 0.5 + 0.5 * cos(a * 2.0); return mix(uColA, uColB, half(wave)); }")
                        agsl.setFloatUniform("uRes", w, h)
                        agsl.setFloatUniform("uTime", angle)
                        agsl.setColorUniform("uColA", palette.primary.toArgb())
                        agsl.setColorUniform("uColB", palette.secondary.toArgb())
                        ShaderBrush(agsl)
                    }.getOrElse { fallbackBrush }
                } else {
                    fallbackBrush
                }

                onDrawWithContent {
                    drawContent()
                    drawRoundRect(
                        brush = glowBrush,
                        topLeft = Offset(strokePx / 2f, strokePx / 2f),
                        size = Size((size.width - strokePx).coerceAtLeast(0f), (size.height - strokePx).coerceAtLeast(0f)),
                        cornerRadius = CornerRadius(rPx, rPx),
                        style = Stroke(width = strokePx)
                    )
                }
            }
    ) {
        content()
    }
}

@Composable
fun EmbeddedStudioVideoCard(
    @RawRes rawResId: Int,
    badge: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    height: Dp = 165.dp,
    initialPlaying: Boolean = true
) {
    val palette = LocalIronPalette.current
    var isPlaying by remember(rawResId) { mutableStateOf(initialPlaying) }
    var isMuted by remember(rawResId) { mutableStateOf(true) }
    var isPrepared by remember(rawResId) { mutableStateOf(false) }
    var mediaPlayerRef by remember(rawResId) { mutableStateOf<MediaPlayer?>(null) }

    Surface(
        modifier = modifier.height(height),
        shape = RoundedCornerShape(22.dp),
        color = palette.card,
        border = BorderStroke(1.2.dp, palette.primary.copy(alpha = 0.55f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        val videoUri = Uri.parse("android.resource://${ctx.packageName}/$rawResId")
                        setVideoURI(videoUri)
                        setOnPreparedListener { mp ->
                            mediaPlayerRef = mp
                            isPrepared = true
                            mp.isLooping = true
                            mp.setVolume(if (isMuted) 0f else 1f, if (isMuted) 0f else 1f)
                            if (isPlaying) start()
                        }
                        setOnErrorListener { _, _, _ ->
                            isPrepared = false
                            true
                        }
                    }
                },
                update = { vv ->
                    if (isPrepared) {
                        runCatching {
                            if (isPlaying && !vv.isPlaying) vv.start()
                            else if (!isPlaying && vv.isPlaying) vv.pause()
                            val vol = if (isMuted) 0f else 1f
                            mediaPlayerRef?.setVolume(vol, vol)
                        }
                    }
                },
                onRelease = { vv ->
                    isPrepared = false
                    mediaPlayerRef = null
                    runCatching { vv.stopPlayback() }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Top LIVE STUDIO pill
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xCC0A0E17))
                    .padding(horizontal = 9.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) palette.primary else palette.textMuted)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                        color = Color.White,
                        fontSize = 9.sp
                    )
                }
            }

            // Bottom gradient scrim + controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6060910)))
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black),
                            color = palette.primaryGlow,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color(0xD9151A24))
                                .clickable { isMuted = !isMuted },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                contentDescription = "Som",
                                tint = palette.secondary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color(0xD9151A24))
                                .clickable { isPlaying = !isPlaying },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = palette.primary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeCoachVideoCard(
    modifier: Modifier = Modifier,
    height: Dp = 165.dp
) {
    EmbeddedStudioVideoCard(
        rawResId = R.raw.welcome_pt,
        badge = "STUDIO COACH",
        title = "IRONFORGE STUDIO COACH",
        subtitle = "Treinos guiados com biomecânica 3D",
        modifier = modifier.fillMaxWidth(),
        height = height,
        initialPlaying = true
    )
}
