package com.demo;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.JsonArray;

/**
 * Shared data used by the crawler. Used to track and save results.
 *
 * Uses four Hashtable objects: 
 *     visitedLinks, successLinks, SkipList, errorLinks. 
 * Hashtable is already thread safe, search speed is constant 
 * even for large tables.
 *
 */
public class CrawlerData {

    private Hashtable<String, Boolean> visitedLinks;

    private Hashtable<String, Boolean> successLinks;
    private Hashtable<String, Boolean> errorLinks;
    private Hashtable<String, Boolean> skippedLinks;

    /**
     * Creates a data store for the webcrawler
     * 
     * visitedLinks - urls that have have been checked already. 
     * successLinks - urls that were successfully parsed.
     * errorLinks   - urls that were not reachable. 
     * skippedLinks - urls that were encountered but already checked.
     * 
     * The Boolean values are not currently used.
     */
    public CrawlerData() {
        this.visitedLinks = new Hashtable<String, Boolean>();
        this.successLinks = new Hashtable<String, Boolean>();
        this.errorLinks   = new Hashtable<String, Boolean>();
        this.skippedLinks = new Hashtable<String, Boolean>();
    }

    /**
     * Prints the data 
     */
    public void print() {

        System.out.println("Success:");
        System.out.println(createJsonArray(successLinks));
        System.out.println("Skipped:");
        System.out.println(createJsonArray(skippedLinks));
        System.out.println("Error:");
        System.out.println(createJsonArray(errorLinks));
    }

    /**
     * 
     * Creates a JsonArray from the hashtables
     * 
     * @params list one of this object's hashtables
     * @return a JsonArray representation of the hashtable
     * 
     */
    private JsonArray createJsonArray(Hashtable<String, Boolean> list) {

        JsonArray jsonArray = new JsonArray();

        Set<String> keys = list.keySet();
        Iterator<String> itr = keys.iterator();
        while(itr.hasNext()){
        	jsonArray.add(itr.next());
        	
        }
        

        return jsonArray;
    }

    /**
     * Adds a url to the visited list if it is not already in it.
     * 
     * Solves a synchronization problem by checking the list and adding to it in one
     * synchronized method
     * 
     * @param url the address to attempt to add
     * @return true if new url is added, false if the url is already visited
     * @throws NullPointerException if url is null
     */
    public synchronized boolean ifNotVisitedPut(String url) {

        // was initially planning to utilize the true/false in hashtables,
        // but get<Key> returns null if key doesn't already exist.
        // in future can implement this object's own synchronized contains and put

        if (visitedLinks.containsKey(url)) {
            return false;
        }

        visitedLinks.put(url, true);
        return true;
    }

    /**
     * Checks if address was already checked
     * 
     * @param url the address to check
     * @return true if the address has already been seen, false if not
     * @throws NullPointerException if url is null
     */
    public synchronized boolean wasVisited(String url) {
        return visitedLinks.containsKey(url);
    }

    /**
     * Puts a url in the success hashtable
     * 
     * @param url the address to add
     * @throws NullPointerException if url is null
     */
    public void putSuccess(String url) {
        this.successLinks.put(url, true);
    }

    /**
     * Puts a url in the error hashtable
     * 
     * @param url the address to add
     * @throws NullPointerException if url is null
     */
    public void putError(String url) {
        this.errorLinks.put(url, true);
    }

    /**
     * Puts a url in the skipped hashtable
     *
     * @param url the address to add
     * @throws NullPointerException if url is null
     */
    public void putSkipped(String url) {
        this.skippedLinks.put(url, true);

    }
}
