package com.example.client.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**	单例的client
 * @author mike
 *
 */
public class Client {

	private static String clusterName = "chenming";

	private static List<String> addresses;
	
	static {
		addresses = new ArrayList<>();
		addresses.add("127.0.0.1:9300");
		//addresses.add("192.168.2.201:9300");
	}

	private static TransportClient client;

	static {
		
		//Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true).build();
		client = new PreBuiltTransportClient(Settings.EMPTY);
		String host;
		String port;
		String[] array;
		for (String address : addresses) {
			array = address.split(":");
			host = array[0];
			port = array[1];
			try {
				client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), Integer.valueOf(port)));
			} catch (NumberFormatException | UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	private Client() {
	}

	public static TransportClient getClient() {
		return client;
	}
}
