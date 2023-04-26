package com.shq.algorithm;

import com.shq.model.system.SysRole;

import java.math.BigInteger;
import java.util.*;

public class Main1 {
    public   void main(String[] args) {
        // TODO 算法
        int n =2 ;
        int[][] edges = {{0,1}};
        int[] price = {4,8};
        int[][] trips = {{0,0}};


    }

    public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {
        int n=nums.length;
        int[] pre = new int[n+1];

        pre[0]=0;
        for(int i=0;i<n;i++){
            pre[i+1]=pre[i]+nums[i];
        }
        int maxx=0;
        for(int i=1;i<=n;i++){
            for(int j=i+firstLen;j<=n;j++){
                if(j+secondLen>n) break;
                maxx=Math.max(maxx,(pre[i+firstLen-1]-pre[i-1])+pre[j+secondLen-1]-pre[j-1]);
            }
            for(int j=i+secondLen;j<=n;j++){
                if(j+firstLen>n) break;
                maxx=Math.max(maxx,(pre[i+secondLen-1]-pre[i-1])+pre[j+firstLen-1]-pre[j-1]);
            }
        }
        return maxx;
    }



}
