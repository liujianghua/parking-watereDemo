package com.kwantler.socket;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class WeatherCodecFactory implements ProtocolCodecFactory {
	
	private final WeatherEncoder encoder;
	private final WeatherDecoder decoder;

	public WeatherCodecFactory() {
		this(Charset.forName("UTF-8"));
	}

	public WeatherCodecFactory(Charset charSet) {
		this.encoder = new WeatherEncoder();
		this.decoder = new WeatherDecoder();
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}
}


