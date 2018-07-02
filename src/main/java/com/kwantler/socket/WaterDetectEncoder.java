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
public class WaterDetectEncoder extends ProtocolEncoderAdapter {

	private static final String HEADER = "##";
	private static final String END = "\r\n";

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) {

		String send = (String) message;

		// 校验码
		String crc = BGYUtils.crc16(send.getBytes());

		String result = HEADER +BGYUtils.prefixZero(send.length(),4)+ send + crc + END;

		IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
		buffer.put(result.getBytes());
		buffer.flip();
		out.write(buffer);
	}
	
	

	

}