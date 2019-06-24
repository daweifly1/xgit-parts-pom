package cn.com.xgit.gw.module.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 拥有的权限计划
 */
@Data
public class RequestUrlSet implements Serializable {
    //需要匹配的url
    private Set<String> matchUrls;
    //具体的url
    private Set<String> urls;
}
