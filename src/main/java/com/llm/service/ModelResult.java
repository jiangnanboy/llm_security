package com.llm.service;

public class ModelResult {
    String label;
    float prob;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getProb() {
        return prob;
    }

    public void setProb(float prob) {
        this.prob = prob;
    }

    public ModelResult(String label, float prob) {
        this.label = label;
        this.prob = prob;
    }
}
