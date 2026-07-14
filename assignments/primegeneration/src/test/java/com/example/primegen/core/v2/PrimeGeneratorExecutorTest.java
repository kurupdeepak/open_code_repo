package com.example.primegen.core.v2;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.primegen.core.Controller;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimeGeneratorExecutorTest {

	@Autowired
	@Qualifier("executor")
	Controller controller;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup(){
//		controller = new ExecuterBasedController();
	}
	
	@Test
	public void testMaxThreadSizeInRuntime() {
		int size = ((ExecuterBasedController) controller).getMaxPoolSize();
		System.out.println("The size is " + size);
		Assert.assertEquals(ExecuterBasedController.POOL_SIZE,size);
	}

	@Test
	public void testMax10K() {
		try {
			controller.execute(0, 10000);
			List<Integer> results = controller.getPrimes();
			System.out.println("Results = " + results);
			Assert.assertEquals(1229, results.size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
	}
	

	@Test
	public void testMax30K() {
		try {
			controller.execute(0, 30000);
			List<Integer> results = controller.getPrimes();
			System.out.println("Results = " + results);
			Assert.assertEquals(3245, results.size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
;
	}
	
	@Test
	public void testMax100K() {
		try {
			controller.execute(0, 100000);
			List<Integer> results = controller.getPrimes();
			System.out.println("Results = " + results);
			Assert.assertEquals(9592, results.size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}

	}
}
