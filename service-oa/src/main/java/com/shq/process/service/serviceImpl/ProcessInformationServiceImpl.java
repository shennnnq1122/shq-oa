package com.shq.process.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shq.model.process.ProcessInformation;
import com.shq.process.mapper.ProcessInformationMapper;
import com.shq.process.service.ProcessInformationService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;


@Service
public class ProcessInformationServiceImpl  extends ServiceImpl<ProcessInformationMapper, ProcessInformation> implements ProcessInformationService {

    @Autowired
    private RepositoryService repositoryService;



    @Override
    public InputStream getProcessDefineResource(String processDefineId) {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefineId).singleResult();
        if (processDefinition != null) {
            String resourceName = processDefinition.getDiagramResourceName();
            return repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        }
        return null;
    }

    @Override
    public void getImage(HttpServletResponse response, String processDefineId) {

        String path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
            path = URLDecoder.decode(path, "utf-8");
            File file=new File(path,processDefineId.substring(processDefineId.lastIndexOf(':') + 1)+".png");
            if(!file.exists()){
                cacheImage(processDefineId , file);
            }

            FileInputStream inputStream = new FileInputStream(file);

            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/png");


            byte[] bytes = new byte[1024];

            while (inputStream.read(bytes) != -1){
                outputStream.write(bytes);
                outputStream.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cacheImage(String processDefineId , File file) {

        InputStream inputStream = getProcessDefineResource(processDefineId);
        FileOutputStream fos= null;
        try {
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int nRead = 0;
            while ((nRead = inputStream.read(b)) != -1) {
                fos.write(b, 0, nRead);
            }
            fos.flush();
            fos.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    @Override
//    @Transactional("RuntimeException.class")
//    public boolean removeByIds(Collection<? extends Serializable> idList) {
//        List<ProcessInformation> processInformations = baseMapper.selectBatchIds(idList);
//        for (ProcessInformation processInformation : processInformations) {
//            // 获取部署ID
//            String deploymentId = repositoryService
//                    .createProcessDefinitionQuery()
//                    .processDefinitionId(processInformation.getProcessDefinitionId())
//                    .singleResult()
//                    .getDeploymentId();
//
//            // 删除部署，cascade 为 true 表示级联删除，会删除与该部署相关的所有数据
//            repositoryService.deleteDeployment(deploymentId);
//
//        }
//        return super.removeByIds(idList);
//    }
}
