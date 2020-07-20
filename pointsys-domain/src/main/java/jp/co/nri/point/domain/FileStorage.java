package jp.co.nri.point.domain;

public class FileStorage extends BaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String url;
    private long size;
    private String type;

    public FileStorage() {
        super();
    }

    public FileStorage(String name, String url, long size, String type) {
        super();
        this.name = name;
        this.url = url;
        this.size = size;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
