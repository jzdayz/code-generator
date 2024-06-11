package io.github.jzdayz.controller;

import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.github.jzdayz.entity.CodeGeneratorEntity;
import io.github.jzdayz.logic.service.GeneratorCommon;
import io.github.jzdayz.logic.service.MbpGenerator;
import io.github.jzdayz.rs.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("mbp")
@AllArgsConstructor
@Slf4j
public class MybatisPlusCodeGeneratorController {

    private GeneratorCommon generatorCommon;

    private Validator validator;

    @ResponseBody
    @GetMapping("init-info")
    public R<?> initInfo() {
        return R.success(generatorCommon.list());
    }

    @GetMapping("generator")
    public Object generator(@RequestParam Map<String, Object> map) throws BindException{
        CodeGeneratorEntity entity = new CodeGeneratorEntity();
        BeanCopier.create(map, entity, CopyOptions.create()).copy();
        BindException bindException = new BindException(entity, "args");
        validator.validate(entity, bindException);
        if (bindException.hasErrors()){
            throw bindException;
        }
        log.info("{}", entity);
        entity.setAlias(StrUtil.isNotBlank(entity.getAlias()) ? entity.getAlias() : IdUtil.randomUUID());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"mbg.zip\"");
        return new HttpEntity<>(generatorCommon.generator("mbp", entity), httpHeaders);
    }

}
