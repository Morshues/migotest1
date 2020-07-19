package com.morshues.migotest1.api

sealed class HTTPResult {
    class Success(val data: String): HTTPResult()
    class Failure(val msg: String): HTTPResult()
}