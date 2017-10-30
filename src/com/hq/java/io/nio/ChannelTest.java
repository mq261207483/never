package com.hq.java.io.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.junit.Test;

/**
 * @author Administrator
 * 3 获取通道
 *	jdk1.7 中的 NIO.2 针对各个通道提供了静态方法 open
 *	jdk1.7 中的 NIO.2 Files工具类 newByteChannel()
 *	
 * 4 通道之间数据传输
 * 
 * 5 分散scatter 聚集 gather
 * 分散  将通道中的数据分散到多个缓冲区中 
 * 聚集 将多个缓冲中的数据聚集到通道中
 * 
 * 6字符集 Charset
 * 编码：
 * 解码：
 */
public class ChannelTest {
//非直接缓冲区
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
	//直接缓冲区        效率高，但是内存被占用，资源可能长时间未被释放
	@Test
	public void test2(){
		try {
			FileChannel inc = FileChannel.open(Paths.get("todo.png"), StandardOpenOption.READ);
			FileChannel outc = FileChannel.open(Paths.get("todo2.png"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
			//内存映射文件
			MappedByteBuffer inMap = inc.map(MapMode.READ_ONLY, 0, inc.size());
			MappedByteBuffer outMap = outc.map(MapMode.READ_WRITE, 0, inc.size());
			
			//直接对缓冲区数据操作
			byte[] b = new byte[inMap.limit()];
			inMap.get(b);
			outMap.put(b);
			
			outc.close();
			inc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		}
		
		
	}
	
	/**
	 * 通道之间数据传输    直接缓冲区
	 */
	@Test
	public void test3(){
		try {
			FileChannel inc = FileChannel.open(Paths.get("todo.png"), StandardOpenOption.READ);
			FileChannel outc = FileChannel.open(Paths.get("todo2.png"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
			
//			inc.transferTo(0, inc.size(), outc);
			outc.transferFrom(inc, 0, inc.size());
			
			outc.close();
			inc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		}
		
		
	}
	
	@Test
	public void test4() throws IOException{
		//分散读取
		RandomAccessFile r = new RandomAccessFile("1.txt", "rw");
		FileChannel channel = r.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(3);
		ByteBuffer buf1 = ByteBuffer.allocate(1024);
		
		ByteBuffer[] bs = {buf,buf1};
		channel.read(bs);
		
		for (ByteBuffer b : bs) {
			b.flip();
			System.out.println(new String(b.array(),0,b.limit()));
		}
		
		//聚集写入
		RandomAccessFile r2 = new RandomAccessFile("2.txt", "rw");
		FileChannel channel2 = r2.getChannel();
		
		channel2.write( bs);
	}
	
	@Test
	public void test5() throws CharacterCodingException{
		SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();
		for (Entry en : availableCharsets.entrySet()) {
//			System.out.println(en.getKey());
		}
		
		Charset cs = Charset.forName("GBK");
		CharsetDecoder cd = cs.newDecoder();
		CharsetEncoder ce = cs.newEncoder();
		
		CharBuffer cb = CharBuffer.allocate(1024);
		
		cb.put("阿萨德");
		cb.flip();
		
		ByteBuffer encode = ce.encode(cb);
		for (int i = 0; i <6; i++) {//一个汉字两个字节 ？ 
			System.out.println(encode.get());
		}
		
		encode.flip();
		CharBuffer decode = cd.decode(encode);
		System.out.println(decode.toString());
		
		
//		Charset latin1 = Charset.forName( "ISO-8859-1" ); 
//		CharsetDecoder decoder = latin1.newDecoder();  
//		CharsetEncoder encoder = latin1.newEncoder(); 
//		ByteBuffer buffer =  ByteBuffer.allocate(1024);
//		CharBuffer cb = decoder.decode( buffer ); 
//		ByteBuffer outputData = encoder.encode( cb ); 
	}
	
	
}
