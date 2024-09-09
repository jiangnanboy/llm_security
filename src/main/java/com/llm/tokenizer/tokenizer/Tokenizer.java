package com.llm.tokenizer.tokenizer;

import java.util.List;

public interface Tokenizer {
	public List<String> tokenize(String text);
}
