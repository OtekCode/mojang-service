package pl.otekplay.mojang.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import java.net.URL


@KtorExperimentalAPI
val client
    get() = HttpClient {
        engine {
            CIO.create {
                endpoint {

                    /**
                     * Maximum number of requests for a specific endpoint route.
                     */
                    maxConnectionsPerRoute = 100

                    /**
                     * Max size of scheduled requests per connection(pipeline queue size).
                     */
                    pipelineMaxSize = 20

                    /**
                     * Max number of milliseconds to keep iddle connection alive.
                     */
                    keepAliveTime = 2000

                    /**
                     * Number of milliseconds to wait trying to connect to the server.
                     */
                    connectTimeout = 500

                    /**
                     * Maximum number of attempts for retrying a connection.
                     */
                    connectRetryAttempts = 1
                }
            }
        }

        install(JsonFeature) {
            serializer = GsonSerializer()
        }

    }
