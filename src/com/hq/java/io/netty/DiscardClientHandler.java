package com.hq.java.io.netty;

/*
 2    * Copyright 2012 The Netty Project
 3    *
 4    * The Netty Project licenses this file to you under the Apache License,
 5    * version 2.0 (the "License"); you may not use this file except in compliance
 6    * with the License. You may obtain a copy of the License at:
 7    *
 8    *   http://www.apache.org/licenses/LICENSE-2.0
 9    *
 10   * Unless required by applicable law or agreed to in writing, software
 11   * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 12   * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 13   * License for the specific language governing permissions and limitations
 14   * under the License.
 15   */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handles a client-side channel.
 */
public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {

	private ByteBuf content;
	private ChannelHandlerContext ctx;

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		this.ctx = ctx;

		// Initialize the message.
		content = ctx.alloc().directBuffer(DiscardClient.SIZE)
				.writeZero(DiscardClient.SIZE);

		// Send the initial messages.
		generateTraffic();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		content.release();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// Server is supposed to send nothing, but if it sends something,
		// discard it.
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

	long counter;

	private void generateTraffic() {
		// Flush the outbound buffer to the socket.
		// Once flushed, generate the same amount of traffic again.
		ctx.writeAndFlush(content.duplicate().retain()).addListener(
				trafficGenerator);
	}

	private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
		@Override
		public void operationComplete(ChannelFuture future) {
			if (future.isSuccess()) {
				generateTraffic();
			} else {
				future.cause().printStackTrace();
				future.channel().close();
			}
		}
	};
}