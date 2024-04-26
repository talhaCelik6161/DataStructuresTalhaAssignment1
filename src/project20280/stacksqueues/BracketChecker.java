package project20280.stacksqueues;

import java.util.Stack;

class BracketChecker {
    private final String input;

    public BracketChecker(String in) {
        input = in;
    }

    public void check() {
        Stack<Character> stack = new Stack<>();

        for (char c : input.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) {
                    System.out.println("no. Missing left delimiter in: " + input);
                    return;
                }

                char top = stack.pop();
                if ((c == ')' && top != '(') || (c == ']' && top != '[') || (c == '}' && top != '{')) {
                    System.out.println("no. Matching error in: " + input);
                    return;
                }
            }
        }

        if (!stack.isEmpty()) {
            System.out.println("no. Missing right delimiter in: " + input);
            return;
        }

        System.out.println("yes. " + input);
    }

    public static void main(String[] args) {
        String[] inputs = {
                "[]]()()", // not correct
                "c[d]", // correct
                "a{b[c]d}e", // correct
                "a{b(c]d}e", // not correct; ] doesn't match (
                "a[b{c}d]e}", // not correct; nothing matches final }
                "a{b(c) ", // not correct; Nothing matches opening {
        };

        for (String input : inputs) {
            BracketChecker checker = new BracketChecker(input);
            System.out.println("Currently checking: " + input);
            checker.check();
        }
    }
}