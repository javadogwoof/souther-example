package app.vending

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PingApiTest {

    @Autowired
    lateinit var env: Environment

    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        val port = env.getRequiredProperty("local.server.port")
        client = RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Test
    fun `pingを叩くとpongが返る`() {
        client.get().uri("/ping").exchange()
            .expectStatus().isOk()
            .expectBody(String::class.java).isEqualTo("pong")
    }
}
