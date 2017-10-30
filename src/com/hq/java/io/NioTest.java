package com.hq.java.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class NioTest {
	
	public static void main(String[] args) throws IOException {
		// read();

		// write();

		// copyFile();

		// 直接和间接缓冲区:
		// 给定一个直接字节缓冲区，Java 虚拟机将尽最大努力直接对它执行本机 I/O 操作。也就是说，它会在每一次调用底层操作系统的本机 I/O
		// 操作之前(或之后)，尝试避免将缓冲区的内容拷贝到一个中间缓冲区中(或者从一个中间缓冲区中拷贝数据)。

		// 内存映射文件 I/O
		// 是通过使文件中的数据神奇般地出现为内存数组的内容来完成的。
		RAMmap();

		// 分散/聚集 I/O
		// long read( ByteBuffer[] dsts );
		// long read( ByteBuffer[] dsts, int offset, int length );
		// long write( ByteBuffer[] srcs );
		// long write( ByteBuffer[] srcs, int offset, int length );

		// 锁定文件
		// RandomAccessFile raf = new RandomAccessFile( "usefilelocks.txt", "rw"
		// );
		// FileChannel fc = raf.getChannel();
		// FileLock lock = fc.lock( start, end, false );
		// 释放锁
		// lock.release();
		
		
		
		/* * 这个程序有点过于简单，因为它的目的只是展示异步 I/O 所涉及的技术。在现实的应用程序中，您需要通过将通道从 Selector
		 * 中删除来处理关闭的通道。而且您可能要使用多个线程。这个程序可以仅使用一个线程，因为它只是一个演示，但是在现实场景中，创建一个线程池来负责
		 * I/O 事件处理中的耗时部分会更有意义。*/
		 
		//连网和异步 I/O
//		它能同时监听多个端口，并处理来自所有这些端口的连接。并且它只在单个线程中完成所有这些工作
		Selector selector = Selector.open(); 
		ServerSocketChannel ssc = ServerSocketChannel.open();  
		ssc.configureBlocking( false );   //非阻塞的
		ServerSocket ss = ssc.socket();  
		int ports  = 0;
		InetSocketAddress address = new InetSocketAddress( ports );  
		ss.bind( address ); 
		//将新打开的 ServerSocketChannels 注册到 Selector上,指定我们想要监听 accept 事件(也就是在新的连接建立时所发生的事件)
		SelectionKey key1 = ssc.register( selector, SelectionKey.OP_ACCEPT ); 
		
		//这个方法会阻塞，直到至少有一个已注册的事件发生
		//当一个或者更多的事件发生时，select() 方法将返回所发生的事件的数量。
		int select = selector.select();
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Iterator<SelectionKey> it = selectedKeys.iterator();
		while(it.hasNext()){
			SelectionKey key = it.next();
			 // ... deal with I/O event ...} 
			
			//监听新连接
			if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
				// Accept the new connection
				System.out.println(key.readyOps());
				// ...
				//接受新的连接
				ServerSocketChannel sscNew = (ServerSocketChannel)key.channel();  
				SocketChannel sc = sscNew.accept(); 
				// 将新连接的 SocketChannel 配置为非阻塞的。而且由于接受这个连接的目的是为了读取来自套接字的数据，
				//所以我们还必须将 SocketChannel 注册到 Selector上
				sc.configureBlocking( false ); 
				//OP_READ 读取
				SelectionKey newKey = sc.register( selector, SelectionKey.OP_READ ); 
				
				//删除处理过的 SelectionKey
				it.remove();
			} else if((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ){
				// Read the data  
				SocketChannel  sc = (SocketChannel)key.channel();
				
			}
		}
		
		//字符集 编码解码
		Charset latin1 = Charset.forName( "ISO-8859-1" ); 
		CharsetDecoder decoder = latin1.newDecoder();  
		CharsetEncoder encoder = latin1.newEncoder(); 
		ByteBuffer buffer =  ByteBuffer.allocate(1024);
		CharBuffer cb = decoder.decode( buffer ); 
		ByteBuffer outputData = encoder.encode( cb ); 
		
	}

	private static void RAMmap() throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream("E:\\文档\\资料\\数据库连接串.txt");
		FileChannel fc = fis.getChannel();
		MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
	}

	private static void copyFile() throws FileNotFoundException, IOException {
		FileChannel fcout = null;
		FileChannel fcin = null;
		try {
			FileInputStream fis = new FileInputStream("E:\\文档\\资料\\数据库连接串.txt");
			FileOutputStream fos = new FileOutputStream("E:\\testmq.txt");
			fcout = fos.getChannel();
			fcin = fis.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			//
			while (true) {
				buffer.clear();
				if (fcin.read(buffer) == -1) {
					break;
				}
				buffer.flip();
				fcout.write(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fcout != null){
				fcout.close();
			}
			if(fcin != null){
				fcin.close();
			}
		}
	}

	private static void write() throws FileNotFoundException, IOException {
		FileOutputStream fo = new FileOutputStream("E:\\testmq.txt");
		FileChannel cout = fo.getChannel();
		ByteBuffer bout = ByteBuffer.allocate(1024);
		String s = new String("Some bytes");
		byte[] bytes = s.getBytes();
		bout.put(bytes);
		bout.flip();
		cout.write(bout);
	}

	private static void read() throws FileNotFoundException, IOException {
		FileInputStream fs = new FileInputStream("E:\\文档\\资料\\数据库连接串.txt");
		FileChannel channel = fs.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int read = channel.read(buffer);
		System.out.print(read);
		
	}
	
	@Test
	public  void test(){
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());
		System.out.println(buffer.position());
		
		String s = "abcde";
		buffer.put(s.getBytes());
		
		System.out.println("======put======");
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());//1024
		System.out.println(buffer.position());//5
		
		
		System.out.println("=====flip=======");
		buffer.flip();
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());//5
		System.out.println(buffer.position());//0
		
		
		System.out.println("======get======");
		System.out.println(buffer.get(new byte[buffer.limit()]));
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());//5
		System.out.println(buffer.position());//5
		
		
//		System.out.println("======put=flip=====");
//		String s1 = "uiop";
//		buffer.put(s1.getBytes());
//		buffer.flip();
//		
//		System.out.println("======get======");
//		System.out.println(buffer.get(new byte[buffer.limit()]));
//		System.out.println(buffer.capacity());
//		System.out.println(buffer.limit());
//		System.out.println(buffer.position());
		
		System.out.println("======rewind重复读======");
		buffer.rewind();
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());//5
		System.out.println(buffer.position());//0
		
		
		
		System.out.println("======clear 清空缓冲区，但是数据存在，处于“被遗忘”状态======");
		buffer.clear();
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());//1024
		System.out.println(buffer.position());//0
	}
	
	@Test
	public void test2(){
		//mark 记录当前位置  
		//reset 回复mark的位置
 	}
	
	@Test
	public void test3(){
		//直接缓冲区  非直接缓冲区
		System.out.println("1");
		ByteBuffer direct = ByteBuffer.allocateDirect(1024);
		
	}

}
