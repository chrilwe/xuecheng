package com.xuecheng.framework.domain.ucenter.ext;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by admin on 2018/3/20.
 */
public class XcMenuExt extends XcMenu {

    List<CategoryNode> children;

	public List<CategoryNode> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryNode> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "XcMenuExt [children=" + children + "]";
	}
    
}
