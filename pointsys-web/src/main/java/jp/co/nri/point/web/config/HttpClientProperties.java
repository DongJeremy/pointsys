package jp.co.nri.point.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "https.httpclient")
public class HttpClientProperties {

    private String url;
    /**
     * 是否开启服务端HTTPS证书校验
     */
    private boolean enabled = true;
    /**
     * 是否发送客户端证书
     */
    private boolean clientCert = true;
    /**
     * 是否支持eureka的HTTPS注册
     */
    private boolean eureka = true;
    /**
     * CA根证书密钥库文件
     */
    private String caRootCertKeyStore;
    /**
     * CA根证书密钥库密码
     */
    private String caRootCertPassword;
    /**
     * 客户端证书库文件
     */
    private String clientCertKeyStore;
    /**
     * 客户端证书库密码
     */
    private String clientCertPassword;
    /**
     * 建立连接的超时时间
     */
    private int connectTimeout = 20000;
    /**
     * 连接不够用的等待时间
     */
    private int requestTimeout = 20000;
    /**
     * 每次请求等待返回的超时时间
     */
    private int socketTimeout = 30000;
    /**
     * 每个主机最大连接数
     */
    private int defaultMaxPerRoute = 100;
    /**
     * 最大连接数
     */
    private int maxTotalConnections = 300;
    /**
     * 连接保持活跃的时间（Keep-Alive）
     */
    private int defaultKeepAliveTimeMillis = 20000;
    /**
     * 空闲连接的生存时间
     */
    private int closeIdleConnectionWaitTimeSecs = 30;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isClientCert() {
        return clientCert;
    }

    public void setClientCert(boolean clientCert) {
        this.clientCert = clientCert;
    }

    public boolean isEureka() {
        return eureka;
    }

    public void setEureka(boolean eureka) {
        this.eureka = eureka;
    }

    public String getCaRootCertKeyStore() {
        return caRootCertKeyStore;
    }

    public void setCaRootCertKeyStore(String caRootCertKeyStore) {
        this.caRootCertKeyStore = caRootCertKeyStore;
    }

    public String getCaRootCertPassword() {
        return caRootCertPassword;
    }

    public void setCaRootCertPassword(String caRootCertPassword) {
        this.caRootCertPassword = caRootCertPassword;
    }

    public String getClientCertKeyStore() {
        return clientCertKeyStore;
    }

    public void setClientCertKeyStore(String clientCertKeyStore) {
        this.clientCertKeyStore = clientCertKeyStore;
    }

    public String getClientCertPassword() {
        return clientCertPassword;
    }

    public void setClientCertPassword(String clientCertPassword) {
        this.clientCertPassword = clientCertPassword;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getDefaultKeepAliveTimeMillis() {
        return defaultKeepAliveTimeMillis;
    }

    public void setDefaultKeepAliveTimeMillis(int defaultKeepAliveTimeMillis) {
        this.defaultKeepAliveTimeMillis = defaultKeepAliveTimeMillis;
    }

    public int getCloseIdleConnectionWaitTimeSecs() {
        return closeIdleConnectionWaitTimeSecs;
    }

    public void setCloseIdleConnectionWaitTimeSecs(int closeIdleConnectionWaitTimeSecs) {
        this.closeIdleConnectionWaitTimeSecs = closeIdleConnectionWaitTimeSecs;
    }

}
