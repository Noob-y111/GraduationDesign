package com.example.graduationdesign.tools

class TimeFormat {
    companion object{
        fun timeFormat(time: Int): String{
            var second = time / 1000
            val min = second / 60
            second %= 60
            return (if (min > 9) min.toString() + "" else "0$min") + ":" + if (second > 9) second else "0$second"
        }
    }
}