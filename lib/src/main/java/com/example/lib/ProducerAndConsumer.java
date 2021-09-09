package com.example.lib;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class ProducerAndConsumer {

    public final ArrayList<Integer> queue = new ArrayList<>();
    public int size;
    private Consumer consumer;
    private Producer producer;

    public ProducerAndConsumer() {
        this.size = 0;
    }

    public ProducerAndConsumer(int size) {
        this.size = size;
        consumer = new Consumer();
        producer = new Producer();
    }

    public static void main(String[] args) {
        ProducerAndConsumer producerAndConsumer = new ProducerAndConsumer(10);
        producerAndConsumer.producer.start();
        producerAndConsumer.consumer.start();

    }

    class Consumer extends Thread {

        @Override
        public void run() {
            consume();
        }

        public void consume() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() <= 0) {
                        queue.notify();
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String str = "kong";
                        System.out.println(str);
                    }

                    String str = "consume ";
                    System.out.println( str+ queue.remove(0));
                    queue.notify();
                    try {
                        sleep((new Random()).nextInt(3)* 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class Producer extends Thread {

        @Override
        public void run() {
            produce();
        }

        public void produce() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() >= size) {
                        queue.notify();
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("man");
                    }
                    int number = new Random().nextInt(100);
                    queue.add(number);
                    System.out.println("produce " + number);
                    queue.notify();
                    try {
                        sleep((new Random()).nextInt(3)* 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}


