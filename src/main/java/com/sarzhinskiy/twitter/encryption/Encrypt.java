package com.sarzhinskiy.twitter.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Encrypt {
	private static final String CIPHER = "MD5";
	private static final String ENCODING = "UTF-8";
	
	public static String encryptByMDFive(String text) {
		MessageDigest msgDigest = null;
		try {
		    msgDigest = MessageDigest.getInstance(CIPHER);
		    msgDigest.reset();
		    msgDigest.update(text.getBytes(ENCODING));
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	    return new BigInteger(1, msgDigest.digest()).toString(16);
	}
	
}
