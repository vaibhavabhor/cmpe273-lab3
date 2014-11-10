package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.List;

import com.google.common.hash.Hashing;

public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        //CacheServiceInterface cache = new DistributedCacheService(
        //        "http://localhost:3000");
        //CacheServiceInterface cache1 = new DistributedCacheService(
        //		                "http://localhost:3001");
        
        //cache.put(1, "foo");
        //cache1.put(2, "hoo");
        //System.out.println("put(1 => foo)");
        //String value = cache.get(1);
        //System.out.println("get(1) => " + value);
        //String value2 = cache1.get(2);
        //System.out.println("get(2) => " + value2);
        
        // Storing list of all servers
        List<CacheServiceInterface> allServers = new ArrayList<CacheServiceInterface>();
        allServers.add(new DistributedCacheService("http://localhost:3000")); 
        allServers.add(new DistributedCacheService("http://localhost:3001")); 
        allServers.add(new DistributedCacheService("http://localhost:3002")); 
        
        // Sequence of key-value pairs
        // Intentional blank entry as data[0]
        char[] data = {' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        
        //PUT data into Cache servers using consistent hashing
        System.out.println("PUT data into Cache... ");
        for(int getKey=1; getKey<= data.length-1; getKey++)
        {
        	int bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(getKey)), allServers.size());
        	CacheServiceInterface serverToPut = allServers.get(bucket);
        	serverToPut.put(getKey, Character.toString(data[getKey]));
        	System.out.println("PUT --> Key=" + getKey + " and Value="+ data[getKey] + " routed to Cache server at localhost://300"+ bucket);
        }
        
        System.out.println(" ");
        //GET data from Cache servers consistent hashing
        System.out.println("GET data from Cache... ");
        for(int putKey=1; putKey<= data.length-1; putKey++)
        {
        	int bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(putKey)), allServers.size());
        	CacheServiceInterface serverToGet = allServers.get(bucket);
        	String getValue = serverToGet.get(putKey);
        	System.out.println("GET --> Obtained Key=" + putKey + " and Value="+ getValue + " from Cache server at localhost://300"+ bucket);
        }
        
        System.out.println("Existing Cache Client...");
    }

}
