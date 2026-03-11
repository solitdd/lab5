package org.example.command;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.models.Organization;
import org.example.models.OrganizationWrapper;
import org.example.models.Product;
import org.example.models.ProductWrapper;
import org.example.repositories.OrganizationRepository;
import org.example.repositories.ProductRepository;
import org.example.utils.JsonUtil;


public class AddCommand implements Command {
    private final ProductRepository repository;

    private final OrganizationRepository orgRepository;

    public AddCommand(ProductRepository repository,  OrganizationRepository orgRepository) {
        this.repository = repository;

        this.orgRepository = orgRepository;
    }



    @Override
    public void invoke(String params) {
        if(params.contains("product")){
            try {
                Product product = JsonUtil.readFromString(params, new TypeReference<ProductWrapper>(){}).getProduct();


                Organization manufacturer = product.getManufacturer();
                if (manufacturer != null) {
                    Organization existingOrg = orgRepository.getById(manufacturer.getId());
                    if (existingOrg != null) {

                        product.setManufacturer(existingOrg);
                    } else {

                        orgRepository.add(manufacturer);
                    }
                }
                if(repository.add(product)){
                    System.out.println("Продукт успешно добавлен");
                }else {
                    System.out.println("Продукт не добавлен");
                }


            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
        else if(params.contains("organization")){
            try {
                Organization organization = JsonUtil.readFromString(params, new TypeReference<OrganizationWrapper>() {}).getOrganization();
                orgRepository.add(organization);
            }
            catch (Exception e){
                System.out.println("Ошибка ввода: " + e.getMessage());
            }
        }

    }

    @Override
    public String commandName() {
        return "add";
    }
}