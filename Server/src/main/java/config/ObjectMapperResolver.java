package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {

    private static final ObjectMapper MAPPER =
            new ObjectMapper()
                    .findAndRegisterModules();

    @Override public ObjectMapper getContext(Class<?> type) { return MAPPER; }
}
