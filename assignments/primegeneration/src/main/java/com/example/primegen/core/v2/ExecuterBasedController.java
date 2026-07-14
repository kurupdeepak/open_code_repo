package com.example.primegen.core.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.primegen.core.Controller;

@Component
@Qualifier("executor")
public class ExecuterBasedController implements Controller {

	private int min;
	
	private int max;
	
	static final int POOL_SIZE = 3;
	
	static final int GENERATOR_LIMIT = 10000;
	
	static final int WAIT_FOR_SHUTDOWN=1000;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);
	
	List<PrimeGenerator> generators;
	
	List<Integer> primes ;
	@Override
	public void execute(int min, int max) throws Exception {
		this.min = min;
		this.max = max;
		initialize();
	}
	
	private void initialize() throws Exception{
		primes = new ArrayList<>();
		generators = new ArrayList<>();
		initializeGenerators();
		displayPoolSize("Step1");
		for(int i = 0 ;i < generators.size() ; i++) {
			Future<List<Integer>> futureResult = executorService.submit(generators.get(i));
			displayPoolSize("Step2");
			primes.addAll(futureResult.get(1000,TimeUnit.MILLISECONDS));
		}
		displayPoolSize("Step3");
		executorService.awaitTermination(WAIT_FOR_SHUTDOWN, TimeUnit.MILLISECONDS);
	}
	
	private void displayPoolSize(String msg) {
		System.out.println(msg + " ->Current pool size = cur = " + getCurrentPoolSize() + ",max = "  + getMaxPoolSize() + ", active = " + getActiveCount());
	}
	
	void initializeGenerators() {
		int start = min < 2?2:min;
		
		int next = min + GENERATOR_LIMIT ;
		
		while(next <= max) {
			PrimeGenerator pg = new PrimeGenerator(start,next);
			generators.add(pg);
			start = next + 1;
			next = next + GENERATOR_LIMIT;
		}
	}

	@Override
	public List<Integer> getPrimes() {
		return primes;
	}
	
	public int getCurrentPoolSize() {
		return getExecutor().getPoolSize();
	}
	
	public int getActiveCount() {
		return getExecutor().getActiveCount();
	}
	
	public int getMaxPoolSize() {
		return getExecutor().getMaximumPoolSize();
	}
	
	
	private ThreadPoolExecutor getExecutor() {
		return ((ThreadPoolExecutor) executorService);
	}

}
