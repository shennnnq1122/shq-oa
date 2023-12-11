package com.shq.process.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shq.model.process.Process;
import com.shq.vo.process.ProcessQueryVo;
import com.shq.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author shq
 * @since 2023-05-31
 */
public interface ProcessMapper extends BaseMapper<Process> {


    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam,@Param("vo") ProcessQueryVo processQueryVo);

}
