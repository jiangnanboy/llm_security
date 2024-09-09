package com.llm.service;

import com.llm.sensitivity.SensitiveWordResult;

import java.util.List;

public class Result {
    private int code = 200;
    private String message = "正常内容！";
    private List<SensitiveWordResult> sensitiveWordResultList;
    private List<ModelResult> modelDetResultList;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<SensitiveWordResult> getSensitiveWordResultList() {
        return sensitiveWordResultList;
    }

    public List<ModelResult> getModelDetResultList() {
        return modelDetResultList;
    }

    public static class Builder {
        private int code;
        private String message;
        private List<SensitiveWordResult> sensitiveWordResultList;
        private List<ModelResult> modelDetResultList;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder sensitiveWordResultList(List<SensitiveWordResult> sensitiveWordResultList) {
            this.sensitiveWordResultList = sensitiveWordResultList;
            return this;
        }

        public Builder modelDetResultList(List<ModelResult> modelDetResultList) {
            this.modelDetResultList = modelDetResultList;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }

    private Result(Builder builder) {
        code = builder.code;
        message = builder.message;
        sensitiveWordResultList = builder.sensitiveWordResultList;
        modelDetResultList = builder.modelDetResultList;
    }

}

