package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleMenuTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;

    @Test
    public void whenAddThenReturnSame() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        assertThat(new Menu.MenuItemInfo("Сходить в магазин",
                List.of("Купить продукты"), STUB_ACTION, "1."))
                .isEqualTo(menu.select("Сходить в магазин").get());
        assertThat(new Menu.MenuItemInfo(
                "Купить продукты",
                List.of("Купить хлеб", "Купить молоко"), STUB_ACTION, "1.1."))
                .isEqualTo(menu.select("Купить продукты").get());
        assertThat(new Menu.MenuItemInfo(
                "Покормить собаку", List.of(), STUB_ACTION, "2."))
                .isEqualTo(menu.select("Покормить собаку").get());
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }

    @Test
    public void whenAddSuccessful() {
        Menu menu = new SimpleMenu();
        assertTrue(menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION));
    }

    @Test
    public void whenNotEdd() {
        Menu menu = new SimpleMenu();
        assertFalse(menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION));
    }

    @Test
    public void whenSelectLastChildren() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        Optional<Menu.MenuItemInfo> actual = menu.select("Купить хлеб");
        Menu.MenuItemInfo expected = new Menu.MenuItemInfo(
                "Купить хлеб",
                List.of(),
                STUB_ACTION,
                "1.1.1.");
        assertTrue(actual.isPresent());
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    public void whenNotSelect() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        Optional<Menu.MenuItemInfo> actual = menu.select("Купить хлеб");
        assertTrue(actual.isEmpty());
    }
}
