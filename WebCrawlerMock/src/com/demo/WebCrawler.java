package com.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class WebCrawler {
       
    public static void main(String[] args) {

        // How long to wait for threads to finish
        long     timeout  = 5;
        TimeUnit t_unit   = TimeUnit.SECONDS;
        int      poolSize = 10;

        String filename = "internet_1.json";

        // Create the "Internet"
        JsonInternet internet  = new JsonInternet(filename);
        CrawlerData  crawlerDB = new CrawlerData();

        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        // Get first address
        String firstAddress = internet.getFirstAddress();
        //System.out.println(firstAddress+"    firstAddress");

        // Launch the crawler
        try {

            pool.submit(new CrawlerThread(firstAddress, crawlerDB, internet, pool));

        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }

        // Shut it down
        try {

            pool.awaitTermination(timeout, t_unit);
            pool.shutdownNow();

        } catch (InterruptedException e) {
            e.printStackTrace();
            pool.shutdownNow();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // prints the results
        crawlerDB.print();

    } 
}
