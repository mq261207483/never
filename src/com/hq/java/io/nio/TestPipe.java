package com.hq.java.io.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SourceChannel;

import org.junit.Test;

public class TestPipe {

	/**
	 * @throws IOException
	 * 两个线程间的单向数据连接
	 * 
	 * 
	 */
	@Test
	public void test1() throws IOException{
		//获取管道
		Pipe pipe = Pipe.open();
		
		//可以用两个线程分别写和读
		
		//写入通道
		Pipe.SinkChannel sc = pipe.sink();
		
		ByteBuffer b = ByteBuffer.allocate(1024);
		b.put("单向管道".getBytes());
		b.flip();
		sc.write(b);
		
		//读取
		SourceChannel source = pipe.source();
		b.flip();
		int len = source.read(b);
		System.out.println(new String(b.array(),0,len));
		
		source.close();
		sc.close();
	}
}
