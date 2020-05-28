package com.fuzzy.reasoner.component;

import fuzzydl.KnowledgeBase;
import fuzzydl.exception.FuzzyOntologyException;
import fuzzydl.exception.InconsistentOntologyException;
import fuzzydl.parser.ParseException;
import fuzzydl.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ModelBuilder {
    KnowledgeBase kb;

    public KnowledgeBase getKb() {
        return kb;
    }

    public ModelBuilder(int temperature, int numberOfMovements, int smokeLevel) throws IOException, ParseException, InconsistentOntologyException, FuzzyOntologyException {
        String rawModel = readFile("FuzzyModel.txt");
        String model = rawModel.replace("{temperature}", String.valueOf(temperature)).replace("{numberOfMovements}", String.valueOf(numberOfMovements)).replace("{smokeLevel}", String.valueOf(smokeLevel));
        Parser parser = new Parser(new ByteArrayInputStream(model.getBytes(StandardCharsets.UTF_8)));
        parser.clearKB();
        parser.Start();
        kb = parser.getKB();
        kb.solveKB();
    }

    private String readFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return new String(data, "UTF-8");
    }
}
