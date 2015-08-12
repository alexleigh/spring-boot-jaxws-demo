package me.alexleigh.springbootjaxws;

import com.sun.xml.ws.transport.http.servlet.SpringBinding;
import org.jvnet.jax_ws_commons.spring.SpringService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Servlet;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SpringBootJaxwsDemoApplication {

    private static final List<Object> DEMOSERVICE_METADATA;
    private static final Object DEMOSERVICE_WSDL_LOCATION;

    static {
        DEMOSERVICE_METADATA = Arrays.asList(
                SpringBootJaxwsDemoApplication.class.getResource("/wsdl/DemoService.wsdl"),
                SpringBootJaxwsDemoApplication.class.getResource("/xsd/Fibonacci.xsd"),
                SpringBootJaxwsDemoApplication.class.getResource("/xsd/Factorial.xsd"));
        DEMOSERVICE_WSDL_LOCATION = DEMOSERVICE_METADATA.get(0);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJaxwsDemoApplication.class, args);
    }

    @Bean
    public Servlet jaxwsServlet() {
        return new WSSpringServlet();
    }

    @Bean
    public ServletRegistrationBean jaxwsServletRegistration() {
        return new ServletRegistrationBean(jaxwsServlet(), "/service/*");
    }

    @Bean
    public SpringService fibonacciService() throws IOException {
        SpringService service = new SpringService();
        service.setImpl(FibonacciImpl.class);
        service.setServiceName(new QName("http://www.alexleigh.me/demo", "DemoService"));
        service.setPortName(new QName("http://www.alexleigh.me/demo", "FibonacciPort"));
        service.setMetadata(DEMOSERVICE_METADATA);
        service.setPrimaryWsdl(DEMOSERVICE_WSDL_LOCATION);
        return service;
    }

    @Bean
    public SpringService factorialService() throws IOException {
        SpringService service = new SpringService();
        service.setImpl(FactorialImpl.class);
        service.setServiceName(new QName("http://www.alexleigh.me/demo", "DemoService"));
        service.setPortName(new QName("http://www.alexleigh.me/demo", "FactorialPort"));
        service.setMetadata(DEMOSERVICE_METADATA);
        service.setPrimaryWsdl(DEMOSERVICE_WSDL_LOCATION);
        return service;
    }

    @Bean
    public SpringBinding fibonacciBinding() throws Exception {
        SpringBinding binding = new SpringBinding();
        binding.setUrl("/service/fibonacci");
        binding.setService(fibonacciService().getObject());
        return binding;
    }

    @Bean
    public SpringBinding factorialBinding() throws Exception {
        SpringBinding binding = new SpringBinding();
        binding.setUrl("/service/factorial");
        binding.setService(factorialService().getObject());
        return binding;
    }
}
