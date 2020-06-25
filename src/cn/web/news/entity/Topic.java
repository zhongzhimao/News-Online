package cn.web.news.entity;

import java.util.ArrayList;
import java.util.List;

/*
 * 主题 实体类   主题  one  --> 新闻   many
 * @author
 */
public class Topic {
	private int tid;
	private String tname;
	
	private List<News> newsList = new ArrayList<News>();

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public List<News> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}

}
