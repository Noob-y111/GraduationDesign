package com.example.lib

import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

fun main() {

    var i = 0
    val o: Any = Object()
    var threadNum = 0
    repeat(10) {
        thread {
            threadNum++;
            Thread.sleep(((10 - it) * 1000).toLong())
            synchronized(o) {
                repeat(10000) {
                    i++
                }
            }
            threadNum--;
            if (threadNum == 0) {
                println("================i: $i")
            }
        }
    }
}

class Test{
    companion object{
        @Volatile
        var threadNum = 0
    }
}