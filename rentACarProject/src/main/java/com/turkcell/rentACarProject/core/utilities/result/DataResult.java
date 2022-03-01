package com.turkcell.rentACarProject.core.utilities.result;

public class DataResult<T> extends Result{
	
	T data;
	
	public DataResult(T data, String message, boolean success) {
		super(success,message);
		this.data = data;
	}
	
	public DataResult(T data, boolean success) {
		super(success);
		this.data = data;
	}
	
	public T getData() {
		return this.data;
	}
	
}
