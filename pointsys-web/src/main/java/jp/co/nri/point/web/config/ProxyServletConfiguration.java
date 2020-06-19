package jp.co.nri.point.web.config;

import java.util.Map;

import javax.servlet.Servlet;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.ImmutableMap;

@Configuration
@ConfigurationProperties(prefix = "proxy")
public class ProxyServletConfiguration {

    private String servlet_url;

    private String target_url;

    public String getServlet_url() {
        return servlet_url;
    }

    public void setServlet_url(String servlet_url) {
        this.servlet_url = servlet_url;
    }

    public String getTarget_url() {
        return target_url;
    }

    public void setTarget_url(String target_url) {
        this.target_url = target_url;
    }

    @Bean
    public Servlet createProxyServlet() {
        // 创建新的ProxyServlet
        return new ProxyServlet();
    }

    @Bean
    public ServletRegistrationBean<Servlet> proxyServletRegistration() {
        ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<Servlet>(createProxyServlet(),
                servlet_url);
        Map<String, String> params = ImmutableMap.of("targetUri", target_url, "log", "true");
        registrationBean.setInitParameters(params);
        return registrationBean;
    }
}
