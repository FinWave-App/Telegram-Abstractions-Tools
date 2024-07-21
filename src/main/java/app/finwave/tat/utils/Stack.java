package app.finwave.tat.utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Stack<T> {
    protected ArrayList<T> stack = new ArrayList<>();
    protected int maxSize = 0;
    protected Consumer<T> lastItemWatcher;
    protected T lastProvidedItem;

    public Stack(int maxSize) {
        this.maxSize = maxSize;
    }

    public Stack() {
    }

    public void setLastItemWatcher(Consumer<T> lastItemWatcher) {
        this.lastItemWatcher = lastItemWatcher;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        if (maxSize < 1)
            throw new IllegalArgumentException("maxSize must be greater than 0");

        this.maxSize = maxSize;
        int currentSize = stack.size();

        if (currentSize > maxSize) {
            stack.subList(maxSize, currentSize).clear();

            if (lastItemWatcher != null && maxSize == 1) {
                lastProvidedItem = peek();
                lastItemWatcher.accept(lastProvidedItem);
            }
        }
    }

    public void push(T item) {
        stack.add(0, item);

        if (lastItemWatcher != null) {
            lastProvidedItem = item;
            lastItemWatcher.accept(lastProvidedItem);
        }

        if (maxSize <= 0 || stack.size() <= maxSize)
            return;

        stack.remove(size() - 1);
    }

    public T pop() {
        T result = stack.isEmpty() ? null : stack.remove(0);

        if (lastItemWatcher != null && result != null) {
            lastProvidedItem = peek();
            lastItemWatcher.accept(lastProvidedItem);
        }

        return result;
    }

    public T peek() {
        return stack.isEmpty() ? null : stack.get(0);
    }

    public boolean remove(int index) {
        if (index < 0 || index >= size())
            return false;

        stack.remove(index);

        if (index == 0) {
            lastProvidedItem = peek();
            lastItemWatcher.accept(lastProvidedItem);
        }

        return true;
    }

    public boolean remove(Predicate<? super T> predicate) {
        boolean result = stack.removeIf(predicate);

        if (result && lastItemWatcher != null) {
            T lastItem = peek();

            if (lastProvidedItem != lastItem) {
                lastProvidedItem = lastItem;
                lastItemWatcher.accept(lastProvidedItem);
            }
        }

        return result;
    }

    public List<T> raw() {
        return Collections.unmodifiableList(stack);
    }

    public Optional<T> popOptional() {
        return Optional.ofNullable(pop());
    }

    public Optional<T> peekOptional() {
        return Optional.ofNullable(peek());
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }

    public List<T> clear() {
        List<T> oldStack = List.copyOf(stack);

        stack.clear();

        if (lastItemWatcher != null) {
            lastProvidedItem = null;
            lastItemWatcher.accept(null);
        }

        return oldStack;
    }

    @Override
    public String toString() {
        return "Stack{" +
                "stack=" + stack +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stack<?> stack1 = (Stack<?>) o;
        return Objects.equals(stack, stack1.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stack.hashCode());
    }
}
