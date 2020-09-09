package cn.ykf;

import cn.ykf.service.Animal;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

import java.util.ServiceLoader;

/**
 * SPI Demo
 *
 * @author YuKaiFan <1092882580@qq.com>
 * @date 2020-09-09
 */
public class SpiTest {

    /**
     * Test Java SPI
     */
    @Test
    public void testJavaSpi() {
        ServiceLoader<Animal> services = ServiceLoader.load(Animal.class);
        for (Animal animal : services) {
            animal.say();
        }
    }

    /**
     * Test Dubbo SPI
     */
    @Test
    public void testDubboSpi() {
        // get extensionLoader
        ExtensionLoader<Animal> extensionLoader = ExtensionLoader.getExtensionLoader(Animal.class);
        // get interface implements you need
        Animal dog = extensionLoader.getExtension("dog");
        dog.say();
    }
}
