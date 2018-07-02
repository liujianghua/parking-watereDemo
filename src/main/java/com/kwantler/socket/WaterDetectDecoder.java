package com.kwantler.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class WaterDetectDecoder extends CumulativeProtocolDecoder {

	private static final int HEADER_LENGTH = 6;

	private static final int END_LENGTH = 6;

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		try {

			if (in.remaining() > HEADER_LENGTH) {

				reset(in);
				in.mark();

				byte[] headerArray = new byte[HEADER_LENGTH];

				in.get(headerArray);

				String header = new String(headerArray);

				int dataLength = Integer.parseInt(header.substring(2));

				if (in.remaining() < (dataLength + END_LENGTH)) {

					// 内容不够， 重置position到操作前，进行下一轮接受新数据
					in.reset();
					return false;

				} else {

					byte[] dataArray = new byte[dataLength + END_LENGTH];

					in.get(dataArray);

					String data = new String(dataArray);

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

//	/**
//	 * 重新定位包头
//	 * 
//	 * @param in
//	 */
//	private void reset(IoBuffer in) {
//
//		byte[] dataArray = new byte[2];
//
//		for (int i = 0; i < in.limit(); i++) {
//
//			if (in.remaining() > HEADER_LENGTH) {
//
//				in.mark();
//
//				in.get(dataArray);
//
//				String data = new String(dataArray);
//
//				if ("##".equals(data)) {
//					in.reset();
//					return;
//				}
//			} else {
//				return;
//
//			}
//		}
//
//	}
	
	/**
	 * 重新定位包头
	 * 
	 * @param in
	 */
	private static void reset(IoBuffer in) {

		byte[] dataArray = new byte[1];
		String data = "";
		
		int max = in.limit();

		for (int i = 0; i < max; i++) {

			if (in.remaining() > HEADER_LENGTH) {

				in.mark();

				in.get(dataArray);

				data = new String(dataArray);

				if ("#".equals(data)) {

					in.get(dataArray);
					data = new String(dataArray);
					if ("#".equals(data)) {
						in.reset();
						return;
					}

				}
			} else {
				return;

			}
		}

	}

}
