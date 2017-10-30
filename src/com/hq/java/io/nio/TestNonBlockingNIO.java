package com.hq.java.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.junit.Test;

public class TestNonBlockingNIO {

	@Test
	public void client() throws IOException{
		SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
		
		//非阻塞模式
		sc.configureBlocking(false);
		
		ByteBuffer b = ByteBuffer.allocate(1024);
		
//		b.put((new Date().toString()).getBytes());
//		b.flip();
//		sc.write(b);
		
		Scanner s = new Scanner(System.in);
		while(s.hasNext()){
			String next = s.next();
			
			b.put((new Date().toString() +"\n"+ next).getBytes());
			b.flip();
			sc.write(b);
			b.clear();
		}
		
		sc.close();
	}
	
	@Test
	public void server() throws IOException{
		ServerSocketChannel ssc = ServerSocketChannel.open();
		
		ssc.configureBlocking(false);
		
		ssc.bind(new InetSocketAddress(8888));
		
		//选择器
		Selector selector = Selector.open();
		
		//将通道注册到选择器     (指定监听事件)
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		//轮询 获取选择器上 已经准备就绪的事件
		while(selector.select()>0){
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = keys.iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				//获取 事件
				if(key.isAcceptable()){
					//接收客户端连接  可以单独线程，也可以多线程
//					System.out.println("isAcceptable");
					//接收就绪，
					//获取客户端连接
					SocketChannel sc = ssc.accept();
					//设置非阻塞
					sc.configureBlocking(false);
					
					//注册到选择器上
					sc.register(selector, SelectionKey.OP_READ);
					
				}else if(key.isReadable()){
					//读  可以单独线程，也可以多线程
//					System.out.println("isReadable");
					//读就绪
					SocketChannel sc = (SocketChannel) key.channel();
					ByteBuffer b = ByteBuffer.allocate(1024);
					int len = 0;
					while((len = sc.read(b))>0){
						b.flip();
						System.out.println(new String(b.array(),0,len));
						b.clear();
					}
				}
				//取消选择键
				iterator.remove();
				String s ;
			}
		}
	}
}
