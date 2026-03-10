package org.example.command;

import org.example.repositories.ProductRepository;

import java.util.Scanner;

public class RemoveByIdCommand implements Command {

    private final Scanner scanner;
    private final ProductRepository repository;

    public RemoveByIdCommand(Scanner scanner, ProductRepository repository) {
        this.scanner = scanner;
        this.repository = repository;
    }


    @Override
    public void invoke(String params) {
        try {
            repository.remove(Integer.parseInt(params));
            System.out.println("Успешно удалено");
        }
        catch (Exception e){
            System.out.println("Ошибка: "+ e.getMessage());
        }
    }

    @Override
    public String commandName() {
        return "remove_by_id";
    }
}
