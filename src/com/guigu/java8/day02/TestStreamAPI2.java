package com.guigu.java8.day02;

import com.atguigu.java8.day02.Employee.Status;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * 一、 Stream 的操作步骤
 * 
 * 1. 创建 Stream
 * 
 * 2. 中间操作
 * 
 * 3. 终止操作
 */

//3. 终止操作
/*
	allMatch——检查是否匹配所有元素
	anyMatch——检查是否至少匹配一个元素
	noneMatch——检查是否没有匹配的元素
	findFirst——返回第一个元素
	findAny——返回当前流中的任意元素
	count——返回流中元素的总个数
	max——返回流中最大值
	min——返回流中最小值
 */
public class TestStreamAPI2 {

	List<Employee> emps = Arrays.asList(
			new Employee(102, "李四", 59, 6666.66, Status.BUSY),
			new Employee(101, "张三", 18, 9999.99, Status.FREE),
			new Employee(103, "王五", 28, 3333.33, Status.VOCATION),
			new Employee(104, "赵六", 8, 7777.77, Status.BUSY),
			new Employee(104, "赵六", 8, 7777.77, Status.FREE),
			new Employee(104, "赵六", 8, 7777.77, Status.FREE),
			new Employee(105, "田七", 38, 5555.55, Status.BUSY)
	);

	@Test
	public void test1(){
			boolean bl = emps.stream()
				.allMatch((e) -> e.getStatus().equals(Status.BUSY));
			
			System.out.println(bl);
			
			boolean bl1 = emps.stream()
				.anyMatch((e) -> e.getStatus().equals(Status.BUSY));
			
			System.out.println(bl1);
			
			boolean bl2 = emps.stream()
				.noneMatch((e) -> e.getStatus().equals(Status.BUSY));
			
			System.out.println(bl2);
	}
	
	@Test
	public void test2(){
		Optional<Employee> op = emps.stream()
			.sorted((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()))
			.findFirst();
		System.out.println(op.get());
		System.out.println("--------------------------------");
		Optional<Employee> op2 = emps.parallelStream()
			.filter((e) -> e.getStatus().equals(Status.FREE))
			.findAny();
		System.out.println(op2.get());
	}
	
	@Test
	public void test3(){
		long count = emps.stream()
						 .filter((e) -> e.getStatus().equals(Status.FREE))
						 .count();
		System.out.println(count);
		Optional<Double> op = emps.stream()
			.map(Employee::getSalary)
			.max(Double::compare);
		System.out.println(op.get());
		Optional<Employee> op2 = emps.stream()
			.min((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
		System.out.println(op2.get());
	}
	
	//注意：流进行了终止操作后，不能再次使用
	@Test
	public void test4(){
		Stream<Employee> stream = emps.stream()
		 .filter((e) -> e.getStatus().equals(Status.FREE));
		long count = stream.count();
		stream.map(Employee::getSalary)
			.max(Double::compare);
	}

	@Test
	public void test5(){
		List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		Integer sum = list.stream()
				.reduce(0, (x, y) -> x + y);
		System.out.println(sum);
		System.out.println("----------------------------------------");
		Optional<Double> op = emps.stream()
				.map(Employee::getSalary)
				.reduce(Double::sum);
		System.out.println(op.get());
	}

	//需求：搜索名字中 “六” 出现的次数
	@Test
	public void test6(){
		Optional<Integer> sum = emps.stream()
				.map(Employee::getName)
				.flatMap(TestStreamAPI1::filterCharacter)
				.map((ch) -> {
					if(ch.equals('六'))
						return 1;
					else
						return 0;
				}).reduce(Integer::sum);

		System.out.println(sum.get());
	}

	//collect——将流转换为其他形式。接收一个 Collector接口的实现，用于给Stream中元素做汇总的方法
	@Test
	public void test7(){
		List<String> list = emps.stream()
				.map(Employee::getName)
				.collect(Collectors.toList());

		list.forEach(System.out::println);

		System.out.println("----------------------------------");

		Set<String> set = emps.stream()
				.map(Employee::getName)
				.collect(Collectors.toSet());

		set.forEach(System.out::println);

		System.out.println("----------------------------------");

		HashSet<String> hs = emps.stream()
				.map(Employee::getName)
				.collect(Collectors.toCollection(HashSet::new));

		hs.forEach(System.out::println);
	}

	@Test
	public void test8(){
		Optional<Double> max = emps.stream()
				.map(Employee::getSalary)
				.collect(Collectors.maxBy(Double::compare));

		System.out.println(max.get());

		Optional<Employee> op = emps.stream()
				.collect(Collectors.minBy((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())));

		System.out.println(op.get());

		Double sum = emps.stream()
				.collect(Collectors.summingDouble(Employee::getSalary));

		System.out.println(sum);

		Double avg = emps.stream()
				.collect(Collectors.averagingDouble(Employee::getSalary));

		System.out.println(avg);

		Long count = emps.stream()
				.collect(Collectors.counting());

		System.out.println(count);

		System.out.println("--------------------------------------------");

		DoubleSummaryStatistics dss = emps.stream()
				.collect(Collectors.summarizingDouble(Employee::getSalary));

		System.out.println(dss.getMax());
	}

	//分组
	@Test
	public void test9(){
		Map<Status, List<Employee>> map = emps.stream()
				.collect(Collectors.groupingBy(Employee::getStatus));
		System.out.println(map);
	}

	//多级分组
	@Test
	public void test10(){
		Map<Status, Map<String, List<Employee>>> map = emps.stream()
				.collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
					if(e.getAge() >= 60)
						return "老年";
					else if(e.getAge() >= 35)
						return "中年";
					else
						return "成年";
				})));
		System.out.println(map);
	}

	//分区
	@Test
	public void test11(){
		Map<Boolean, List<Employee>> map = emps.stream()
				.collect(Collectors.partitioningBy((e) -> e.getSalary() >= 5000));
		System.out.println(map);
	}

	@Test
	public void test12(){
		String str = emps.stream()
				.map(Employee::getName)
				.collect(Collectors.joining("," , "----", "----"));
		System.out.println(str);
	}

	@Test
	public void test13(){
		Optional<Double> sum = emps.stream()
				.map(Employee::getSalary)
				.collect(Collectors.reducing(Double::sum));
		System.out.println(sum.get());
	}

}
