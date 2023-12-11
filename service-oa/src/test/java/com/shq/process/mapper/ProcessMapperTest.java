package com.shq.process.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shq.process.service.ProcessInformationService;
import com.shq.vo.process.ProcessQueryVo;
import com.shq.vo.process.ProcessVo;
import org.apache.ibatis.javassist.ClassPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URLDecoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProcessMapperTest {

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProcessInformationService processInformationService;

    @Test
    void pageSelect(){
        IPage<ProcessVo> page = processMapper.selectPage(new Page<>(1, 10), new ProcessQueryVo());
        System.out.println(page.getRecords());


    }

    @Test
    void laodimg(){




        byte[] bytes = new byte[1024];

        try {






        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private  File streamToFile(InputStream inputStream) throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile("temp", ".tmp");

        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            // 从 InputStream 复制到 FileOutputStream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }




}