package com.lonely.设计模式在多线程中的体现.GuardedSuspension_暂停保护模式;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther: 15072
 * @date: 2020/2/23 16:58
 * Description: 请求资源
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    /**
     * 消息
     */
    private String name;
}
