package ninja.cero.sqltemplate.core.template;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NoEngine implements TemplateEngine {
    /**
     * template cache
     */
    protected final ConcurrentMap<String, String> templateCache = new ConcurrentHashMap<>();

    @Override
    public String get(String fileName, Object[] args) throws IOException {
        String template = templateCache.get(fileName);

        if (template != null) {
            return template;
        }

        URL resource = getClass().getResource("/" + fileName);
        if (resource == null) {
            throw new FileNotFoundException("Tempalte '" + fileName + "' not found");
        }

        Path path = Paths.get(resource.getFile());
        try (Stream<String> stream = Files.lines(path)) {
            template = stream.collect(Collectors.joining("\n"));
        }

        templateCache.put(fileName, template);
        return template;
    }

    @Override
    public String get(String fileName, Object object) throws IOException {
        return get(fileName, new Object[]{object});
    }
}