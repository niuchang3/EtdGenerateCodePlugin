package org.etd.generate.code.plugin.constants;

import com.intellij.database.model.basic.BasicElement;
import com.intellij.database.view.DataSourceNode;
import com.intellij.openapi.actionSystem.DataKey;

public class BaseConstants {


    public static final DataKey<Object[]> DATABASE_NODES_KEY = DataKey.create("DATABASE_NODES");

    public static final DataKey<BasicElement[]> DATABASE_ELEMENTS = DataKey.create("DATABASE_ELEMENTS");

    public static final DataKey<DataSourceNode[]> DATABASE_RELATED_DATA_SOURCES = DataKey.create("DATABASE_RELATED_DATA_SOURCES");


    /**
     * 不允许操作的系统code
     */
    public static final String SYS_DEFAULT_CODE = "default";

    public static final String SYS_TEMPLATE_CODE = "SYS_TEMPLATE_CODE";

    public static final String SYS_AUTHOR_CODE = "AUTHOR";


}
