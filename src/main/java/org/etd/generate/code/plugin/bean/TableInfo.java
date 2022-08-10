package org.etd.generate.code.plugin.bean;

import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTable;
import com.intellij.database.util.DasUtil;
import com.intellij.util.containers.JBIterable;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;
import org.etd.generate.code.plugin.utils.ToHumpUtil;

import java.io.Serializable;
import java.util.List;

@Data
public class TableInfo implements Serializable {
    /**
     * 作者
     */
    private String author;
    /**
     * 用于存放原始表信息
     */
    private DasTable obj;
    /**
     * 表名称
     */
    private String name;

    /**
     * 注释
     */
    private String comment;

    /**
     * 列信息
     */
    private List<ColumnInfo> columns;
    /**
     * 子表，用于Mybatis.xml中生成关联性查询
     */
    private List<TableInfo> childTables;

    /**
     * 回调地址
     */
    private Callback callback;


    public TableInfo(DasTable obj, String author, String packageName, String path) {

        this.obj = obj;
        this.author = author;
        this.name = ToHumpUtil.upperCaseFirstOne(ToHumpUtil.toHump(obj.getName()));
        this.callback = new Callback(packageName, path);
        this.comment = obj.getComment();
        this.columns = initColumnInfo();

    }

    private List<ColumnInfo> initColumnInfo() {
        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(obj);
        List<ColumnInfo> columnInfos = Lists.newArrayList();
        for (DasColumn column : columns) {
            ColumnInfo columnInfo = new ColumnInfo(column);
            columnInfos.add(columnInfo);
        }
        return columnInfos;
    }
}
