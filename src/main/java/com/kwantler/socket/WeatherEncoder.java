package com.kwantler.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.kwantler.util.BGYUtils;

/**
 * <协议：数据发送方加码器>
 * 
 * @author HYH
 * 
 */
public class WeatherEncoder extends ProtocolEncoderAdapter {

	private static final String HEADER = "FA";
	private static final String END = "FB";

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) {

		String send = (String) message;

		// 校验码
		String result = HEADER +send+ END;

		IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
		buffer.put(result.getBytes());
		buffer.flip();
		out.write(buffer);
	}
	
	
	/**
	 * 定长字符，不足前面补0
	 * @param v
	 * @param length
	 * @return
	 */
	private  String prefixZero(Integer v, int length) {
		
		String src =String.valueOf(v);

		String ret = src;

		for (int i = length - src.length(); i > 0; i--) {

			ret = "0" + ret;
		}

		return ret;
	}
	
	

}