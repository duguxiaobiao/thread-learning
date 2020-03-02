package com.lonely.设计模式在多线程中的体现.上下文模式_使用ThreadLocal实现;

/**
 * @auther: 15072
 * @date: 2020/2/24 14:15
 * Description: 任务处理上下文,使用单例模式
 */
public class ActionContext {

    private static final ThreadLocal<User> THREAD_LOCAL = ThreadLocal.withInitial(() -> new User());

    private ActionContext() {

    }

    public static ActionContext getInstance() {
        return ActionContextHolder.actionContext;
    }

    /**
     * 获取当前线程中保存的用户信息
     *
     * @return
     */
    public User getUser() {
        return THREAD_LOCAL.get();
    }

    /**
     * 结束后，清除当前线程的value值
     */
    public void clear(){
        THREAD_LOCAL.remove();
    }

    private static class ActionContextHolder {
        private static ActionContext actionContext = new ActionContext();
    }

}
