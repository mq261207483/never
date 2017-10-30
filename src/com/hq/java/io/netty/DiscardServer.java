package com.hq.java.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discards any incoming data.
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
    	//NioEventLoopGroup 处理I / O操作的多线程事件循环
    	//第一个，通常称为“boss”，接受传入连接。第二个，通常称为“工人”，一旦老板接受连接并向工人注册接受的连接，就处理接受的连接的流量
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	//ServerBootstrap是一个设置服务器的助手类。您可以使用一个设置服务器Channel直接。但是，请注意，这是一个冗长的过程，你在大多数情况下不需要这样做。
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3) 指定ta 接受传入的连接
             .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new DiscardServerHandler());
                 }
             })
             //我们正在编写一个TCP / IP服务器，所以我们被允许设置套接字选项
             .option(ChannelOption.SO_BACKLOG, 128)          // (5)
             //childOption（）用于父ServerChannel接受的通道，在这种情况下为NioServerSocketChannel。
             .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * @param args
     * @throws Exception
     * telnet localhost 8080  测试
     */
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new DiscardServer(port).run();
    }
}