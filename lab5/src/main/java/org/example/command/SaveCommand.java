package org.example.command;

import org.example.repositories.OrganizationRepository;
import org.example.repositories.ProductRepository;

import java.io.IOException;

public class SaveCommand implements Command{
    private final ProductRepository repository ;
    private final OrganizationRepository organizationRepository;

    public SaveCommand(ProductRepository repository, OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }



    @Override
    public void invoke(String params) {
        try{
            repository.save();
            organizationRepository.save();
            System.out.println("Сохранили в products.json");
            System.out.println("Сохранили в organizations.json");
        }
        catch (IOException exception){
            System.out.println("Ошибка при работе с файлами");

        }

    }

    @Override
    public String commandName() {
        return "save";
    }
}
