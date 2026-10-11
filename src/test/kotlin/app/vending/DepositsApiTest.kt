package app.vending

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    fun `120円入金すると投入金額が120円になる`() {
        client.post().uri("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .body("""{"amount": 120}""")
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json("""{"credit": 120}""")
    }
}
