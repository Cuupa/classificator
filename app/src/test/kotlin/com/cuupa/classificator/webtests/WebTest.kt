package com.cuupa.classificator.webtests

import com.cuupa.classificator.configuration.ApplicationTestConfiguration
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.concurrent.TimeUnit

@SpringBootTest(
    classes = [ApplicationTestConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableWebSecurity
open class WebTest {

    @LocalServerPort
    private var port = 0

    private val url = "http://localhost"

    private var driver = ChromeDriver()

    @BeforeAll
    fun init() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS)
    }

    @AfterEach
    fun destroy() {
        driver.close()
    }

    //    @Test
    fun shouldOpenBrowser() {
        log.error("Navigate to $url:$port")
        driver.navigate().to("$url:$port")
    }

    //    @Test
    fun shouldSmoketest() {
        driver.navigate().to("$url:$port")
        driver.findElement(By.id("inputText")).sendKeys(smokeText)
        driver.findElement(By.id("submit_text")).click()
    }

    companion object {
        val log: Log = LogFactory.getLog(WebTest::class.java)

        val smokeText = """Sehr geehrte Damen und Herren,
            hiermit kündige ich, Max Mustermann, geboren am 01.01.1999, meinen Vertrag zur Vertrangsnummer 32103847298 zum 31.12.3030.
            Bitte überweisen Sie den verbleibenden Betrag auf mein Konto mit der IBAN DE19123412341234123412.
            Bei Rückfragen stehe ich unter der Tel: +49301234567 zur Verfügung
        """.trimIndent()
    }
}