package cn.ykf;

import cn.ykf.service.Animal;

import java.util.ServiceLoader;

/**
 * Java SPI Demo
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-09
 */
public class Main {
    public static void main(String[] args) {
        ServiceLoader<Animal> services = ServiceLoader.load(Animal.class);
        for (Animal animal : services) {
            animal.say();
        }
    }
}
