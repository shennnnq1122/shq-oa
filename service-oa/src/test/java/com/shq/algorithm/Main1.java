package com.shq.algorithm;


import com.shq.common.jwt.JwtHelper;
import com.shq.model.system.SysDept;
import org.apache.logging.log4j.spi.CopyOnWrite;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;


//import java.util.ArrayList;

public class Main1 {

    public static void add(int x, List<Integer> a1){
        a1.add(x);
    }

    public static void main(String[] args) {

        String token = JwtHelper.createToken(4L, "lisi");
        System.out.println(token);
        String username = JwtHelper.getUsername(token);
        Long userId = JwtHelper.getUserId(token);

        System.out.println("username = " + username);
        System.out.println("userId = " + userId);

















    }



}
