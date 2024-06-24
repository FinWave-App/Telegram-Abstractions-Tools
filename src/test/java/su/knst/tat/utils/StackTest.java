package su.knst.tat.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {
    @Test
    void getMaxSize() {
        Stack<Integer> stack = new Stack<>();
        assertEquals(0, stack.getMaxSize());

        stack.setMaxSize(15);
        assertEquals(15, stack.getMaxSize());
    }

    @Test
    void setMaxSize() {
        Stack<Integer> stack = new Stack<>();
        stack.setMaxSize(3);

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        assertEquals(3, stack.size());

        stack.push(5);

        assertEquals(3, stack.size());

        stack.setMaxSize(2);
        assertEquals(2, stack.size());

        stack.push(6);
        assertEquals(2, stack.size());
    }

    @Test
    void push() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);

        assertEquals(2, stack.peek());
    }

    @Test
    void pop() {
        Stack<Integer> stack = new Stack<>();
        assertNull(stack.pop());

        stack.push(1);
        stack.push(2);

        assertEquals(2, stack.pop());

        assertEquals(1, stack.size());
        assertEquals(1, stack.pop());

        assertEquals(0, stack.size());
    }

    @Test
    void peek() {
        Stack<Integer> stack = new Stack<>();
        assertNull(stack.peek());

        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.peek());
        assertEquals(3, stack.size());
    }

    @Test
    void isEmpty() {
        Stack<Integer> stack = new Stack<>();

        stack.push(1);

        assertFalse(stack.isEmpty());

        stack.pop();

        assertTrue(stack.isEmpty());
    }

    @Test
    void size() {
        Stack<Integer> stack = new Stack<>();

        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.size());
    }

    @Test
    void clear() {
        Stack<Integer> stack = new Stack<>();

        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, stack.size());

        var oldStack = stack.clear();

        assertEquals(0, stack.size());

        assertEquals(3, oldStack.size());
    }

    @Test
    void popOptional() {
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.popOptional().isEmpty());

        stack.push(1);

        assertTrue(stack.popOptional().isPresent());

        assertTrue(stack.popOptional().isEmpty());
    }

    @Test
    void peekOptional() {
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.peekOptional().isEmpty());

        stack.push(1);

        assertTrue(stack.peekOptional().isPresent());
        assertTrue(stack.peekOptional().isPresent());
    }
}