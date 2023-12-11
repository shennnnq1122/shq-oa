package com.shq.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shq.model.process.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shq.vo.process.ApprovalVo;
import com.shq.vo.process.ProcessFormVo;
import com.shq.vo.process.ProcessQueryVo;
import com.shq.vo.process.ProcessVo;

import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author shq
 * @since 2023-05-31
 */
public interface ProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    String deployByZip(InputStream inputStream);

    void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);

    Map<String,Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);
}
