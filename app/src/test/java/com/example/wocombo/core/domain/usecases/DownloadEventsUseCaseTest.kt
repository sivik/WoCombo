package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.core.data.exceptions.DownloadEventsException
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.EventFailures
import com.example.wocombo.core.domain.models.Event
import com.example.wocombo.core.domain.repositories.EventsRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import java.util.stream.Stream
import javax.net.ssl.SSLHandshakeException


internal class DownloadEventsUseCaseTest {

    private lateinit var repository: EventsRepository
    private lateinit var useCase: DownloadEventsUseCase

    private companion object {
        @JvmStatic
        fun arguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                SSLHandshakeException(""),
                DownloadEventsUseCase.Result(failure = CommunicationsFailures.InternalServerFailure)
            ),
            Arguments.of(
                TimeoutException(),
                DownloadEventsUseCase.Result(failure = CommunicationsFailures.ConnectionFailure)
            ),
            Arguments.of(
                UnknownHostException(),
                DownloadEventsUseCase.Result(failure = CommunicationsFailures.NoInternetFailure)
            ),
            Arguments.of(
                ConnectException(),
                DownloadEventsUseCase.Result(failure = CommunicationsFailures.NoInternetFailure)
            ),
            Arguments.of(
                InterruptedIOException(),
                DownloadEventsUseCase.Result(failure = CommunicationsFailures.ConnectionFailure)
            ),
            Arguments.of(
                DownloadEventsException("Cannot download"),
                DownloadEventsUseCase.Result(failure = EventFailures.DownloadEventsFailure)
            ),
            Arguments.of(
                Exception(),
                DownloadEventsUseCase.Result(failure = Failure.UnknownFailure)
            )
        )
    }

    @BeforeEach
    fun init() {
        repository = mockk()
        useCase = DownloadEventsUseCase(repository)
    }

    @ParameterizedTest
    @MethodSource("arguments")
    fun `Check failure for download events`(
        exception: Exception,
        expected: DownloadEventsUseCase.Result
    ) {
        //ARRANGE
        val fakeRequest = DownloadEventsUseCase.Request()

        every {
            repository.downloadEvents()
        } throws exception

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.downloadEvents()
        }

        //ASSERT
        confirmVerified(repository)
        assertEquals(expected, result)
    }

    @Test
    fun `Download events successfully`() {
        //ARRANGE
        val fakeRequest = DownloadEventsUseCase.Request()

        val fakeEvents = arrayListOf(
            Event(
                id = "1",
                date = DateTime(2020, 2, 22, 2, 2, 2),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310798917361_image-header_pDach_1554667420000.jpeg",
                subtitle = "Soccer",
                title = "Liverpool vs AS Monaco",
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/promo.mp4?alt=media"
            )
        )

        every { repository.downloadEvents() } returns fakeEvents

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.downloadEvents()
        }

        //ASSERT
        confirmVerified(repository)
        assertEquals(DownloadEventsUseCase.Result(fakeEvents), result)
    }
}
