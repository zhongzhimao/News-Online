package cn.web.news.entity;

import java.util.List;

/**
 * ajax响应回客户端的对象(json对象)
 * @author gec
 *
 */
public class Result {
	
	private String msg;
	private List datas;
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List getDatas() {
		return datas;
	}
	public void setDatas(List datas) {
		this.datas = datas;
	}
	
	
}
