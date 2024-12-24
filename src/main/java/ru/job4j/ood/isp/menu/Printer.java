package ru.job4j.ood.isp.menu;

public class Printer implements MenuPrinter {
    @Override
    public void print(Menu menu) {
        for (Menu.MenuItemInfo current : menu) {
            int tabsLength = current.getNumber().split("\\.").length;
            System.out.printf("%s%s%s%n", "\t".repeat(tabsLength), current.getNumber(), current.getName());
        }
    }
}
