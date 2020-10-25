package org.vb;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.vb.config.APIConf;

@Slf4j
public class APIModule extends AbstractModule {

    private final APIConf apiConf;

    public APIModule(APIConf config) {
        this.apiConf = config;
    };

    @Override
    protected void configure() {
        log.info("Binding APIConf...");
        bind(APIConf.class).toInstance(apiConf);
        log.info("Binding APIApp...");
        bind(APIApp.class).in(Singleton.class);
        log.info("Binding Resource_Classes...");
        log.info(APIApp.RESOURCE_CLASSES.toString());
        for (Class<?> c : APIApp.RESOURCE_CLASSES) {
            bind(c).in(Singleton.class);
        }
        this.binder().requireExplicitBindings();
    }
}
