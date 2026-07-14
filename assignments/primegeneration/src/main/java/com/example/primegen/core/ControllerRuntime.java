package com.example.primegen.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class ControllerRuntime {
	static int MAX_THREAD_COUNT = 3;
	
	private Map<Integer,PrimeGenerator> runtime = new HashMap<>();

	private int activeCount;
	
	private static ControllerRuntime controllerRuntime;

	private ControllerRuntime() {
	}

	int activeCount() {
		return runtime.size();
	}

	boolean isInitialized() {
		return runtime.size() != 0;
	}

	void addGenerator(PrimeGenerator pgObj) {
		if (activeCount < MAX_THREAD_COUNT && activeCount != 0) {
			runtime.put(pgObj.getGeneratorId(),pgObj);
		}else
			throw new MaxRuntimeSizeException("Maximum size " + activeCount );
	}

	void addGenerator(List<PrimeGenerator> pgObjLst) {
		if (activeCount < MAX_THREAD_COUNT &&  (activeCount + pgObjLst.size()) <= MAX_THREAD_COUNT) {
			Iterator<PrimeGenerator> iter = pgObjLst.iterator();
			while(iter.hasNext()) {
				PrimeGenerator pgObj = iter.next();
				runtime.put(pgObj.getGeneratorId(), pgObj);
			}
		}else
			throw new MaxRuntimeSizeException("maximum size " + (activeCount + pgObjLst.size()));
	}
	
	void runGenerator() {
		for(PrimeGenerator pgObj : runtime.values()) {
			pgObj.setGeneratorState(GeneratorState.START);
			pgObj.start();
		}
	}

	void markCompleted(PrimeGenerator primeGenerator) {
		activeCount --;
		runtime.remove(primeGenerator.getGeneratorId());
	}

	public static ControllerRuntime instance() {
		if(controllerRuntime == null)
		return new ControllerRuntime();
		return controllerRuntime;
	}
}
