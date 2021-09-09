package com.example.lib;

public class LeetCode {
    int[] array = new int[]{23, 45, 65, 61, 78, 19, 12, 80, 30, 72, 90, 31, 83, 2, 70, 46};

    public static void main(String[] args) {
        LeetCode leetCode = new LeetCode();
//        leetCode.selectionSort();
        int[] arr = leetCode.copyArray();
        leetCode.mergeSort(0, arr.length - 1, arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    public int[] copyArray() {
        int[] arr = new int[this.array.length];
        System.arraycopy(array, 0, arr, 0, array.length);
        return arr;
    }

    public void mergeSort(int left, int right, int[] a) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(left, mid, a);
            mergeSort(mid + 1, right, a);
            insert(a, mid, left, right);
        }
    }

    public void insert(int[] arr, int mid, int left, int right) {
        for (int i = mid + 1; i <= right; i++) {
            int insertIndex = i;
            int temp = arr[i];
            for (int j = i - 1; j >= 0; j--) {
                if (temp < arr[j]) {
                    arr[j + 1] = arr[j];
                    insertIndex = j;
                }
            }
            arr[insertIndex] = temp;
        }
    }

    public void insertionSort() {
        int[] arr = new int[this.array.length];
        System.arraycopy(array, 0, arr, 0, array.length);
        int insertIndex;
        int temp;
        for (int i = 1; i < arr.length; i++) {
            temp = arr[i];
            insertIndex = i;
            for (int j = i - 1; j >= 0; j--) {
                if (temp > arr[j]) {
                    arr[j + 1] = arr[j];
                    insertIndex = j;
                }
            }
            arr[insertIndex] = temp;
        }
        for (int i : arr) {
            System.out.println(i);
        }
    }

    public void selectionSort() {
        int[] arr = new int[this.array.length];
        System.arraycopy(array, 0, arr, 0, array.length);

        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        for (int i : arr) {
            System.out.println(i);
        }
    }

    public void bubbleSort() {
        int[] arr = new int[this.array.length];
        System.arraycopy(array, 0, arr, 0, array.length);

        for (int i = 0; i < arr.length; i++) {
            for (int j = 1; j < arr.length - i; j++) {
                if (arr[j - 1] < arr[j]) {
                    int temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }

        for (int i : arr) {
            System.out.println(i);
        }
    }


}
