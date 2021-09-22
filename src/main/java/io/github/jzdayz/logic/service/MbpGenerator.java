package io.github.jzdayz.logic.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import io.github.jzdayz.entity.CodeGeneratorEntity;
import io.github.jzdayz.ex.TableNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Objects;

@Service
@Slf4j
public class MbpGenerator {

    private static final String TEMP_PATH = System.getProperty("java.io.tmpdir") + "code-generator" + File.separator;

    private static Field NAME_CONVERT;

    static {
        try {
            NAME_CONVERT = Entity.class.getDeclaredField("nameConvert");
            NAME_CONVERT.setAccessible(true);
        } catch (NoSuchFieldException e) {
            log.error("field", e);
            System.exit(0);
        }
    }

    public FileSystemResource generator(CodeGeneratorEntity cg) {
        String path = TEMP_PATH + IdUtil.fastSimpleUUID();
        StrategyConfig.Builder scBuild = new StrategyConfig
                .Builder();
        Mapper.Builder mapperBuild = scBuild
                .mapperBuilder().enableBaseResultMap();
        Controller.Builder controllerBuild = mapperBuild
                .controllerBuilder().enableRestStyle().enableHyphenStyle();
        Entity.Builder entityBuild = controllerBuild
                .entityBuilder().naming(NamingStrategy.underline_to_camel);
        entityBuild.enableTableFieldAnnotation();
        if (cg.getLombok()) {
            entityBuild.enableLombok();
        }
        if (Objects.equals("SQL", cg.getTfType())) {
            scBuild.likeTable(new LikeTable(cg.getTableString()));
        } else {
            scBuild.disableSqlFilter();
            scBuild.addInclude(cg.getTableString());
        }
        StrategyConfig sc = entityBuild.build();
        INameConvert nameConvert = sc.entity().getNameConvert();
        try {
            NAME_CONVERT.set(sc.entity(), new INameConvert() {
                @Override
                public String entityNameConvert(TableInfo tableInfo) {
                    String name = nameConvert.entityNameConvert(tableInfo);
                    String tableNameFormat = cg.getTableNameFormat();
                    if (StrUtil.isBlank(tableNameFormat)) {
                        return name;
                    }
                    return tableNameFormat.replace("${entity}", name);
                }

                @Override
                public String propertyNameConvert(TableField field) {
                    return nameConvert.propertyNameConvert(field);
                }
            });
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        GlobalConfig.Builder gcBuild = new GlobalConfig
                .Builder()
                .fileOverride()
                .openDir(false)
                .outputDir(path)
                .author(System.getProperty("user.name"))
                .dateType(DateType.ONLY_DATE);
        if (cg.getSwagger()) {
            gcBuild.enableSwagger();
        }
        DataSourceConfig.Builder dsBuild = new DataSourceConfig.Builder(cg.getJdbc(), cg.getUser(), cg.getPwd());
        if (StrUtil.isNotBlank(cg.getSchema())){
            dsBuild.schema(cg.getSchema());
        }
        new AutoGenerator(dsBuild.build())
                // 全局配置
                .global(gcBuild.build())
                // 包配置
                .packageInfo(new PackageConfig.Builder().parent(cg.getMbpPackage()).build())
                // 策略配置
                .strategy(sc)
                // 执行
                .execute();
        if (!Files.exists(new File(path).toPath())) {
            throw new TableNotFoundException(path);
        }
        log.info("生成目录:{}", path);
        File zip = ZipUtil.zip(path);
        log.info("zip目录:{}", zip);
        return new FileSystemResource(zip);
    }

}
