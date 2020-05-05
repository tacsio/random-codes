package io.tacsio;

import java.util.function.*;

public class Main {

    public static void main(String[] args) {
        //functional interfaces can be implemented by a lambda expression
        System.out.println("Functional interface");
        MyFunctionalInterface lambda = () -> System.out.println("Simple functional interface execution");
        lambda.execute();

        //built-in Function
        System.out.println("Function");
        Function<Long, Long> doubled = new DoubleLongFunction();
        System.out.println(doubled.apply(2L));

        //Function interface can be implemented by a lambda expression
        Function<Integer, Integer> increment = (val) -> val + 1;
        System.out.println(increment.apply(1));

        //Predicates can be implemented by a lambda expression
        System.out.println("Predicates");
        Predicate checkNotNullInline = (it) -> it != null;
        Object test = null;
        System.out.println(checkNotNullInline.test(test));
        System.out.println(new CheckNullPredicate().test(test));

        //Unary operator can be implemented by a lambda expression
        System.out.println("Unary Operator");
        UnaryOperator<Person> childish = (person) -> new Person(person.age - 1);
        Person person = new Person(20);
        System.out.println(new GrowupUnaryOperator().apply(person).age);
        System.out.println(childish.apply(person).age);

        //Binary operator can be implemented by a lambda expression
        //Both parameters and the return type must be of the same type.
        System.out.println("Binary Operator");
        BinaryOperator<Integer> sum = (x, y) -> x + y;
        System.out.println(new MultiBinaryOperator().apply(2, 3));
        System.out.println(sum.apply(2, 3));

        //Supplier interface is a functional interface that represents an function that supplies a value of some sorts
        System.out.println("Supplier");
        Supplier<Integer> random = () -> (int) (Math.random() * 100D);
        System.out.println(random.get());

        //Consumer is a functional interface that represents an function that consumes a value without returning any value
        System.out.println("Consumer");
        Consumer<String> print = (val) -> System.out.println(val);
        print.accept("Hello consumer!");
    }
}

//functional interface
interface MyFunctionalInterface {
    void execute();
}

//built-in Function
class DoubleLongFunction implements Function<Long, Long> {

    @Override
    public Long apply(Long aLong) {
        return aLong * 2;
    }
}

//built-in Predicate
class CheckNullPredicate implements Predicate {
    @Override
    public boolean test(Object o) {
        return o == null;
    }
}

//built-in UnaryOperator
class GrowupUnaryOperator implements UnaryOperator<Person> {

    @Override
    public Person apply(Person person) {
        return new Person(person.age + 1);
    }
}

//built-in BinaryOperator
class MultiBinaryOperator implements BinaryOperator<Integer> {

    @Override
    public Integer apply(Integer integer, Integer integer2) {
        return integer * integer2;
    }
}