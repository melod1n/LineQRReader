package com.meloda.lineqrreader.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

object ColorUtils {

    fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 0.75f
        return Color.HSVToColor(hsv)
    }

    @JvmOverloads
    fun lightenColor(color: Int, lightFactor: Float = 1.1f): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= lightFactor
        return Color.HSVToColor(hsv)
    }

    @JvmOverloads
    fun saturateColor(color: Int, factor: Float = 2f): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[1] *= factor
        return Color.HSVToColor(hsv)
    }

    @JvmOverloads
    fun alphaColor(
        color: Int,
        @FloatRange(from = 0.0, to = 1.0)
        alphaFactor: Float = 0.85f
    ): Int {
        val alpha = Color.alpha(color)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb((alpha * alphaFactor).toInt(), red, green, blue)
    }

    private fun compositeAlpha(foregroundAlpha: Int, backgroundAlpha: Int): Int {
        return 0xFF - (0xFF - backgroundAlpha) * (0xFF - foregroundAlpha) / 0xFF
    }

    private fun compositeComponent(fgC: Int, fgA: Int, bgC: Int, bgA: Int, a: Int): Int {
        return if (a == 0) 0 else (0xFF * fgC * fgA + bgC * bgA * (0xFF - fgA)) / (a * 0xFF)
    }

    @FloatRange(from = 0.0, to = 1.0)
    fun calculateLuminance(@ColorInt color: Int): Double {
        var red = Color.red(color) / 255.0
        red = if (red < 0.03928) red / 12.92 else ((red + 0.055) / 1.055).pow(2.4)
        var green = Color.green(color) / 255.0
        green =
            if (green < 0.03928) green / 12.92 else ((green + 0.055) / 1.055).pow(2.4)
        var blue = Color.blue(color) / 255.0
        blue = if (blue < 0.03928) blue / 12.92 else ((blue + 0.055) / 1.055).pow(2.4)
        return 0.2126 * red + 0.7152 * green + 0.0722 * blue
    }

    private fun rgbToHsl(
        @androidx.annotation.IntRange(from = 0x0, to = 0xFF) r: Int,
        @androidx.annotation.IntRange(from = 0x0, to = 0xFF) g: Int,
        @androidx.annotation.IntRange(
            from = 0x0,
            to = 0xFF
        ) b: Int,
        hsl: FloatArray
    ) {
        val rf = r / 255f
        val gf = g / 255f
        val bf = b / 255f
        val max = rf.coerceAtLeast(gf.coerceAtLeast(bf))
        val min = rf.coerceAtMost(gf.coerceAtMost(bf))
        val deltaMaxMin = max - min
        var h: Float
        val s: Float
        val l = (max + min) / 2f
        if (max == min) { // Monochromatic
            s = 0f
            h = s
        } else {
            h = when (max) {
                rf -> {
                    (gf - bf) / deltaMaxMin % 6f
                }
                gf -> {
                    (bf - rf) / deltaMaxMin + 2f
                }
                else -> {
                    (rf - gf) / deltaMaxMin + 4f
                }
            }
            s = deltaMaxMin / (1f - abs(2f * l - 1f))
        }
        h = h * 60f % 360f
        if (h < 0) {
            h += 360f
        }
        hsl[0] = constrain(h, 0f, 360f)
        hsl[1] = constrain(s, 0f, 1f)
        hsl[2] = constrain(l, 0f, 1f)
    }

    fun colorToHSL(@ColorInt color: Int, hsl: FloatArray) {
        rgbToHsl(
            Color.red(color),
            Color.green(color),
            Color.blue(color),
            hsl
        )
    }

    @ColorInt
    fun HSLToColor(hsl: FloatArray): Int {
        val h = hsl[0]
        val s = hsl[1]
        val l = hsl[2]
        val c = (1f - abs(2 * l - 1f)) * s
        val m = l - 0.5f * c
        val x = c * (1f - abs(h / 60f % 2f - 1f))
        val hueSegment = h.toInt() / 60
        var r = 0
        var g = 0
        var b = 0
        when (hueSegment) {
            0 -> {
                r = (255 * (c + m)).roundToInt()
                g = (255 * (x + m)).roundToInt()
                b = (255 * m).roundToInt()
            }
            1 -> {
                r = (255 * (x + m)).roundToInt()
                g = (255 * (c + m)).roundToInt()
                b = (255 * m).roundToInt()
            }
            2 -> {
                r = (255 * m).roundToInt()
                g = (255 * (c + m)).roundToInt()
                b = (255 * (x + m)).roundToInt()
            }
            3 -> {
                r = (255 * m).roundToInt()
                g = (255 * (x + m)).roundToInt()
                b = (255 * (c + m)).roundToInt()
            }
            4 -> {
                r = (255 * (x + m)).roundToInt()
                g = (255 * m).roundToInt()
                b = (255 * (c + m)).roundToInt()
            }
            5, 6 -> {
                r = (255 * (c + m)).roundToInt()
                g = (255 * m).roundToInt()
                b = (255 * (x + m)).roundToInt()
            }
        }
        r = constrain(r, 0, 255)
        g = constrain(g, 0, 255)
        b = constrain(b, 0, 255)
        return Color.rgb(r, g, b)
    }

    @ColorInt
    fun setAlphaComponent(
        @ColorInt color: Int,
        @androidx.annotation.IntRange(from = 0x0, to = 0xFF) alpha: Int
    ): Int {
        require(!(alpha < 0 || alpha > 255)) { "alpha must be between 0 and 255." }
        return color and 0x00ffffff or (alpha shl 24)
    }

    private fun constrain(
        amount: Float,
        low: Float,
        high: Float
    ): Float {
        return if (amount < low) low else if (amount > high) high else amount
    }

    private fun constrain(amount: Int, low: Int, high: Int): Int {
        return if (amount < low) low else if (amount > high) high else amount
    }

    fun isLight(@ColorInt color: Int): Boolean {
        return calculateLuminance(color) >= 0.5
    }

    fun isDark(@ColorInt color: Int): Boolean {
        return calculateLuminance(color) < 0.5
    }

    fun isValidHexColor(color: CharSequence): Boolean {
        return Pattern.matches("^#?([a-f0-9]{6}|[a-f0-9]{3})$", color)
    }

    @ColorInt
    fun getRandomColor(): Int {
        val red = (Math.random() * 256).toInt()
        val green = (Math.random() * 256).toInt()
        val blue = (Math.random() * 256).toInt()
        return Color.rgb(red, green, blue)
    }
}