package obigo.obigo_ai_server.prompt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptLoader loader;
    // 매우 단순한 인메모리 캐시
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    /** classpath 에서 읽고 캐시에 저장 */
    public String get(String classpathLocation) {
        return cache.computeIfAbsent(classpathLocation, loader::load);
    }

    /** {{key}} 치환 템플릿 */
    public String render(String template, Map<String, String> ctx) {
        String out = template;
        if (ctx != null) {
            for (var e : ctx.entrySet()) {
                out = out.replace("{{" + e.getKey() + "}}", e.getValue());
            }
        }
        return out;
    }
}

