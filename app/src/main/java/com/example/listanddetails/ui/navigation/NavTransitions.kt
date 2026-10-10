package com.example.listanddetails.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene

object NavTransitions {
    private const val DURATION_MS = 340
    private val Easing = FastOutSlowInEasing

    // Переход вперед: Новый экран приближается, старый утапливается
    val pushTransition: AnimatedContentTransitionScope<Scene<Route>>.() -> ContentTransform = {
        (scaleIn(
            initialScale = 0.88f,
            animationSpec = tween(DURATION_MS, easing = Easing),
        ) + fadeIn(animationSpec = tween(DURATION_MS, easing = Easing)))
            .togetherWith(
                scaleOut(
                    targetScale = 0.94f,
                    animationSpec = tween(DURATION_MS, easing = Easing),
                ) + fadeOut(animationSpec = tween(DURATION_MS / 2, easing = Easing))
            )
    }

    // Переход назад: Текущий экран уменьшается и растворяется, предыдущий возвращается
    val popTransition: AnimatedContentTransitionScope<Scene<Route>>.() -> ContentTransform = {
        (scaleIn(
            initialScale = 0.94f,
            animationSpec = tween(DURATION_MS, easing = Easing)
        ) + fadeIn(animationSpec = tween(DURATION_MS, easing = Easing)))
            .togetherWith(
                scaleOut(
                    targetScale = 0.88f,
                    animationSpec = tween(DURATION_MS, easing = Easing)
                ) + fadeOut(animationSpec = tween(DURATION_MS / 2, easing = Easing))
            )
    }
}
