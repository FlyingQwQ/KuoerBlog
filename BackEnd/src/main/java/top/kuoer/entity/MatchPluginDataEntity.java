package top.kuoer.entity;

public class MatchPluginDataEntity {

    private String name;
    private String version;
    private String template;

    public MatchPluginDataEntity(String name, String version, String template) {
        this.name = name;
        this.version = version;
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
