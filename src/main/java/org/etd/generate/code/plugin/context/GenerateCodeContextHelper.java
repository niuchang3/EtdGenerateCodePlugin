package org.etd.generate.code.plugin.context;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 代码生成器本地上下文
 */
public class GenerateCodeContextHelper {

    private static final ThreadLocal<GenerateCodeContext> context = ThreadLocal.withInitial(GenerateCodeContext::new);

    /**
     * 从本地线程获取上下文
     *
     * @return
     */
    public static GenerateCodeContext getContext() {
        return context.get();
    }

    public static void setContext(GenerateCodeContext generateCodeContext) {
        context.set(generateCodeContext);
    }


    /**
     * 清理上下文内容
     */
    public static void clean() {
        GenerateCodeContext requestContextModel = getContext();
        if (ObjectUtils.isEmpty(requestContextModel)) {
            return;
        }
        requestContextModel.clean();
        context.remove();
    }

}
