package com.kwantler.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class WeatherDecoder extends CumulativeProtocolDecoder {

	private static final int TOTAL_LENGTH = 70;

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		try {

			if (in.remaining() > 4) {

				in.mark();

				if (in.remaining() < (TOTAL_LENGTH)) {

					// 内容不够， 重置position到操作前，进行下一轮接受新数据
					in.reset();
					return false;

				} else {

					byte[] dataArray = new byte[TOTAL_LENGTH];

					in.get(dataArray);

					String data = bytesHexString(dataArray);

					out.write(data);

				}

				// 如果读取一个完整包内容后还粘了包，就让父类再调用一次，进行下一次解析
				if (in.remaining() > 0) {

					return true;

				}

				return false;

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return false;
	}

	public static String bytesHexString(byte[] b) {

		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < b.length; i++) {
			// 转换16进制
			String hexString = Integer.toHexString(b[i] & 0xFF);

			if (hexString.length() < 2) {

				hexString = "0" + hexString;

			}
			stringBuffer.append(hexString);
		}

		return stringBuffer.toString();
	}

}
