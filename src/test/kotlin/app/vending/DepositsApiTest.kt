package app.vending

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DepositsApiTest {

    @Autowired
    lateinit var env: Environment

    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        val port = env.getRequiredProperty("local.server.port")
        client = RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Test
    fun `100円玉を入れると投入金額が100円になる`() {
        client.post().uri("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"HundredYen\"")
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json("""{"credit": 100}""")
    }

    @Test
    fun `100円玉と50円玉を続けて入れると投入金額が150円になる`() {
        client.post().uri("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"HundredYen\"")
            .exchange()

        client.post().uri("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"FiftyYen\"")
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json("""{"credit": 150}""")
    }
}
