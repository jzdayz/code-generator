package io.github.jzdayz.entity;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 *
 * </p>
 *
 * @author huqingfeng
 * @since 2021-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("CODE_GENERATOR_ENTITY")
public class CodeGeneratorEntity implements Serializable {


    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    private String jdbc;

    private String alias;

    private String user;

    private String schema;

    private String pwd;

    @TableField("TABLE_STRING")
    private String tableString;

    @TableField("TF_TYPE")
    private String tfType;

    @TableField("MBP_PACKAGE")
    private String mbpPackage;

    private Boolean swagger;

    private Boolean lombok;

    @TableField("TABLE_NAME_FORMAT")
    private String tableNameFormat;

}
