package ru.job4j.ood.isp.menu;

import java.util.*;

public class SimpleMenu implements Menu {

    private final List<MenuItem> rootElements = new ArrayList<>();

    @Override
    public boolean add(String parentName, String childName, ActionDelegate actionDelegate) {
        MenuItem current = new SimpleMenuItem(childName, actionDelegate);
        Optional<ItemInfo> itemInfo = findItem(parentName);
        if (itemInfo.isPresent()) {
            List<MenuItem> children = itemInfo.get().menuItem.getChildren();
            children.add(current);
        } else if (parentName == null) {
            rootElements.add(current);
        }
        return itemInfo.isPresent() || parentName == null;
    }

    @Override
    public Optional<MenuItemInfo> select(String itemName) {
        Optional<ItemInfo> itemInfo = findItem(itemName);
        Optional<MenuItemInfo> result = Optional.empty();
        if (itemInfo.isPresent()) {
            ItemInfo info = itemInfo.get();
            result = Optional.of(new MenuItemInfo(info.menuItem, info.number));
        }
        return result;
    }

    @Override
    public Iterator<MenuItemInfo> iterator() {
        return new Iterator<MenuItemInfo>() {
            private final Iterator<ItemInfo> infoIterator = new DFSIterator();

            public Iterator<ItemInfo> getInfoIterator() {
                return infoIterator;
            }

            @Override
            public boolean hasNext() {
                return infoIterator.hasNext();
            }

            @Override
            public MenuItemInfo next() {
                ItemInfo info = infoIterator.next();
                return new MenuItemInfo(info.menuItem, info.number);
            }
        };
    }

    private Optional<ItemInfo> findItem(String name) {
        Iterator<ItemInfo> iterator = new DFSIterator();
        Optional<ItemInfo> result = Optional.empty();
        while (iterator.hasNext()) {
            ItemInfo current = iterator.next();
            if (current.menuItem.getName().equals(name)) {
                result = Optional.of(current);

            }
        }
        return result;
    }

    private static class SimpleMenuItem implements MenuItem {

        private final String name;
        private final List<MenuItem> children = new ArrayList<>();
        private final ActionDelegate actionDelegate;

        public SimpleMenuItem(String name, ActionDelegate actionDelegate) {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<MenuItem> getChildren() {
            return children;
        }

        @Override
        public ActionDelegate getActionDelegate() {
            return actionDelegate;
        }
    }

    private class DFSIterator implements Iterator<ItemInfo> {

        private final Deque<MenuItem> stack = new LinkedList<>();

        private final Deque<String> numbers = new LinkedList<>();

        DFSIterator() {
            int number = 1;
            for (MenuItem item : rootElements) {
                stack.addLast(item);
                numbers.addLast(String.valueOf(number++).concat("."));
            }
        }

        public Deque<MenuItem> getStack() {
            return stack;
        }

        public Deque<String> getNumbers() {
            return numbers;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public ItemInfo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            MenuItem current = stack.removeFirst();
            String lastNumber = numbers.removeFirst();
            List<MenuItem> children = current.getChildren();
            int currentNumber = children.size();
            var iterator = children.listIterator(children.size());
            while (iterator.hasPrevious()) {
                stack.addFirst(iterator.previous());
                numbers.addFirst(lastNumber.concat(String.valueOf(currentNumber--)).concat("."));
            }
            return new ItemInfo(current, lastNumber);
        }
    }

    private static class ItemInfo {

        private final MenuItem menuItem;
        private final String number;

        public ItemInfo(MenuItem menuItem, String number) {
            this.menuItem = menuItem;
            this.number = number;
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        public String getNumber() {
            return number;
        }
    }
}
