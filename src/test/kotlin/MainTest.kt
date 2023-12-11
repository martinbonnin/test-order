import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.isFromCache
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.mockserver.enqueue
import com.apollographql.apollo3.network.http.HttpNetworkTransport
import com.example.GetHelloQuery
import kotlinx.coroutines.runBlocking
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals

class MainTest {
    @Test
    fun testStuff() = runBlocking {
        val mockServer = MockServer()
        ApolloClient.Builder()
            .normalizedCache(MemoryCacheFactory())
            .networkTransport(
                HttpNetworkTransport.Builder()
                    .serverUrl(mockServer.url())
                    .build()
            )
            .build()
            .use { apolloClient ->

                @Language("json")
                val json = """
                    {
                      "data": { "hello": "World"}
                    }
                """.trimIndent()
                mockServer.enqueue(json)

                apolloClient.query(GetHelloQuery()).execute().apply {
                    assertEquals("World", data?.hello)
                    assertEquals(false, isFromCache)
                }
                apolloClient.query(GetHelloQuery()).execute().apply {
                    assertEquals("World", data?.hello)
                    assertEquals(true, isFromCache)
                }

            }
        mockServer.stop()
    }
}