package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PrinterTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;

    @Test
    public void whenPrintOneRow() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Menu menu = new SimpleMenu();
        assertTrue(menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION));
        Printer printer = new Printer();
        printer.print(menu);
        assertThat(outContent.toString())
                .isNotBlank()
                .contains("Сходить в магазин")
                .endsWith("\n");
    }

    @Test
    public void whenLastRowStartWithTreeTabThenPrint() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        Printer printer = new Printer();
        printer.print(menu);
        assertThat(outContent.toString()).isEqualTo(
                """
                        \t1.Сходить в магазин
                        \t\t1.1.Купить продукты
                        \t\t\t1.1.1.Купить хлеб
                        \t\t\t1.1.2.Купить молоко
                        """
        );
    }
}