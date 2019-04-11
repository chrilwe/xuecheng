package com.xuecheng.framework.domain.search;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by admin on 2018/2/18.
 */
public class CourseSearchParam implements Serializable {
    //关键字
    String keyword;
    //一级分类
    String mt;
    //二级分类
    String st;

    //难度等级
    String grade;

    //价格区间
    Float price_min;
    Float price_max;

    //排序字段
    String sort;
    //过虑字段
    String filter;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getMt() {
		return mt;
	}
	public void setMt(String mt) {
		this.mt = mt;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Float getPrice_min() {
		return price_min;
	}
	public void setPrice_min(Float price_min) {
		this.price_min = price_min;
	}
	public Float getPrice_max() {
		return price_max;
	}
	public void setPrice_max(Float price_max) {
		this.price_max = price_max;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	@Override
	public String toString() {
		return "CourseSearchParam [keyword=" + keyword + ", mt=" + mt + ", st=" + st + ", grade=" + grade
				+ ", price_min=" + price_min + ", price_max=" + price_max + ", sort=" + sort + ", filter=" + filter
				+ "]";
	}

}
