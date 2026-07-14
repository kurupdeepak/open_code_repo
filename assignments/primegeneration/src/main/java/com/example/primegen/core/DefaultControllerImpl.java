package com.example.primegen.core;

import java.util.ArrayList;
import java.util.List;

public class DefaultControllerImpl implements Controller {
	static final int LIMIT = 10000;
	
	static final int SLEEP_TIME = 1000;
	
	private int index;

	private List<Integer> results = new ArrayList<>();

	private List<PrimeGenerator> generators = new ArrayList<>();

	private ControllerRuntime controllerRuntime;

	private int min;

	private int max;

	private int generatorsComplete;

	@Override
	public void execute(int min, int max) {
		initialize(min,max);
		runJob();
	}
	
	public void initialize(int min,int max){
		setControllerRuntime(ControllerRuntime.instance());
		setMin(min);
		setMax(max);
		initializeGenerators();		
	}
	
	void initializeGenerators() {
		int start = min;
		
		int next = min + LIMIT ;
		
		while(next <= max) {
			PrimeGenerator pg = createPrimeGenerator(start, next);
			generators.add(pg);
			start = next + 1;
			next = next + LIMIT;
		}
	}
	
	private void runJob() {		
		while (!checkAllGeneratorsAreCompleted()) {
			
			System.out.println("Details --" + controllerRuntime.activeCount() + ", next in Q =" + index );
			
			int i = getIndex();
			
			if(i < generators.size()) {
				int toIndex = i + ControllerRuntime.MAX_THREAD_COUNT ;
				if(toIndex >= generators.size()) {
					toIndex = generators.size();
				}
				controllerRuntime.addGenerator(generators.subList(i, toIndex));
				controllerRuntime.runGenerator();
				setIndex(toIndex);
			}
			
			try {
				System.out.println("Controller going to sleep ");
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	private boolean checkAllGeneratorsAreCompleted() {
		if(generatorsComplete == generators.size())
			return true;
		
		return checkCompleted();
	}

	private boolean checkCompleted() {
		for(int i =0;i < generators.size() ; i++) {
			if(!generators.get(i).getCurrentState().equals(GeneratorState.COMPLETED))
				return false;
		}
		generatorsComplete = generators.size();
		return true;
	}

	@SuppressWarnings("unused")
	private void printGenPrime() {
		for(int i =0;i < generators.size() ; i++)
			System.out.println("Initialized " + generators.get(i));
	}

	@Override
	public List<Integer> getPrimes(){
		for(int i = 0 ; i < generators.size() ; i++) {
			results.addAll(generators.get(0).getPrimesFound());
		}
		return results;
	}
	
	private int  getIndex() {
		return index;
	}
	
	private void setIndex(int n) {
		this.index = n;
	}
	
	private PrimeGenerator createPrimeGenerator(int s,int u) {
		return new PrimeGenerator(s,u,this);
	}

	public void notifyComplete(PrimeGenerator primeGenerator) {
		controllerRuntime.markCompleted(primeGenerator);	
		generatorsComplete ++;
	}

	
	public void setControllerRuntime(ControllerRuntime controllerRuntime) {
		this.controllerRuntime = controllerRuntime;
	}

	int getActiveCount() {
		return controllerRuntime.activeCount();
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	
	
}
