package com.test.demo;

import java.io.IOException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App 
{
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main( String[] args ) throws IOException, JSONException
	{
		System.out.println( "Start to read data stream!" );
		String url = "https://tweet-service.herokuapp.com/sps";

		final StreamProcessor sp = new StreamProcessor(url);
		LOG.debug("Starting stream processor for stream:  " + url);
		sp.start();

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				try {
					sp.stop();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		System.out.println("Terminating ...");
	}

}
