package com.example.rlopatka.kotlinsamples

import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * Created by rlopatka on 29.10.2017.
 */
class SampleDataService {
    fun callSlowService(): Observable<Result<String>> {
        return Observable
                .just("Test")
                .delay(5, TimeUnit.SECONDS)
                .map { Result<String>(it, false) }
    }
}

data class Result<TValue>(val value: TValue?, val isFailure: Boolean)