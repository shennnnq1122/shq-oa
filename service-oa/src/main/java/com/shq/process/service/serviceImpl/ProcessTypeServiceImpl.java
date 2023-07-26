package com.shq.process.service.serviceImpl;

import com.shq.model.process.ProcessTemplate;
import com.shq.model.process.ProcessType;
import com.shq.process.mapper.ProcessTypeMapper;
import com.shq.process.service.ProcessTemplateService;
import com.shq.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Override
    public List<ProcessType> findProcessType() {

        //查询所有审批分类
        List<ProcessType> processTypesList = baseMapper.selectList(null);

        //查询所有审批模板
        List<ProcessTemplate> processTemplateList = processTemplateService.list();

        //根据processTypesId将审批模板列表分成多个列表
        Map<Long, List<ProcessTemplate>> processTemplateListMap = processTemplateList.stream()
                .collect(Collectors.groupingBy(ProcessTemplate::getProcessTypeId));

        //遍历所有审批分类吧对应模板存入
        for (ProcessType processType: processTypesList) {
            Long processTypeId = processType.getId();
            processType.setProcessTemplateList(processTemplateListMap.get(processTypeId));
        }

        return processTypesList;
    }
}
