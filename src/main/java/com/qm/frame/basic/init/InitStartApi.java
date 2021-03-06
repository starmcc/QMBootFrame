package com.qm.frame.basic.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author 浅梦工作室
 * @createDate 2018年11月18日17:56:56
 * @Description 项目启动时，初始化某些设定
 */
@Component
public class InitStartApi {

    /**
     * @Title init
     * @Description 项目启动时，只执行一次
     */
    @PostConstruct
    public void init() {
        System.out.println("\n" +
                "                  ____    __  __________  ___   __  _______\n" +
                "                 / __ \\  /  |/  / __/ _ \\/ _ | /  |/  / __/\n" +
                "                / /_/ / / /|_/ / _// , _/ __ |/ /|_/ / _/\n" +
                "                \\___\\_\\/_/  /_/_/ /_/|_/_/ |_/_/  /_/___/\n" +
                "         ________________________________________________________\n" +
                "________| 浅梦:gitHub:https://github.com/starmcc                 |_______\n" +
                "\\       | 邮箱:starczt1992@163.com                               |      /\n" +
                " \\      | 开源:[https://github.com/starmcc/QMBoootFrame]         |     /\n" +
                "  |     | 文档:[https://github.com/starmcc/QMBoootFrame/wiki]    |    |\n" +
                " /      |________________________________________________________|    \\\n" +
                "/__________)                                                 (_________\\\n" +
                "                    指若下键万里行，如入浅梦醉逍遥；");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("※※※※※※※※※※※※服务已停止※※※※※※※※※※※※");
        System.out.println("浅梦gitHub:https://github.com/starmcc/QMBoootFrame");
    }
}
