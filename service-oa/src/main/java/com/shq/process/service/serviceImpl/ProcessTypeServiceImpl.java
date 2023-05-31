package com.shq.process.service.serviceImpl;

import com.shq.model.process.ProcessType;
import com.shq.process.mapper.ProcessTypeMapper;
import com.shq.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author shq
 * @since 2023-05-29
 */
@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {

}
