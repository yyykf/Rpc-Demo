package cn.ykf.service.impl;

import cn.ykf.service.Animal;

/**
 * Cat
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-09
 */
public class Cat implements Animal {
    @Override
    public void say() {
        System.out.println("Cat 喵喵喵...");
    }
}
