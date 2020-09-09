package cn.ykf.service;

import org.apache.dubbo.common.extension.SPI;

/**
 * 动物接口
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-09
 */
@SPI
public interface Animal {

    /**
     * 动物叫
     */
    void say();
}
