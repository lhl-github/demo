package com.xljy.util;

import java.util.UUID;

public class ZhangHao {
    /**
     * 生成随机账号
     * @return
     */
    public static String getZhangHaoUuid() {//12位
        int machineId = 1;   //最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        return machineId + String.format("%011d", hashCodeV);
     }

}
