package com.example.primegen.core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PrimeGeneratorTest {

	Controller controller;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup(){
		controller = new DefaultControllerImpl();
	}
	
	@Test
	public void testMaxThreadSizeInRuntime() {
		ControllerRuntime cr = ControllerRuntime.instance();
		thrown.expect(MaxRuntimeSizeException.class);
		List<PrimeGenerator> lst = new ArrayList<PrimeGenerator>();
		lst.add(new PrimeGenerator(0,10000));
		lst.add(new PrimeGenerator(10001,20000));
		lst.add(new PrimeGenerator(20001,30000));
		lst.add(new PrimeGenerator(30001,40000));
		cr.addGenerator(lst);
	}

	@Test
	public void testMax10K() throws Exception {
		controller.execute(0, 10000);
		List<Integer> results = controller.getPrimes();
		System.out.println("Results = " + results);
		Assert.assertEquals(1231, results.size());
	}
	

	@Test
	public void testMax30K() throws Exception {
		controller.execute(0, 30000);
		List<Integer> results = controller.getPrimes();
		System.out.println(results);
		Assert.assertEquals(3693, results.size());
	}
	
	@Test
	public void testMax100K() throws Exception {
		controller.execute(0, 100000);
		List<Integer> results = controller.getPrimes();
		System.out.println(results);
		Assert.assertEquals(12310, results.size());
	}
}
