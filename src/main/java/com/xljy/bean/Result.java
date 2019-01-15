package com.xljy.bean;

public class Result {

	private static final long serialVersionUID = -7074012067378557866L;

	/** 返回结果集 */
	private Object data;
	/** 返回结果集 2 */
	private Object data2;
	/** 返回结果集 2 */
	private Object data3;

	/** 分页信息 */
	private Object pageInfo;

	/** 成功失败 */
	private boolean success;

	/** 信息 */
	private String message;

	/** 状态码 */
	private Integer status;

	/** 前端弹窗模式："warning", "error", "success", "info" */
	private String icon;
	/**  防止二次登录*/
	private String Token;

	public Result() {
	}

	public Result(boolean success, String message) {
		this.success = success;
		this.message = message;
		if (success)
			icon = "success";
		else
			icon = "error";
	}

	public Result(boolean success, Integer code, String message) {
		this(success, message);
		this.status = code;
	}

	public Result(boolean success, Integer code, Object data, String message) {
		this(success, code, message);
		this.data = data;
	}

	public static Result result(Object data, boolean success, Integer code, String message) {
		Result result = new Result();
		result.data = data;
		result.success = success;
		result.message = message;
		result.status = code;
		return result;
	}

	public static Result result(Object data, Object pageInfo, boolean success, Integer code, String message) {
		Result result = new Result();
		result.pageInfo = pageInfo;
		result.data = data;
		result.success = success;
		result.message = message;
		result.status = code;
		return result;
	}

	// 返回成功结果
	public static Result resultSuccess(Object data, String message) {
		Result result = new Result();
		result.data = data;
		result.success = true;
		result.status = 200;
		result.message = message;
		return result;
	}

	// 返回失败
	public static Result resultError(String message) {
		Result result = new Result();
		result.success = false;
		result.status = 500;
		result.message = message;
		return result;
	}
	
	public static Result resultError(int code, String message) {
		Result result = new Result();
		result.success = false;
		result.status = code;
		result.message = message;
		return result;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return status;
	}

	public void setCode(Integer code) {
		this.status = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Object getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(Object pageInfo) {
		this.pageInfo = pageInfo;
	}

	public Object getData2() {
		return data2;
	}

	public void setData2(Object data2) {
		this.data2 = data2;
	}

	public Object getData3() {
		return data3;
	}

	public void setData3(Object data3) {
		this.data3 = data3;
	}
	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		this.Token = token;
	}
}
