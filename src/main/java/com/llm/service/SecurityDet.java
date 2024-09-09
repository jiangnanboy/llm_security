package com.llm.service;

import ai.onnxruntime.OrtException;
import com.llm.models.*;
import com.llm.sensitivity.SenDetection;
import com.llm.sensitivity.SimpleSenDetectionProcessor;
import com.llm.util.CollectionUtil;
import com.llm.util.PropertiesReader;

import java.util.List;

public class SecurityDet {
    static SenDetection senDetection = null;
    static TextTokenizer textTokenizer = null;
    static PoliticModel politicModel = null;
    static ViolenceModel violenceModel = null;
    static PornModel pornModel = null;
    static InsultModel insultModel = null;

    public SecurityDet() {}

    public SecurityDet(String sensiWordPath, String vocabPath, String politicModelPath, String violenceModelPath, String pornModelPath, String insultModelPath) throws OrtException {
        init(sensiWordPath, vocabPath, politicModelPath, violenceModelPath, pornModelPath, insultModelPath);
    }

    private static void init(String sensiWordPath, String vocabPath, String politicModelPath, String violenceModelPath, String pornModelPath, String insultModelPath) throws OrtException {
        SimpleSenDetectionProcessor simpleSenDetectionProcessor = SimpleSenDetectionProcessor.newInstance();
        senDetection = simpleSenDetectionProcessor.getKWSeeker(sensiWordPath);
        textTokenizer = new TextTokenizer(vocabPath);
        politicModel = new PoliticModel(politicModelPath);
        violenceModel = new ViolenceModel(violenceModelPath);
        pornModel = new PornModel(pornModelPath);
        insultModel = new InsultModel(insultModelPath);
    }

    static {
        String sensiWordPath = "sensiWordPath";
        String vocabPath = PropertiesReader.get("vocabPath");
        String politicModelPath = PropertiesReader.get("politicModelPath");
        String violenceModelPath = PropertiesReader.get("violenceModelPath");
        String pornModelPath = PropertiesReader.get("pornModelPath");
        String insultModelPath = PropertiesReader.get("insultModelPath");
        try {
            init(sensiWordPath, vocabPath, politicModelPath, violenceModelPath, pornModelPath, insultModelPath);
        } catch (OrtException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param text
     * @return
     */
    public static Result detect(String text) throws Exception {
        var resultBuilder = new Result.Builder();

        var sensiWords = senDetection.findWords(text);
        if(sensiWords.size() >= 0) {
            resultBuilder.sensitiveWordResultList(sensiWords);
            resultBuilder.code(500);
            resultBuilder.message("疑似包含敏信息！");
        }

        var onnxTensorMap = textTokenizer.parseInputText(text, politicModel.env);
        List<ModelResult> modelDetResultList = CollectionUtil.newArrayList();
        ModelResult modelResult = null;
        // politic detection
        var pairResult = politicModel.pred(onnxTensorMap);
        int label = pairResult.getLeft();
        float prob = pairResult.getRight();
        if(label == 1) {
            modelResult = new ModelResult("politics", prob);
            resultBuilder.code(500);
            resultBuilder.message("疑似包含敏信息！");
        } else if(label == 0) {
            modelResult = new ModelResult("no_politics", prob);
        }
        modelDetResultList.add(modelResult);

        // violence detection
        pairResult = violenceModel.pred(onnxTensorMap);
        label = pairResult.getLeft();
        prob = pairResult.getRight();
        if(label == 1) {
            modelResult = new ModelResult("violence", prob);
            resultBuilder.code(500);
            resultBuilder.message("疑似包含敏信息！");
        } else if(label == 0) {
            modelResult = new ModelResult("no_violence", prob);
        }
        modelDetResultList.add(modelResult);

        // porn detection
        pairResult = pornModel.pred(onnxTensorMap);
        label = pairResult.getLeft();
        prob = pairResult.getRight();
        if(label == 1) {
            modelResult = new ModelResult("porn", prob);
            resultBuilder.code(500);
            resultBuilder.message("疑似包含敏信息！");
        } else if(label == 0) {
            modelResult = new ModelResult("no_porn", prob);
        }
        modelDetResultList.add(modelResult);

        // insult detection
        pairResult = insultModel.pred(onnxTensorMap);
        label = pairResult.getLeft();
        prob = pairResult.getRight();
        if(label == 1) {
            modelResult = new ModelResult("insult", prob);
            resultBuilder.code(500);
            resultBuilder.message("疑似包含敏信息！");
        } else if(label == 0) {
            modelResult = new ModelResult("no_insult", prob);
        }
        modelDetResultList.add(modelResult);
        resultBuilder.modelDetResultList(modelDetResultList);

        return resultBuilder.build();
    }

}
