package utils;


import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class TemplateBuilder {

    public String getResolvedTemplate(String path, Map<String, Object> values) throws IOException {
        String template = readDataFromFile(path);
        StringSubstitutor stringSubstitutor = new StringSubstitutor(values, "{{", "}}");
        return stringSubstitutor.replace(template).replaceAll("\\s", "");
    }


    // public static String getTemplate(String path) {


    //        ClassLoader classLoader = Utils.class.getClassLoader();
//        try {
//            return IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream(path)), Charset.defaultCharset());
//        } catch (IOException exception) {
//            throw new RuntimeException("Cannot read file '" + path + "'", exception);
//        }
    //  }
    public String readDataFromFile(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        String data = FileUtils.readFileToString(file, Charset.defaultCharset());
        return data;
    }


}
