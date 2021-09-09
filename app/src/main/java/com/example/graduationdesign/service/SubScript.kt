package com.example.graduationdesign.service

import kotlin.random.Random

class SubScript {

    companion object{
        const val PLAY_ORDER = "order"
        const val RANDOM_NEXT = "random"
        const val NORMAL_NEXT = "normal"
        const val LOOP_NEXT = "loop"
    }

    fun randomNext(size: Int): Int{
        return (0 until size).random()
    }

    fun looping(){

    }

    fun normalNext(position: Int, size: Int): Int{
        return if (position + 1 <= size - 1) {
            position + 1
        } else {
            0
        }
    }

    fun normalLast(position: Int, size: Int): Int{
        return if (position - 1 >= 0) {
            position - 1
        } else {
            size - 1
        }
    }

}