package com.mobile.fodein.domain.interfaces

import io.reactivex.Scheduler

interface IPostExecutionThread {
    val scheduler: Scheduler
}