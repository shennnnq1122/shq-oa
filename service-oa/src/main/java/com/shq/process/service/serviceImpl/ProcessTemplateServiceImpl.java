package com.shq.process.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shq.model.process.ProcessTemplate;

import com.shq.model.process.ProcessType;
import com.shq.process.mapper.ProcessTemplateMapper;
import com.shq.process.service.ProcessService;
import com.shq.process.service.ProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shq.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author shq
 * @since 2023-05-29
 */
@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate> implements ProcessTemplateService {

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ProcessService processService;



    @Override
    public IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam) {

        LambdaQueryWrapper<ProcessTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ProcessTemplate::getId);

        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, queryWrapper);
        List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();
        List<Long> list = processTemplateList.stream().map(ProcessTemplate::getProcessTypeId).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(list)) {
            return null;
        }

        Map<Long, String> listMap = processTypeService.list(new LambdaQueryWrapper<ProcessType>().in(ProcessType::getId, list))
                .stream().collect(Collectors.toMap(ProcessType::getId, ProcessType::getName));

        processTemplateList.stream().map(c->{c.setName(listMap.get(c.getId())); return c;});


        return processTemplatePage;
    }

    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = this.getById(id);
        processTemplate.setStatus(1);
        this.updateById(processTemplate);

    }
}
