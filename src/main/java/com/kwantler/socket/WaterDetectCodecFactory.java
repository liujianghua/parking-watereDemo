package com.kwantler.socket;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class WaterDetectCodecFactory implements ProtocolCodecFactory {
	
	private final WaterDetectEncoder encoder;
	private final WaterDetectDecoder decoder;

	public WaterDetectCodecFactory() {
		this(Charset.forName("UTF-8"));
	}

	public WaterDetectCodecFactory(Charset charSet) {
		this.encoder = new WaterDetectEncoder();
		this.decoder = new WaterDetectDecoder();
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}
}


