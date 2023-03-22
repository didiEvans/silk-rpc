package com.anker.rpc.core.codec;

import com.anker.rpc.core.common.constants.RpcConstants;
import com.anker.rpc.core.protocol.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * rpc encoder
 *
 * @author Anker
 */
public class Encoder extends MessageToByteEncoder<RpcProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol msg, ByteBuf out) throws Exception {
        out.writeShort(msg.getMagicNumber());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
        out.writeBytes(RpcConstants.DEFAULT_DECODE_CHAR.getBytes());
    }
}
