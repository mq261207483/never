package com.hq.test.mapper;   

import org.apache.ibatis.annotations.Param;

/**     
 * Description: TODO  
 *
 * ClassName: IHelloDao 
 * date: 2016年5月24日 下午4:19:10    
 *   
 * @author mengqiang 
 * @version    
 * @since JDK 1.8 
 * Copyright (c) 2016, o2o-lottery All Rights Reserved.     
 */
public interface HelloMapper {

	/**   
	 * function: 
	 *   
	 * @param string   
	 * @since JDK 1.8   
	 */
	int sayHello(@Param("string") String string);

}
   
