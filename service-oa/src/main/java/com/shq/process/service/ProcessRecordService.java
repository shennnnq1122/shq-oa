package com.shq.process.service;

import com.shq.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author shq
 * @since 2023-06-02
 */
public interface ProcessRecordService extends IService<ProcessRecord> {

    void record(Long processId, Integer status, String description);

}
