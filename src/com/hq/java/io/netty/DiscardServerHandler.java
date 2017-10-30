package com.hq.java.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * http://netty.io/wiki/user-guide-for-4.x.html
 * 
 * Handles a server-side channel.
 * 它处理Netty生成的I / O事件
 * 
 * 
 */
/**
 * @author Administrator
 *
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

	
	/* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     * 每当从客户端接收到新数据时，调用该方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
    	//在这个例子中，所接收的消息的类型是ByteBuf
//        ((ByteBuf) msg).release(); // (3)
        
//        try {
//            // Do something with msg
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
    	
        ByteBuf in = (ByteBuf) msg;
        System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
        
//        try {
//            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            ReferenceCountUtil.release(msg); // (2)
//        }
        
        //它将接收到的数据发送回来,而不是将接收的数据打印到控制台 .
        //Netty在你写出来的时候为你释放它
        ctx.write(msg+"-return"); // (1)
        ctx.flush(); // (2)
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
     * The event handler method is called with a Throwable
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
