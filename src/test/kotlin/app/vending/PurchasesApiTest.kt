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
class PurchasesApiTest {

    @Autowired
    lateinit var env: Environment

    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        val port = env.getRequiredProperty("local.server.port")
        client = RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Test
    fun `500円玉を入れてお茶を買うと、お茶と380円のおつりが出る`() {
        client.post().uri("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"FiveHundredYen\"")
            .exchange()

        client.post().uri("/purchases")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"Tea\"")
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json("""{"product": "Tea", "change": 380}""")
    }

    @Test
    fun `100円玉だけではお茶を買えず、残高不足が返る`() {
        client.post().uri("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"HundredYen\"")
            .exchange()

        client.post().uri("/purchases")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"Tea\"")
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody().json("""{"error": "InsufficientCredit"}""")
    }

    @Test
    fun `お茶を買ったあとに100円玉を入れると、投入金額は100円から始まる`() {
        client.post().uri("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"FiveHundredYen\"")
            .exchange()

        client.post().uri("/purchases")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"Tea\"")
            .exchange()

        client.post().uri("/deposits")
            .contentType(MediaType.APPLICATION_JSON)
            .body("\"HundredYen\"")
            .exchange()
            .expectStatus().isCreated()
            .expectBody().json("""{"credit": 100}""")
    }
}
