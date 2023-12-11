package com.shq.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shq.model.process.ProcessInformation;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public interface ProcessInformationService extends IService<ProcessInformation> {


    InputStream getProcessDefineResource(String processDefineId);

    void getImage(HttpServletResponse response, String processDefineId);



}
