package com.hq.java.lambda;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Java8Test {
	public static void main(String[] args) {
		// 计算空字符串
	      List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
	      System.out.println("列表: " +strings);
	      long count = strings.stream().filter(s -> s.isEmpty()).count();
	      System.out.println("空字符数量为: " + count);
	        
	      count = strings.stream().filter(s -> s.length() == 3).count();
	      System.out.println("字符串长度为 3 的数量为: " + count);
	        
	      // 删除空字符串
	      List<String> filtered = strings.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
	      System.out.println("筛选后的列表: " + filtered);
	        
	      // 删除空字符串，并使用逗号把它们合并起来
	      String mergedString = strings.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(", "));
	      System.out.println("合并字符串: " + mergedString);
	      List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
	        
	      // 获取列表元素平方数
	      List<Integer> squaresList = numbers.stream().map(i -> i*i).distinct().collect(Collectors.toList());
	      System.out.println("平方数列表: " + squaresList);
	      List<Integer> integers = Arrays.asList(1,2,13,4,15,6,17,8,19);
	        
	      IntSummaryStatistics summaryStatistics = numbers.stream().mapToInt(s -> s).summaryStatistics();
	      System.out.println("列表: " +integers);
	      System.out.println("列表中最大的数 : " + summaryStatistics.getMax());
	      System.out.println("列表中最小的数 : " +summaryStatistics.getMin());
	      System.out.println("所有数之和 : " + summaryStatistics.getSum());
	      System.out.println("平均数 : " + summaryStatistics.getAverage());
	      System.out.println("随机数: ");
	        
	      // 输出10个随机数
	      Random random = new Random();
	      random.ints().limit(10).sorted().forEach(System.out::println);
	}
}
