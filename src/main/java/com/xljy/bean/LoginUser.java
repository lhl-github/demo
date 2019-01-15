package com.xljy.bean;

public class LoginUser {
	private Integer id;
	private Object juese;
	private Object caidan;
	private Object user;

	public LoginUser() {
		super();
	}

	public LoginUser(Integer id,Object user, Object juese, Object caidan) {
		super();
		this.id = id;
		this.user = user;
		this.juese = juese;
		this.caidan = caidan;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public Object getJuese() {
		return juese;
	}

	public void setJuese(Object juese) {
		this.juese = juese;
	}

	public Object getCaidan() {
		return caidan;
	}

	public void setCaidan(Object caidan) {
		this.caidan = caidan;
	}

}