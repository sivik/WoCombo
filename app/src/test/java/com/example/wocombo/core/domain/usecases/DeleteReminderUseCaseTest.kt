package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.core.domain.models.Event
import com.example.wocombo.core.domain.repositories.ReminderRepository
import io.mockk.*
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


internal class DeleteReminderUseCaseTest {

    private lateinit var repository: ReminderRepository
    private lateinit var useCase: DeleteReminderUseCase

    private companion object {
        @JvmStatic
        fun arguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                Exception(),
                DeleteReminderUseCase.Result(failure = Failure.UnknownFailure)
            )
        )
    }

    @BeforeEach
    fun init() {
        repository = mockk()
        useCase = DeleteReminderUseCase(repository)
    }

    @ParameterizedTest
    @MethodSource("arguments")
    fun `Check failure for delete reminder`(
        exception: Exception,
        expected: DeleteReminderUseCase.Result
    ) {
        //ARRANGE
        val fakeId = 20
        val fakeRequest = DeleteReminderUseCase.Request(fakeId)

        every {
            repository.deleteReminder(fakeId)
        } throws exception

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.deleteReminder(fakeId)
        }

        //ASSERT
        confirmVerified(repository)
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `Delete reminder successfully`() {
        //ARRANGE
        val fakeId = 20
        val fakeRequest = DeleteReminderUseCase.Request(fakeId)

        every { repository.deleteReminder(fakeId) } just runs

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.deleteReminder(fakeId)
        }

        //ASSERT
        confirmVerified(repository)
        Assertions.assertEquals(DeleteReminderUseCase.Result(fakeId), result)
    }
}