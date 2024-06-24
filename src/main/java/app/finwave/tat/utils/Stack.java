package app.finwave.tat.utils;

import java.util.*;
import java.util.function.Predicate;

public class Stack<T> {
    protected ArrayList<T> stack = new ArrayList<>();
    protected int maxSize = 0;

    public Stack(int maxSize) {
        this.maxSize = maxSize;
    }

    public Stack() {
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;

        if (maxSize > 0 && size() > maxSize) {
            int toRemove = size() - maxSize;

            stack.removeAll(stack.subList(0, toRemove));
        }
    }

    public void push(T item) {
        stack.add(0, item);

        if (maxSize <= 0 || stack.size() <= maxSize)
            return;

        stack.remove(size() - 1);
    }

    public T pop() {
        return stack.isEmpty() ? null : stack.remove(0);
    }

    public T peek() {
        return stack.isEmpty() ? null : stack.get(0);
    }

    public boolean remove(int index) {
        if (index < 0 || index >= size())
            return false;

        stack.remove(index);

        return true;
    }

    public boolean remove(Predicate<? super T> predicate) {
        return stack.removeIf(predicate);
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
