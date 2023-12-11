package com.shq.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shq.common.result.Result;
import com.shq.model.process.ProcessInformation;
import com.shq.model.process.ProcessTemplate;
import com.shq.process.service.ProcessInformationService;
import com.shq.process.service.ProcessService;
import com.shq.vo.process.ProcessQueryVo;
import com.shq.vo.process.ProcessVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/process/information")
public class ProcessInformationController {


    @Autowired
    private ProcessService processService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessInformationService processInformationService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit) {
        Page<ProcessInformation> pageParam = new Page<>(page, limit);
        Page<ProcessInformation> page1 = processInformationService.page(pageParam);
        return Result.ok(page1);
    }

    @ApiOperation(value = "获取流程定义列表")
    @GetMapping("/list")
    public Result list() {
        return Result.ok(processInformationService.list());
    }


    @ApiOperation(value = "新增流程定义")
    @PostMapping("/uploadProcessDefinition")
    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "java.io.File")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {


        String fileName = file.getOriginalFilename();

        InputStream inputStream = null;

        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;


        //保存流程信息
        ProcessInformation processInformation = new ProcessInformation();
        processInformation.setProcessDefinitionPath("processes/" + fileName);
        processInformation.setProcessDefinitionKey(fileName.substring(0, fileName.lastIndexOf(".")));


        //优先发布在线流程设计
        if(!StringUtils.isEmpty(processInformation.getProcessDefinitionPath())) {

            String id = processService.deployByZip(inputStream);
            processInformation.setProcessDefinitionId(id);
        }

        processInformation.setImg("http://localhost:8800/admin/process/information/image/" + processInformation.getProcessDefinitionId());



        return Result.ok(processInformation);

    }

    /**
     * 获取流程定义图片。
     *
     * @param response http请求响应
     * @param processDefineId 流程定义Id
     */
    @GetMapping("/image/{id}")
    @ApiOperation(value = "获取流程定义图片")
    public void getProcessDefineImge(HttpServletResponse response,
                                     @ApiParam(value = "流程定义id")
                                     @PathVariable("id") String processDefineId){

        processInformationService.getImage(response, processDefineId);

    }


    @PostMapping("/save")
    @ApiOperation("保存流程信息")
    public Result save(@RequestBody ProcessInformation processInformation){

        if(processInformationService.save(processInformation)){
            return Result.ok();
        }

        return Result.fail();

    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除已发布的流程")
    public Result del(@PathVariable("id") String id){
        // 获取部署ID
        String deploymentId = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(id)
                .singleResult()
                .getDeploymentId();

        // 删除部署，cascade 为 true 表示级联删除，会删除与该部署相关的所有数据
        repositoryService.deleteDeployment(deploymentId);

        return Result.ok();

    }
    @DeleteMapping("/del/{id}")
    @ApiOperation("删除流程信息")
    public Result delList(@PathVariable("id") Long id){

        processInformationService.removeById(id);


        return Result.ok();

    }











}
