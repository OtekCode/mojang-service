import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import org.junit.Test
import pl.otekplay.mojang.main
import java.util.concurrent.atomic.AtomicInteger

class HasPaidTest {
    @Test
    fun testRequestPremium() =
        assertEquals("true", testRequest("Otekplay"))

    @Test
    fun testRequestCracked() = assertEquals("false", testRequest("ochujjajebie"))

    fun testRequest(name: String): String? = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/api/mojang/haspaid/$name")) { response.content }
    }



    @Test
    fun test10Request() = testMultiRequests(10)

    @Test
    fun test100Request() = testMultiRequests(100)

    @Test
    fun test1000Request() = testMultiRequests(1000)

    @Test
    fun test10000Request() = testMultiRequests(10000)



    fun testMultiRequests(amount: Int) = withTestApplication(Application::main) {
         val compute = newFixedThreadPoolContext(amount, "FixedThreads")
        val ids = AtomicInteger(0)

        runBlocking {
            val all = IntRange(1, amount).map {
                async(compute) {
                    val req = handleRequest(HttpMethod.Get, "/api/mojang/haspaid/test$it")
                    ids.incrementAndGet()
                }
            }.awaitAll()
        }
        assertEquals(ids.get(),amount)
    }
}