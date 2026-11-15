package com.ironforge.pro.ui.components

import android.graphics.RuntimeShader
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.widget.VideoView
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    speed: Float = 1f
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(assetPath))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
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
                val orbX = size.width * 0.5f + cos(angle) * (size.width * 0.45f)
                val orbY = size.height * 0.5f + sin(angle) * (size.height * 0.45f)

                val glowBrush = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val agsl = RuntimeShader("uniform float2 uRes; uniform float uTime; uniform half4 uColA; uniform half4 uColB; half4 main(float2 fragCoord) { float2 uv = fragCoord / max(uRes, float2(1.0)); float2 p = uv - 0.5; float a = atan(p.y, p.x) + uTime; float wave = 0.5 + 0.5 * cos(a * 2.0); return mix(uColA, uColB, half(wave)); }")
                    agsl.setFloatUniform("uRes", size.width, size.height)
                    agsl.setFloatUniform("uTime", angle)
                    agsl.setColorUniform("uColA", palette.primary.toArgb())
                    agsl.setColorUniform("uColB", palette.secondary.toArgb())
                    ShaderBrush(agsl)
                } else {
                    Brush.radialGradient(
                        colors = listOf(palette.primary, palette.secondary, palette.hairline),
                        center = Offset(orbX, orbY),
                        radius = size.maxDimension * 0.65f
                    )
                }

                onDrawWithContent {
                    drawContent()
                    drawRoundRect(
                        brush = glowBrush,
                        topLeft = Offset(strokePx / 2f, strokePx / 2f),
                        size = Size(size.width - strokePx, size.height - strokePx),
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
fun WelcomeCoachVideoCard(
    modifier: Modifier = Modifier,
    height: Dp = 165.dp
) {
    val palette = LocalIronPalette.current
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(true) }
    var mediaPlayerRef by remember { mutableStateOf<MediaPlayer?>(null) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(22.dp),
        color = palette.card,
        border = BorderStroke(1.2.dp, palette.primary.copy(alpha = 0.6f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        val videoUri = Uri.parse("android.resource://${ctx.packageName}/${R.raw.welcome_pt}")
                        setVideoURI(videoUri)
                        setOnPreparedListener { mp ->
                            mediaPlayerRef = mp
                            mp.isLooping = true
                            mp.setVolume(0f, 0f)
                            start()
                        }
                    }
                },
                update = { vv ->
                    if (isPlaying && !vv.isPlaying) vv.start()
                    else if (!isPlaying && vv.isPlaying) vv.pause()
                    val vol = if (isMuted) 0f else 1f
                    try {
                        mediaPlayerRef?.setVolume(vol, vol)
                    } catch (_: Exception) {
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color(0xD9070A10)))
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "IRONFORGE STUDIO COACH",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                            color = palette.primaryGlow,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Treinos guiados com biomecânica 3D",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color(0xCC151A24))
                                .clickable { isMuted = !isMuted },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                contentDescription = "Som",
                                tint = palette.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color(0xCC151A24))
                                .clickable { isPlaying = !isPlaying },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = palette.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
