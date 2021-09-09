package com.example.lib

fun binarySearch(nums: IntArray, target: Int): Int {
    var left = 0
    var right = nums.size
    var mid = (left + right) / 2

    while (left <= right) {
        when {
            target > nums[mid] -> {
                left = mid + 1
                mid = (left + right) / 2
            }
            target <= nums[mid] -> {
                right = mid
                mid = (right + left) / 2
            }
            target == nums[mid] -> {
                return mid
            }
        }
    }
    return -1
}

fun isBadVersion(version: Int): Boolean {
    return version >= 1702766719
}

fun firstBadVersion(n: Int): Int {
    var minVersion = 1
    var maxVersion = n
    while (minVersion < maxVersion) {
        println("=======================:")
        val fakeBadVersion = (minVersion + maxVersion) / 2
        if (isBadVersion(fakeBadVersion)) {
            maxVersion = fakeBadVersion
        } else {
            minVersion = fakeBadVersion + 1
        }
    }
    return minVersion
}

fun searchInsert(numbers: IntArray, target: Int): Int {
    var low = 0
    var high = numbers.size - 1
    while (low <= high) {
        val mid = (low + high) / 2
        when {
            target == numbers[mid] -> {
                return mid
            }
            target < numbers[mid] -> {
                high = mid - 1
            }
            target > numbers[mid] -> {
                low = mid + 1
            }
        }
    }
    return low
}

fun sortedSquares(numbers: IntArray): IntArray {
    for (i in numbers.indices){
        if (numbers[i] < 0 ){
            numbers[i] *= -1
        }
    }
    for (i in numbers.indices){
        for (j in i until numbers.size){
            if (numbers[i] > numbers[j]){
                val temp = numbers[j]
                numbers[j] = numbers[i]
                numbers[i] = temp
            }
        }
    }
    for (i in numbers.indices){
        numbers[i] *= numbers[i]
    }
//    numbers.forEach {
//        println(it)
//    }
    return numbers
}

fun newSquare(numbers: IntArray): IntArray {
//    for (i in numbers.indices){
//        numbers[i] *= numbers[i]
//    }
    for (i in numbers.indices){
        for (j in i until numbers.size){
            if (numbers[i] < 0 ){
                numbers[i] = -1 * numbers[i]
            }
            if (numbers[j] < 0 ){
                numbers[j] = -1 * numbers[j]
            }
            if (numbers[i] > numbers[j]){
                val temp = numbers[j]
                numbers[j] = numbers[i]
                numbers[i] = temp
            }
        }
        numbers[i] *= numbers[i]
    }
//    numbers.forEach {
//        println(it)
//    }
    return numbers
}

fun jueDuiZhi(number: Int): Int{
    return if (number > 0) number else -1 * number
}

val arr = intArrayOf(23, 45, 65, 61, 78, 19, 12, 80, 30, 72, 90, 31, 83, 2, 70, 46)

fun selectionSort(){
    for (i in arr.indices){
        for (j in i+1 until arr.size){
            if (arr[i] > arr[j]){
                val temp = arr[j]
                arr[j] = arr[i]
                arr[i] = temp
            }
        }
    }
    arr.forEach {
        println(it)
    }
}

fun bubbleSort() {
    for (i in arr.indices) {
        for (j in 1 until arr.size - i) {
            if (arr[j - 1] > arr[j]) {
                val temp = arr[j - 1]
                arr[j - 1] = arr[j]
                arr[j] = temp
            }
        }
    }
    for (i in arr) {
        println(i)
    }
}



fun main(args: Array<String>) {
//    println(firstBadVersion(2126753390))
//    newSquare(intArrayOf(-7,-3,2,3,11))
    bubbleSort()
}


