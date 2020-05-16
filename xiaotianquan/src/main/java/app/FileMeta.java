package app;

import util.Util;

import java.io.File;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: STAR
 * Date: 2020 -01
 * Time: 9:24
 */
public class FileMeta {
    private String name;
    private String path;
    private Long size;
    private Long lastModified;
    private Boolean isDirectory;
    private String sizeText;
    private String lastModifiedText;

    public FileMeta(String name, String path, Long size,
                    Long lastModified, Boolean isDirectory) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.isDirectory = isDirectory;
        this.sizeText = Util.parseSize(size);
        this.lastModifiedText = Util.parseData(lastModified);
    }

    public FileMeta(File child) {
        this(child.getName(),child.getParent(),
                        child.length(),child.lastModified(),child.isDirectory());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta meta = (FileMeta) o;
        return Objects.equals(name, meta.name) &&
                Objects.equals(path, meta.path) &&
                Objects.equals(size, meta.size) &&
                Objects.equals(lastModified, meta.lastModified) &&
                Objects.equals(isDirectory, meta.isDirectory) &&
                Objects.equals(sizeText, meta.sizeText) &&
                Objects.equals(lastModifiedText, meta.lastModifiedText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, size, lastModified, isDirectory, sizeText, lastModifiedText);
    }
}
