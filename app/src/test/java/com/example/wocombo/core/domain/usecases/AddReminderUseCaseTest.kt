package com.example.wocombo.core.domain.usecases

import com.example.wocombo.common.functional.Failure
import com.example.wocombo.core.domain.models.Reminder
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

internal class AddReminderUseCaseTest {

    private lateinit var repository: ReminderRepository
    private lateinit var useCase: AddReminderUseCase

    private companion object {
        @JvmStatic
        fun arguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                Exception(),
                AddReminderUseCase.Result(failure = Failure.UnknownFailure)
            )
        )
    }

    @BeforeEach
    fun init() {
        repository = mockk()
        useCase = AddReminderUseCase(repository)
    }

    @ParameterizedTest
    @MethodSource("arguments")
    fun `Check failure for add reminder`(
        exception: Exception,
        expected: AddReminderUseCase.Result
    ) {
        //ARRANGE
        val fakeReminder = Reminder(
            1,
            "FC PORTO vs REAL MADRID",
            DateTime(2020, 2, 2, 2, 2, 2, 2)
        )
        val fakeRequest = AddReminderUseCase.Request(fakeReminder)

        every {
            repository.addReminder(fakeReminder)
        } throws exception

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.addReminder(fakeReminder)
        }

        //ASSERT
        confirmVerified(repository)
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `Add reminder successfully`() {
        //ARRANGE
        val fakeReminder = Reminder(
            1,
            "FC PORTO vs REAL MADRID",
            DateTime(2020, 2, 2, 2, 2, 2, 2)
        )
        val fakeRequest = AddReminderUseCase.Request(fakeReminder)

        every { repository.addReminder(fakeReminder) } just runs

        //ACT
        val result = useCase.execute(fakeRequest)

        verify {
            repository.addReminder(fakeReminder)
        }

        //ASSERT
        confirmVerified(repository)
        Assertions.assertEquals(AddReminderUseCase.Result(fakeReminder.id), result)
    }
}