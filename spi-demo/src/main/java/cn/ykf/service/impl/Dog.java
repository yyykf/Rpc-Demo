package cn.ykf.service.impl;

import cn.ykf.service.Animal;

/**
 * Dog
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-09
 */
public class Dog implements Animal {
    @Override
    public void say() {
        System.out.println("Dog 汪汪汪...");
    }
}
