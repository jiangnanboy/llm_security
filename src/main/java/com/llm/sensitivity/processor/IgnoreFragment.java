package com.llm.sensitivity.processor;

import com.llm.sensitivity.KeyWord;

/**
 * 
 * 替换内容的片段处理方式
 *
 */
public class IgnoreFragment extends AbstractFragment {

    private String ignore = "";

    public IgnoreFragment() {
    }

    public IgnoreFragment(String ignore) {
        this.ignore = ignore;
    }

    @Override
    public String format(KeyWord word) {
        return ignore;
    }

}
