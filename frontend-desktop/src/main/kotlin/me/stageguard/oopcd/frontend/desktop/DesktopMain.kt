package me.stageguard.oopcd.frontend.desktop;

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skija.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() = Window(
    title = "Roll System",
    centered = true,
    resizable = false,
    //undecorated = true
) {
    val count = remember { mutableStateOf(0) }
    MaterialTheme {
        val bitmap = remember { bitmapFromByteArray() }
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawImageRect(
                    Image.makeFromBitmap(bitmap),
                    IRect(0, 0, bitmap.getWidth(), bitmap.getHeight()).toRect()
                )
            }
        }
    }
}

fun bitmapFromByteArray(): Bitmap {
    var image: BufferedImage? = null
    try {
        image = ImageIO.read(File("1.jpg"))
    } catch (e: Exception) {
        // image file does not exist
    }

    if (image == null) {
        image = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    }
    val pixels = getBytes(image) // assume we only have raw pixels

    // allocate and fill skija Bitmap
    val bitmap = Bitmap()
    bitmap.allocPixels(ImageInfo.makeS32(image.width, image.height, ColorAlphaType.PREMUL))
    bitmap.installPixels(bitmap.getImageInfo(), pixels, (image.width * 4).toLong())

    return bitmap
}

// creating byte array from BufferedImage
private fun getBytes(image: BufferedImage): ByteArray {
    val width = image.width
    val height = image.height

    val buffer = IntArray(width * height)
    image.getRGB(0, 0, width, height, buffer, 0, width)

    val pixels = ByteArray(width * height * 4)

    var index = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val pixel = buffer[y * width + x]
            pixels[index++] = ((pixel and 0xFF)).toByte() // Blue component
            pixels[index++] = (((pixel shr 8) and 0xFF)).toByte() // Green component
            pixels[index++] = (((pixel shr 16) and 0xFF)).toByte() // Red component
            pixels[index++] = (((pixel shr 24) and 0xFF)).toByte() // Alpha component
        }
    }

    return pixels
}