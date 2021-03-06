package com.kwantler.socket;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.kwantler.util.BGYUtils;

/**
 *
 */
public class WeatherServer {

	private static String PORT = BGYUtils.getLocalConfig("weatherTcpPort", "WEB-INF/classes/environment.properties");

	@Autowired
	private IoAcceptor ioAcceptor;

	public void bind() throws IOException {
		ioAcceptor.bind(new InetSocketAddress(Integer.parseInt(PORT)));
		ioAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new WeatherCodecFactory()));

		System.out.println("端口启动" + PORT);
	}

}
