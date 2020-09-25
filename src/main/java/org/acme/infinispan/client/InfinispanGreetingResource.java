package org.acme.infinispan.client;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;

@Path("/infinispan")
public class InfinispanGreetingResource {

    @Inject
    @Remote("default")
    RemoteCache<String, String> cache;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        cache.put("hello", "Hello World, Infinispan is up!");
        return cache.get("hello");
    }

    @POST
    @Path("/fill")
    @Produces(MediaType.TEXT_PLAIN)
    public String fillerup() throws Exception {

        for (int i = 0 ; i < 100 ; i++ ) {
            char[] chars = new char[2 * 1024 * 1024];
            Arrays.fill(chars, 'f');
            cache.put(Math.random() + "", new String(chars), 30, TimeUnit.SECONDS, 30, TimeUnit.SECONDS);
            System.out.println("Added " + (i + 1) + "MB");
            Thread.sleep(2000);

        }
        return "Added 500MB of junk";      
    }

}
