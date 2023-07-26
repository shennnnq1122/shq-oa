package com.shq.process.service;

import com.shq.model.process.ProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author shq
 * @since 2023-05-29
 */
public interface ProcessTypeService extends IService<ProcessType> {

    List<ProcessType> findProcessType();
}
