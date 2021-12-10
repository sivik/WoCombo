package com.example.wocombo.common.functional

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */

sealed class Failure {
    object UnknownFailure : Failure()
    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}