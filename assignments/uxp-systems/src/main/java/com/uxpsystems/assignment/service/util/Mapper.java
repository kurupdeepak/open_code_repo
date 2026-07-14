package com.uxpsystems.assignment.service.util;

public interface Mapper<E,R> {
	R fromEntity(E s);
	E toEntity(R d);
}
