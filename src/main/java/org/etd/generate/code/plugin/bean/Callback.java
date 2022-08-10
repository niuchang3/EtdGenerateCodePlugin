package org.etd.generate.code.plugin.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Callback implements Cloneable, Serializable {


    private String fileName;

    /**
     * 生成的包名
     */
    private String packageName;
    /**
     * 文件的路径
     */
    private String path;

    public Callback(String packageName, String path) {
        this.packageName = packageName;
        this.path = path;
    }

    @Override
    protected Callback clone() throws CloneNotSupportedException {
        return (Callback) super.clone();
    }

    /**
     * 复制一份对象
     * @return
     * @throws CloneNotSupportedException
     */
    public Callback copy() throws CloneNotSupportedException {
        return clone();
    }
}
