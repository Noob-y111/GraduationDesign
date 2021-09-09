package com.example.lib


fun main() {
    val array = intArrayOf(23, 45, 65, 61, 78, 19, 12, 80, 30, 72, 90, 31, 83, 2, 70, 46)
    array.forEach {
        print("$it ")
    }
    quickSort(array, 0, array.size -1 )
    println("=======================:")
    array.forEach {
        print("$it ")
    }
}

fun quickSort(arr: IntArray, lowIndex: Int, highIndex: Int){
    if (lowIndex < highIndex){
        val index = getIndex(arr, lowIndex, highIndex)
        quickSort(arr, lowIndex, index - 1)
        quickSort(arr, index + 1, highIndex)
    }
}

//val array = intArrayOf(23, 45, 65, 61, 78, 19, 12, 80, 30, 72, 90, 31, 83, 2, 70, 46)
fun getIndex(numbers: IntArray, lowIndex: Int, highIndex: Int): Int {
    val temp = numbers[lowIndex]
    var high = highIndex
    var low = lowIndex

    while (low < high) {
        while (numbers[high] >= temp && low < high){
            high --
        }
        numbers[low] = numbers[high]
        while (low < high && numbers[low] < temp){
            low ++
        }
        numbers[high] = numbers[low]
        numbers[low] = temp
    }
    return low
}