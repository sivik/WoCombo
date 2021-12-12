package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.core.data.exceptions.DownloadEventsException
import com.example.wocombo.core.data.exceptions.DownloadSchedulesException
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.ScheduleFailures
import com.example.wocombo.core.domain.models.Event
import com.example.wocombo.core.domain.models.Schedule
import com.example.wocombo.core.domain.repositories.EventsRepository
import com.example.wocombo.core.domain.repositories.ScheduleRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
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


internal class DownloadSchedulesUseCaseTest {

    private lateinit var repository: ScheduleRepository
    private lateinit var useCase: DownloadSchedulesUseCase

    private companion object {
        @JvmStatic
        fun arguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                SSLHandshakeException(""),
                DownloadSchedulesUseCase.Result(failure = CommunicationsFailures.InternalServerFailure)
            ),
            Arguments.of(
                TimeoutException(),
                DownloadSchedulesUseCase.Result(failure = CommunicationsFailures.ConnectionFailure)
            ),
            Arguments.of(
                UnknownHostException(),
                DownloadSchedulesUseCase.Result(failure = CommunicationsFailures.NoInternetFailure)
            ),
            Arguments.of(
                ConnectException(),
                DownloadSchedulesUseCase.Result(failure = CommunicationsFailures.NoInternetFailure)
            ),
            Arguments.of(
                InterruptedIOException(),
                DownloadSchedulesUseCase.Result(failure = CommunicationsFailures.ConnectionFailure)
            ),
            Arguments.of(
                DownloadSchedulesException("Cannot download"),
                DownloadSchedulesUseCase.Result(failure = ScheduleFailures.DownloadSchedulesFailure)
            ),
            Arguments.of(
                Exception(),
                DownloadSchedulesUseCase.Result(failure = Failure.UnknownFailure)
            )
        )
    }

    @BeforeEach
    fun init() {
        repository = mockk()
        useCase = DownloadSchedulesUseCase(repository)
    }

    @ParameterizedTest
    @MethodSource("arguments")
    fun `Check failure for download schedules`(
        exception: Exception,
        expected: DownloadSchedulesUseCase.Result
    ) {
        //ARRANGE
        val fakeRequest = DownloadSchedulesUseCase.Request()

        every {
            repository.downloadSchedules()
        } throws exception

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.downloadSchedules()
        }

        //ASSERT
        confirmVerified(repository)
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `Download schedules successfully`() {
        //ARRANGE
        val fakeRequest = DownloadSchedulesUseCase.Request()

        val fakeSchedules = arrayListOf(
            Schedule(
                id = "1",
                date = DateTime(2020, 2, 22, 2, 2, 2),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/dazn-recruitment/o/310798917361_image-header_pDach_1554667420000.jpeg",
                subtitle = "Soccer",
                title = "Liverpool vs AS Monaco"
            )
        )

        every { repository.downloadSchedules() } returns fakeSchedules

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.downloadSchedules()
        }

        //ASSERT
        confirmVerified(repository)
        Assertions.assertEquals(DownloadSchedulesUseCase.Result(fakeSchedules), result)
    }
}
