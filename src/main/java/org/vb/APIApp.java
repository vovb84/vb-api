package org.vb;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.vb.config.APIConf;
import org.vb.resources.APIResource;
import org.vb.resourceslibs.APIHttpLib;
import org.vb.resourceslibs.APIJsonLib;
import org.vb.resourceslibs.APILib;
import org.vb.resourceslibs.APIUtilLib;

import java.util.List;
@Slf4j
public class APIApp extends Application<APIConf> {

    private static final String SERVICE_NAME = "APIApp";
    public static final List<Class<?>> RESOURCE_CLASSES = ImmutableList.<Class<?>>builder()
            .add(APIResource.class)
            .add(APIHttpLib.class)
            .add(APIJsonLib.class)
            .add(APIUtilLib.class)
            .add(APILib.class)
        .build();

    @Override
    public String getName() { return SERVICE_NAME; }

    @Override
    public void initialize(final Bootstrap<APIConf> bootstrap) {
        /*bootstrap.addBundle(GuiceBundle.builder().enableAutoConfig(this.getClass().getPackage().getName())
                .modules(new APIModule()).build()); */
        /* Initialize swagger-ui */
        /*final AssetsBundle assetsBundle = new
                AssetsBundle("/swagger-ui", "/ui", "index.html");
        bootstrap.addBundle(assetsBundle); */
        //bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        bootstrap.addBundle(new AssetsBundle(
                "/swagger-ui", "/ui", "index.html", "ui"));
        bootstrap.addBundle(new AssetsBundle(
                "/spec", "/spec", null, "spec" ));

    }

    @Override
    public void run(final APIConf apiConf,
                    final Environment environment) {
        /* Application Implementation */
        log.info("Initializing APIApp...");
        this.initSwagger(apiConf, environment);
    }

    private void initSwagger(APIConf apiConf, Environment env) {

        /* ==== Swagger Resource ====
         * ApiListingResource creates the swagger.json
         * file at localhost:8080/swagger.json */

        /* env.jersey().register(new ApiListingResource());
        env.jersey().register(SwaggerSerializers.class);

        Package objPkg = this.getClass().getPackage();
        String strVersion = objPkg.getImplementationVersion(); */

        /* ==== Swagger Scanner ====
         * finds all resources for @Api Annotations */
        /* ScannerFactory.setScanner(new DefaultJaxrsScanner()); */

        /* ==== What is on the page http://localhost:8080/ui/ ==== */
        /* BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion(strVersion);
        beanConfig.setSchemes(new String[] {"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setPrettyPrint(true);
        beanConfig.setDescription("The dropwizard APIs");

        beanConfig.setResourcePackage("org.vb");
        beanConfig.setScan(true); */

        try {
            Injector injector = Guice.createInjector(new APIModule(apiConf));
            registerResources(env, injector);
            log.info("APIApp initialization completed.");
        } catch (Throwable throwable) {
            log.error("APIApp failed to start.", throwable);
        }
    }

    private void registerResources(Environment environment, Injector injector) {
        JerseyEnvironment jersey = environment.jersey();
        for (Class<?> clazz : RESOURCE_CLASSES) {
            log.info("Registering resource {}...", clazz.getSimpleName());
            jersey.register(injector.getInstance(clazz));
        }
    }

    public static void main(final String[] args) throws Exception {
        log.info("Starting APIApp...");
        new APIApp().run(args);
    }


}
