package org.example.command;

public class ExitCommand implements Command {




    @Override
    public void invoke(String params) {
        System.out.println("Завершение программы");
        System.exit(0);
    }

    @Override
    public String commandName() {
        return "exit";
    }
}
