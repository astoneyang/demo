package com.test.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(StreamProcessor.class);

	long currentSec = -1;
	Map<String, Long> streamMap = new ConcurrentHashMap<String, Long> ();
	
	String url;
	InputStream is = null;
	
	public StreamProcessor (String url) {
		this.url = url;
	}
	public void start() throws IOException, JSONException {
		// Open streams
		is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String line = null;
			// Read data stream line by line
			while ((line = rd.readLine()) != null)
			{
				LOG.debug(line);
				int startIndex = line.indexOf('{');
				if (startIndex >= 0) {
					JSONObject json = new JSONObject(line.substring(startIndex));
					String sev = (String)json.get("sev");
					long time_in_sec = (Long)json.get("time")/1000;

					if (currentSec == -1) {
						currentSec = time_in_sec;
					} else {
						if (currentSec < time_in_sec) {
							currentSec = time_in_sec;
							// write the aggregated result and cleanup the streamMap
							System.out.println("------------------  " + currentSec + " ---------------------");
							LOG.info("------------------  " + currentSec + " ---------------------");
							Iterator<Entry<String, Long>> it = streamMap.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry<String, Long> pair = (Map.Entry<String, Long>)it.next();
								String key = (String) pair.getKey();
								String [] keys = key.split("\\^");
								Long count = (Long) pair.getValue();
								JSONObject jsonObj = new JSONObject();

								jsonObj.put("device", keys[0]);
								jsonObj.put("title", keys[1]);
								jsonObj.put("country", keys[2]);
								jsonObj.put("SPS", count);

								System.out.println(jsonObj.toString());
								LOG.info(jsonObj.toString());
							}
							streamMap.clear();
						}
					}

					if (sev.equals("success")) {
						String device = (String)json.get("device");
						String title = (String)json.get("title");
						String country = (String)json.get("country");
						StringBuffer sb = new StringBuffer();
						sb.append(device).append('^').append(title).append('^').append(country);
						String key = sb.toString();
						// lookup the streamMap
						if (streamMap.containsKey(key)) {
							long count = streamMap.get(key);
							streamMap.put(key, count+1);
						} else {
							streamMap.put(key, 1L);
						}
					}
				}
			}
		} finally {
			is.close();
		}
	}

	public void stop() throws IOException {
		try {
			if (is != null) {
				is.close();
			}
		} finally {
			is.close();
		}
	}
}
