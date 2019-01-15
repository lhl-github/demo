package com.xljy.bean;

public class testlhl {

	private Integer id ;
	private String test_01 ;
	
	
	
	
	public testlhl(String test_01) {
		super();
		this.test_01 = test_01;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTest_01() {
		return test_01;
	}
	public void setTest_01(String test_01) {
		this.test_01 = test_01;
	}

	@Override
	public String toString() {
		return "testlhl [Id=" + id + ", test_01=" + test_01 + "]";
	}
	
} 
