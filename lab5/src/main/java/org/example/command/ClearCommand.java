package org.example.command;

import org.example.repositories.OrganizationRepository;
import org.example.repositories.ProductRepository;

public class ClearCommand implements Command {

    private final ProductRepository repository;
    private final OrganizationRepository organizationRepository;

    public ClearCommand(ProductRepository repository, OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }



    @Override
    public void invoke(String params) {
        repository.clear();
        organizationRepository.clear();
        System.out.println("Коллекция очищена");

    }

    @Override
    public String commandName() {
        return "clear";
    }
}

