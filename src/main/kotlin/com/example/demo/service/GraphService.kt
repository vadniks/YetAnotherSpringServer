package com.example.demo.service

import net.datafaker.Faker
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.chart.JFreeChart
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO
import kotlin.math.absoluteValue

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
class GraphService {
    private val fixtures = ArrayList<Fixture>(50)

    init {
        val faker = Faker()
        for (i in 0..50) fixtures.add(Fixture(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.country().name(),
            faker.address().fullAddress(),
            faker.date().birthday().time.toInt().absoluteValue
        ))
    }

    private fun JFreeChart.encodeToPngAndBase64(): String {
        val outPlain = ByteArrayOutputStream()
        ChartUtils.writeChartAsPNG(outPlain, this, 1024, 768)

        val outAltered = ByteArrayOutputStream()
        outPlain.toByteArray().inputStream() watermark outAltered
        outPlain.close()

        return Base64.getEncoder().encodeToString(outAltered.toByteArray()).also { outAltered.close() }
    }

    private val String.htmlImage get() = "data:image/png;base64,$this"

    private infix fun ByteArrayInputStream.watermark(out: ByteArrayOutputStream) {
        val image = ImageIO.read(this)
        close()

        val text = "Gimme gimme gimme the charts after processing!"

        val g2d = (image.graphics as Graphics2D).apply {
            composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f)
            color = Color.BLACK
            font = Font("Arial", Font.ITALIC, 32)

            drawString(
                text,
                (image.width - fontMetrics.getStringBounds(text, this).width.toInt()) / 2,
                image.height / 2
            )
        }

        ImageIO.write(image, "png", out)
        g2d.dispose()
    }

    operator fun get(number: Int) = when (number) {
        1 -> ChartFactory.createBarChart(
            "Graph 1",
            "Fixture",
            "Value length",
            defaultDataSet
        )
        2 -> ChartFactory.createLineChart(
            "Graph 2",
            "Fixture",
            "Value length",
            defaultDataSet
        )
        3 -> ChartFactory.createScatterPlot(
            "Graph 3",
            "Fixture",
            "Value length",
            XYSeriesCollection().apply { for ((j, i) in fixtures.withIndex())
                addSeries(XYSeries(j.toString()).apply {
                    add(1, i.firstName.length)
                    add(2, i.lastName.length)
                    add(3, i.country.length)
                    add(4, i.address.length)
                    add(5, i.dateOfBirth.digits)
                })
            }
        )
        else -> throw IllegalArgumentException()
    }.encodeToPngAndBase64().htmlImage

    private val defaultDataSet = DefaultCategoryDataset().apply {
        for ((j, i) in fixtures.withIndex()) {
            val number = j.toString()
            addValue(i.firstName.length.toDouble(), "First name", number)
            addValue(i.lastName.length.toDouble(), "Last name", number)
            addValue(i.country.length.toDouble(), "Country", number)
            addValue(i.address.length.toDouble(), "Address", number)
            addValue(i.dateOfBirth.digits.toDouble(), "Date of birth", number)
        }
    }

    private data class Fixture(
        val firstName: String,
        val lastName: String,
        val country: String,
        val address: String,
        val dateOfBirth: Int
    )

    private val Int.digits: Int get() {
        var count = 0
        var num = this

        while (num != 0) {
            num /= 10
            ++count
        }
        return count
    }
}
