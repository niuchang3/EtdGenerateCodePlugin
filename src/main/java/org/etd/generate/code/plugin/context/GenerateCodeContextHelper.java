package org.etd.generate.code.plugin.context;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 代码生成器本地上下文
 */
public class GenerateCodeContextHelper {

    private static final ThreadLocal<ApplicationContext> context = ThreadLocal.withInitial(ApplicationContext::new);

    /**
     * 从本地线程获取上下文
     *
     * @return
     */
    public static <T extends ApplicationContext> T getContext() {
        return (T) context.get();
    }

    public static <T extends ApplicationContext> void setContext(T t) {
        context.set(t);
    }


    public static void setAttribute(String key, Object value) {
        getContext().getAttributes().put(key, value);
    }

    public static Object getAttribute(String key) {
        return getContext().getAttributes().get(key);
    }

    public static void remove(String key) {
        getContext().getAttributes().remove(key);
    }

    /**
     * 清理上下文内容
     */
    public static void clean() {
        ApplicationContext requestContextModel = getContext();
        if (ObjectUtils.isEmpty(requestContextModel)) {
            return;
        }
        requestContextModel.clean();
        context.remove();
    }

}
