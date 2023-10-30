import dev.langchain4j.model.localai.LocalAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;

@ToString
class Vortrag {
    String name;
    String teaser;
    List<String> abschnitte;
}

interface VortragAssistent {
    @UserMessage("Generiere einen Vortrag zum Thema {{thema}} und benutze max. 40 Worte")
    @SystemMessage("Du bist ein Generator der witzigen " +
            "Vorträge über die Software-Architektur")
    Vortrag erfinde(@V("thema") String t);
}

@Slf4j
public class Demo {
    public static void main(String[] args) {
        var gpt = OpenAiChatModel.builder().apiKey("demo").timeout(Duration.ofMinutes(1)).build();

        var llama = LocalAiChatModel.builder()
                .baseUrl("http://localhost:8080")
                .modelName("codellama-7b-instruct-gguf")
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .build();

        var va = AiServices.builder(VortragAssistent.class)
                .chatLanguageModel(gpt)
                .build();

        var antwort = va.erfinde("Kryptographie");
        log.info(antwort.toString());
    }
}
