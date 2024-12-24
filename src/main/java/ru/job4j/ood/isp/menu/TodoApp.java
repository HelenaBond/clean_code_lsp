package ru.job4j.ood.isp.menu;

public class TodoApp {

    private final Printer printer;
    private final Menu menu;

    public TodoApp(Printer printer, Menu menu) {
        this.printer = printer;
        this.menu = menu;
    }

    /**
     * Добавляет новый родительский пункт в меню.
     * @param parentName имя добавляемого родительского пункта.
     * @throws IllegalArgumentException если пункт с таким именем уже существует
     * или входящий параметр пустая строка или null.
     */
    public void addRootElement(String parentName, ActionDelegate actionDelegate) {
        if (parentName == null || parentName.isBlank()) {
            throw new IllegalArgumentException("Имя родительского пункта не может быть пустым.");
        }
        if (menu.select(parentName).isPresent()) {
            throw new IllegalArgumentException("Этот пункт уже существует.");
        }
        menu.add(Menu.ROOT, parentName, actionDelegate);
    }

    /**
     * Добавляет новый подпункт в меню.
     * @param parentName имя родительского пункта к которому будет добавлен подпункт.
     * @param childName имя добавляемого подпункта.
     * @throws IllegalArgumentException если подпункт с таким именем уже существует
     * или родительский пункт не существует.
     * или любой из входящих параметров пустая строка или null.
     */
    public void addElement(String parentName, String childName, ActionDelegate actionDelegate) {
        if (parentName == null || parentName.isBlank()) {
            throw new IllegalArgumentException("Имя родительского пункта не может быть пустым.");
        }
        if (childName == null || childName.isBlank()) {
            throw new IllegalArgumentException("Имя подпункта не может быть пустым.");
        }
        if (menu.select(childName).isPresent()) {
            throw new IllegalArgumentException("Этот подпункт уже существует.");
        }
        boolean result = menu.add(parentName, childName, actionDelegate);
        if (!result) {
            throw new IllegalArgumentException("Не удалось добавить подпункт. Проверьте название родительского пункта.");
        }
    }

    /**
     * Вызывает действие привязанное к пункту меню.
     * @param itemName имя пункта.
     * @throws IllegalArgumentException если имя пункта пустое или null или не существует в меню.
     */
    public void getAction(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Имя пункта не может быть пустым.");
        }
        Menu.MenuItemInfo item = menu.select(itemName).orElseThrow(
                () -> new IllegalArgumentException(
                        "Пункт с именем %s не существует. Проверьте название пункта."
                                .formatted(itemName)
                ));
        item.getActionDelegate().delegate();
    }

    public void printAllElements() {
        printer.print(menu);
    }
}
