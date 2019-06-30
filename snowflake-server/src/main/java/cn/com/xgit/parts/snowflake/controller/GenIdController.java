package cn.com.xgit.parts.snowflake.controller;

import cn.com.xgit.parts.snowflake.MybatisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/uid")
public class GenIdController {

    @Autowired
    MybatisKeyGenerator mybatisKeyGenerator;


    @ResponseBody
    @GetMapping("/nextId")
    public Long nextId() {
        return mybatisKeyGenerator.nextId();
    }
}
