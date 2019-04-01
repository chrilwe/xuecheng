package com.xuecheng.manage.media.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestSort {
	public static void main(String[] args) {
		int[] i = {1,2,5,8,3,6};
		List<Integer> list = new ArrayList<Integer>();
		for (int j = 0; j < i.length; j++) {
			list.add(i[j]);
		}
		Collections.sort(list);
		System.out.println(list);
	}
}
