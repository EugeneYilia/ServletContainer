package config;

public class ServletConfig {
    private String name;
    private String className;
    private String mappingUri;

    public ServletConfig(){}

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getMappingUri() {
        return mappingUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMappingUrl(String mappingUrl) {
        this.mappingUri = mappingUrl;
    }
}
