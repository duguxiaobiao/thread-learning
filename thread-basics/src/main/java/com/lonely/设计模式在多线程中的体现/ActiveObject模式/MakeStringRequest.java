package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 19:43
 * Description: makeString对应的命令类
 */
public class MakeStringRequest extends MethodRequest {

    private final int count;

    private final char fillChar;

    protected MakeStringRequest(Servant servant, FutureResult futureResult, int count, char fillChar) {
        super(servant, futureResult);
        this.count = count;
        this.fillChar = fillChar;
    }

    /**
     * 执行命令
     */
    @Override
    public void execute() {
        Result result = this.servant.makeString(this.count, this.fillChar);
        this.futureResult.setResult(result.getResult());
    }
}
