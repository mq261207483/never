package com.hq.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hq.test.mapper.HelloMapper;
import com.hq.test.service.IHelloService;

/**
 * Description: TODO
 *
 * ClassName: HelloServiceImpl date: 2016年5月24日 下午4:17:38
 * 
 * @author mengqiang
 * @version
 * @since JDK 1.8 Copyright (c) 2016, o2o-lottery All Rights Reserved.
 */
@Transactional
@Service("helloService")
public class HelloServiceImpl implements IHelloService {

	@Autowired
	private HelloMapper helloDao;

	/**   
	 * fuction:  
	 * @param string
	 * @return  
	 */
	public int sayHello(String string) {
		  
		return helloDao.sayHello(string);
	}


}
