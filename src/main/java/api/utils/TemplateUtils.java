package api.utils;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;

public class TemplateUtils {

    public String getResolvedTemplate(String path, Map<String, Object> values) throws IOException {
        String template = readDataFromFile(path);
        StringSubstitutor stringSubstitutor = new StringSubstitutor(values, "{{", "}}");
        return stringSubstitutor.replace(template).replaceAll("\\s", "");
    }

    public static String readDataFromFile(String path) throws IOException {
        ClassLoader classLoader = TemplateUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        String data = FileUtils.readFileToString(file, Charset.defaultCharset());
        return data;
    }

//    private static void exampleTemplateModification() throws IOException {
//        Map<String,Object> map = new HashMap<>();
//        map.put("key","value");
//        String build = TemplateBuilder.of(readDataFromFile("json.models/add_to_card.json"))
//                .withInsert("$","meta","123")
//                .withDelete("$.data[0].type")
//                .withInsert("$data[0]","jsonObject",map)
//                .withUpdate("$.data[0].jsonObject.key","newValue")
//                .build();
//        System.out.println(build);
//    }

//    public static String makeStructureModification() throws IOException {
//        String template = readDataFromFile("json.models/add_to_card.json");
//        DocumentContext context = JsonPath.parse(template);
//        context.put("$","meta","123");
//        context.delete("$['data'][0]['list']");
//        return context.jsonString();
//    }

}
