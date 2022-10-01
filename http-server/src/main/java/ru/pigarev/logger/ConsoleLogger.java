package ru.pigarev.logger;

public class ConsoleLogger implements Logger {
    @Override
    public void info(String msg) {

        System.out.println(msg);
    }
}
