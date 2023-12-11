package com.shq.model.process;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "process_information")
public class ProcessInformation {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "process_definition_path")
    private String processDefinitionPath;

    @TableField(value = "process_definition_key")
    private String processDefinitionKey;

    @TableField(value = "process_definition_id")
    private String processDefinitionId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "img")
    private String img;
}