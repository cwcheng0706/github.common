package com.zy.halfbytechinese;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 判断一个字节数组最后面有没有半个汉字的
 * 
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年8月28日 上午11:44:00
 */
public class StringSpiltByByte {

	/**
	 * 判断结尾是否包含半个字节的汉字
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月28日 下午1:26:37
	 * @param str
	 * @param len
	 * @return
	 */
	private String subStringFirstHalf(String str, int len) {

		byte[] b = str.getBytes();

		String temp = "";

		if (len == 1) {// 当只取1位时
			if (b[0] < 0)
				temp = new String(b, 0, 2);
			else
				temp = new String(b, 0, len);
		} else {

			if (b[len - 1] < 0 && b[len - 2] > 0) { // 判断最后一个字节是否为一个汉字的第一个字节

				// System.out.println(len + "->" + b[len-1] + " " + b[len-2] +
				// " " + b[len-3]);
				temp = new String(b, 0, len - 1);
			} else if (b[len - 2] < 0 && b[len - 3] > 0) {

				// System.out.println(len + "->" + b[len-1] + " " + b[len-2] +
				// " " + b[len-3]);
				temp = new String(b, 0, len - 2);
			} else { // 正常截取
				temp = new String(b, 0, len);
			}

			System.out.println(len + "-> " + b[len - 3] + " " + b[len - 2] + " " + b[len - 1]);
		}
		return temp;
	}

	/**
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月28日 下午1:28:51
	 * @param str
	 * @param len
	 * @return
	 */
	private String subStringLatterHalf(String str, int start) {

		byte[] b = str.getBytes();

		String temp = "";

		int len = b.length;

		System.out.println(b[0] + "  " + 0xa0);

		if (b[1] < 0 && b[2] > 0) { // 判断第一个字节是否为一个汉字的第一个字节

			System.out.println(len + "->" + b[len - 1] + " " + b[len - 2] + " " + b[len - 3]);
			temp = new String(b, 0, len - 1);
		} else if (b[len - 2] < 0 && b[len - 3] > 0) {

		}

		byte[] bb = new byte[len - start];
		for (int i = 0; i < b.length; i++) {
			if (start <= i) {
				bb[i - start] = b[i];
			}
		}
		System.out.println("start:" + start + " 【0=" + bb[0] + " 1=" + bb[1] + " 2=" + bb[2] + "】");

		return temp;
	}

	public static void main(String[] args) throws Exception {

		byte[] head = new byte[40];
		InputStream inputStream = new FileInputStream("c:/split/a.log");

		System.out.println(isUTF8(head));

	}

	public static boolean getByteEncode(byte[] b) {
		if (b != null && b.length > 3) {
			byte utf8[] = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
			if ((b[0] == utf8[0]) && (b[1] == utf8[1]) && (b[2] == utf8[2]))
				return true;
		}
		return false;
	}

	public static boolean isUTF8(byte[] rawtext) {
		int score = 0;
		int i, rawtextlen = 0;
		int goodbytes = 0, asciibytes = 0;
		// Maybe also use UTF8 Byte Order Mark: EF BB BF
		// Check to see if characters fit into acceptable ranges
		rawtextlen = rawtext.length;
		for (i = 0; i < rawtextlen; i++) {
			if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) {
				// 最高位是0的ASCII字符
				asciibytes++;
				// Ignore ASCII, can throw off count
			} else if (-64 <= rawtext[i] && rawtext[i] <= -33
			// -0x40~-0x21
					&& // Two bytes
					i + 1 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65) {
				goodbytes += 2;
				i++;
			} else if (-32 <= rawtext[i] && rawtext[i] <= -17
					&& // Three bytes
					i + 2 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65 && -128 <= rawtext[i + 2]
					&& rawtext[i + 2] <= -65) {
				goodbytes += 3;
				i += 2;
			}
		}
		if (asciibytes == rawtextlen) {
			return false;
		}
		score = 100 * goodbytes / (rawtextlen - asciibytes);
		// If not above 98, reduce to zero to prevent coincidental matches
		// Allows for some (few) bad formed sequences
		if (score > 98) {
			return true;
		} else if (score > 95 && goodbytes > 30) {
			return true;
		} else {
			return false;
		}

	}
}
