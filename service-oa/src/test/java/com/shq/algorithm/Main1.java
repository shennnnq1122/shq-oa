package com.shq.algorithm;

import com.shq.model.system.SysRole;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.*;

public class Main1 {
    public static void main(String[] args) {

        try {
            String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
            System.out.println(ResourceUtils.getURL("classpath:"));
            System.out.println(ResourceUtils.getURL("classpath:").getPath());
            System.out.println(path);
            String decode = URLDecoder.decode(path, "utf-8");;
            System.out.println(decode);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }




}
