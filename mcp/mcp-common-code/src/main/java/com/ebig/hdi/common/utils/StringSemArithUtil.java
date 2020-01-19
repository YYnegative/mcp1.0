package com.ebig.hdi.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringSemArithUtil {

	/**
	 * 我们把两个字符串的相似度定义为：将一个字符串转换成另外一个字符串的代价（转换的方法可能不唯一），转换的代价越高则说明两个字符串的相似度越低。
	 * 比如两个字符串：“SNOWY”和“SUNNY”，下面给出两种将“SNOWY”转换成“SUNNY”的方法： 变换1： S - N O W Y S U
	 * N N - Y Cost = 3 （插入U、替换O、删除W） 变换2： - S N O W - Y S U N - - N Y Cost = 5
	 * （插入S、替换S、删除O、删除W、插入N） 用d[i,
	 * j]表示source[1..i]到target[1..j]之间的最小编辑距离，则计算d[i, j]的递推关系可以这样计算出来：
	 * 如果source[i] 等于target[j]，则： d[i, j] = d[i, j] + 0 （递推式 1） 如果source[i]
	 * 不等于target[j]，则根据插入、删除和替换三个策略，分别计算出使用三种策略得到的编辑距离，然后取最小的一个： d[i, j] =
	 * min(d[i, j - 1] + 1，d[i - 1, j] + 1，d[i - 1, j - 1] + 1 ) （递推式 2） d[i, j
	 * - 1] + 1 表示对source[i]执行插入操作后计算最小编辑距离 d[i - 1, j] + 1
	 * 表示对source[i]执行删除操作后计算最小编辑距离 d[i - 1, j - 1] +
	 * 1表示对source[i]替换成target[i]操作后计算最小编辑距离
	 *
	 * @param args
	 */
	public static double getSemblance(String source, String target) {
		source = source == null ? "" : source;
		target = target == null ? "" : target;
		char[] s = source.toCharArray();
		char[] t = target.toCharArray();
		int slen = source.length();
		int tlen = target.length();
		int [][] d = new int[slen + 1][tlen + 1];
		for (int i = 0; i <= slen; i++) {
			d[i][0] = i;
		}
		for (int i = 0; i <= tlen; i++) {
			d[0][i] = i;
		}
		for (int i = 1; i <= slen; i++) {
			for (int j = 1; j <= tlen; j++) {
				if (s[i - 1] == t[j - 1]) {
					d[i][j] = d[i - 1][j - 1];
				} else {
					int insert = d[i][j - 1] + 1;
					int del = d[i - 1][j] + 1;
					int update = d[i - 1][j - 1] + 1;
					d[i][j] = Math.min(insert, del) > Math.min(del, update) ? Math.min(del, update)
							: Math.min(insert, del);
				}
			}
		}
		double len = d[slen][tlen];
		double semblance = 1 - len / Math.max(source.length(), target.length());
		return BigDecimal.valueOf(semblance).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
}
