package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.core.data.exceptions.DownloadCurrencyException
import com.example.wocombo.core.domain.errors.CommunicationsFailures
import com.example.wocombo.core.domain.errors.CurrencyFailures
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.repositories.CurrencyRepository
import com.example.wocombo.core.presentation.enums.SortType
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
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

internal class DownloadCurrenciesUseCaseTest {

    private lateinit var repository: CurrencyRepository
    private lateinit var useCase: DownloadCurrenciesUseCase

    private companion object {
        @JvmStatic
        fun arguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                SSLHandshakeException(""),
                DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.InternalServerFailure)
            ),
            Arguments.of(
                TimeoutException(),
                DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.ConnectionFailure)
            ),
            Arguments.of(
                UnknownHostException(),
                DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.NoInternetFailure)
            ),
            Arguments.of(
                ConnectException(),
                DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.NoInternetFailure)
            ),
            Arguments.of(
                InterruptedIOException(),
                DownloadCurrenciesUseCase.Result(failure = CommunicationsFailures.ConnectionFailure)
            ),
            Arguments.of(
                DownloadCurrencyException("Cannot download"),
                DownloadCurrenciesUseCase.Result(failure = CurrencyFailures.DownloadCurrenciesFailure)
            ),
            Arguments.of(
                Exception(),
                DownloadCurrenciesUseCase.Result(failure = Failure.UnknownFailure)
            )
        )
    }

    @BeforeEach
    fun init() {
        repository = mockk()
        useCase = DownloadCurrenciesUseCase(repository)
    }

    @ParameterizedTest
    @MethodSource("arguments")
    fun `Check failure for download currencies`(
        exception: Exception,
        expected: DownloadCurrenciesUseCase.Result
    ) {
        //ARRANGE
        val fakeRequest = DownloadCurrenciesUseCase.Request(SortType.ASCENDING)

        every {
            repository.getCurrencies()
        } throws exception

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.getCurrencies()
        }

        //ASSERT
        confirmVerified(repository)
        assertEquals(expected, result)
    }

    @Test
    fun `Download currencies successfully`() {
        //ARRANGE
        val fakeRequest = DownloadCurrenciesUseCase.Request(SortType.ASCENDING)

        val fakeCurrencies = arrayListOf(
            Currency(
                id = 1,
                symbol = "BTC",
                name = "Bitcoin",
                nameId = "bitcoin",
                percentHour = 1.78,
                percentDay = 2.55,
                priceUsd = 2.34,
                volume = 234.5345
            ),
            Currency(
                id = 2,
                symbol = "DOGE",
                name = "Doge",
                nameId = "DogeCoin",
                percentHour = 6.78,
                percentDay = 6.55,
                priceUsd = 7.34,
                volume = 45.5345
            ),
            Currency(
                id = 3,
                symbol = "SHIB",
                name = "shiba",
                nameId = "Shiba Inu",
                percentHour = 4.78,
                percentDay = 6.55,
                priceUsd = 7.34,
                volume = 44.5345
            )
        )

        every { repository.getCurrencies() } returns fakeCurrencies

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.getCurrencies()
        }

        //ASSERT
        confirmVerified(repository)
        assertEquals(DownloadCurrenciesUseCase.Result(fakeCurrencies), result)
    }
}