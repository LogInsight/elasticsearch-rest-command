package com.everdata.test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.utils.URIBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class CommandActionTest{
	private String host = "http://192.168.200.121:9200/_command";
    
	@Before
    public void setUp() throws Exception {
		
	}

    @After
    public void tearDown() throws Exception {
    	
    }
    
    static AtomicInteger success_conn = new AtomicInteger(0);
    static AtomicInteger success_recv = new AtomicInteger(0);
    
	
	
    @Test
    @Ignore
    public void multiThread() {
    	Random rand = new Random();

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.addParameter("q", String.valueOf(rand.nextDouble()));
        uriBuilder.addParameter("query", "true");
        final String uri;
		try {
			uri = uriBuilder.build().toString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		List<Thread> handles = new ArrayList<Thread>();
		for(int j = 0; j < 10; j++){
			for(int i = 0; i < 1000; i++){
				Thread handle = new Thread(new Runnable(){
		
					@Override
					public void run() {
						
						String json = HttpHelper.sendGetUrl(host, uri);
						
						if(json != null && !json.contains("Exception")){
							success_recv.incrementAndGet();
						}
				        //System.out.println(json);
					}
					
				});
				
				handle.start();
				handles.add(handle);
				
				
				
			
			}
			System.out.println("-----------------------");
			
			for(Thread t: handles){
				try {
					//System.out.print(t.isAlive());
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			handles.clear();
		}
		
		
		System.out.println("success_conn:" + success_conn.get());
		System.out.println("success_recv:" + success_recv.get());
		        
    }

}
