package com.shq.auth;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shq.model.process.ProcessTemplate;
import com.shq.process.service.ProcessTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Testdemo1 {

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Test
    public void test1(){
        long page = 1;
        long limit = 1;
        Page<ProcessTemplate> pageParam = new Page<>(page, limit);
        IPage<ProcessTemplate> pageModel = processTemplateService.selectPage(pageParam);

        System.out.println(pageModel);
    }

}
