package com.llm.controller;

import com.llm.service.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.llm.service.SecurityDet;

@RestController
@RequestMapping("llm_security")
@RequiredArgsConstructor
@Slf4j
public class Controller {
    @PostMapping("detection")
    public Result detect(@RequestBody Content content) throws Exception {
        var text = content.getContent();
        var result = SecurityDet.detect(text);
        return result;
    }
}
