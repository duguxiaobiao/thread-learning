package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 19:58
 * Description:
 */
public class DisplayStringRequest extends MethodRequest {

    private final String text;

    protected DisplayStringRequest(Servant servant, FutureResult futureResult, String text) {
        super(servant, futureResult);
        this.text = text;
    }

    /**
     * 执行命令
     */
    @Override
    public void execute() {
        this.servant.displayString(this.text);
    }
}
