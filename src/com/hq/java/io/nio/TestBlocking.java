package com.hq.java.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * @author Administrator
 *非阻塞式IO只能网络io，   file不能非阻塞。
 * 一 使用nio网络通讯 三核心
 * 		1 通道 
 * 	java.nio.channels.Channel接口：
 * 		|--SelectableChannel
	 * 			|--SocketChannel              tcp
	 * 			|--ServerSocketChannel		  tcp
	 * 			|--DatagramChannel		      udp
 * 			
 * 			Pipe.SinkChannel
 * 			Pipe.SourceChannel
 * 		2缓冲区
 * 		3选择器   是 SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况
 */
public class TestBlocking {

	//阻塞式 客户端
	@Test
	public void client() throws IOException{
		SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
		ByteBuffer b = ByteBuffer.allocate(1024);
		
		FileChannel fc = FileChannel.open(Paths.get("todo.png"), StandardOpenOption.READ);
		while(fc.read(b)!=-1){
			b.flip();
			sc.write(b);
			b.clear();
		}
		
		//关闭通道
		fc.close();
		sc.close();
	}
	
	//阻塞式 服务端
	@Test 
	public void server() throws IOException{
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress(8888));
		SocketChannel accept = ssc.accept();
		
		FileChannel fc = FileChannel.open(Paths.get("2.png"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		ByteBuffer b = ByteBuffer.allocate(1024);
		while(accept.read(b)!=-1){
			b.flip();
			fc.write(b);
			b.clear();
		}
		
		fc.close();
		accept.close();
		ssc.close();
	}
	
	@Test
	public void clien2() throws IOException{
		SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1",8888));
		ByteBuffer b = ByteBuffer.allocate(1024);
		FileChannel fc = FileChannel.open(Paths.get("todo.png"), StandardOpenOption.READ);
		while(fc.read(b)!=-1){
			b.flip();
			sc.write(b);
			b.clear();
		}
		
		//通知服务端完毕
		sc.shutdownOutput();
		
		//接收反馈
		int len = 0;
		while((len=sc.read(b))!=-1){
			b.flip();
			System.out.println(new String(b.array(),0,len));
			b.clear();
		}
		
		fc.close();
		sc.close();
	}
	
	@Test
	public void server2() throws IOException{
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress(8888));
		
		SocketChannel accept = ssc.accept();
		
		ByteBuffer b = ByteBuffer.allocate(1024);
		FileChannel fc = FileChannel.open(Paths.get("3.png"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		while(accept.read(b)!=-1){
			b.flip();
			fc.write(b);
			b.clear();
		}
		
		//发送反馈
		b.put("server接受成功".getBytes());
		b.flip();
		accept.write(b);
		
		fc.close();
		accept.close();
		ssc.close();
	}
}
