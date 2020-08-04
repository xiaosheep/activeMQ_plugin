package com.aadata.activemqPlugin;

import com.aadata.activemqPlugin.plugin.DESUtil;

import cn.hutool.crypto.digest.BCrypt;

public class App 
{
    public static void main( String[] args ) 
    {
    	String password = "admin1";//$2a$10$ofPkBDUezOJp6Sik63Q/0.QlU8a1itEyzldjSXqfn2nDPqXjN0Ljm
    	
    	System.out.println(DESUtil.encryptHY(password));
    	
    	String pwt = BCrypt.hashpw(password, BCrypt.gensalt()); 
    	
    	boolean pswFlag = BCrypt.checkpw(password,"$2a$10$5zx/uoZ5aIm/seJ/Z/Tok.uLTda2Sme13XkYJGW5xEe7kOfr7K2by");//解密

    	System.out.println(pwt+"===="+pswFlag);
    	
    }
}
