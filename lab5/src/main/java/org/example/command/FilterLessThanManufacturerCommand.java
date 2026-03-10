package org.example.command;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.models.Organization;
import org.example.models.Product;
import org.example.repositories.ProductRepository;
import org.example.utils.JsonUtil;

import java.util.TreeSet;

public class FilterLessThanManufacturerCommand implements Command{
    private final ProductRepository productRepository;
    public FilterLessThanManufacturerCommand (ProductRepository productRepository){
        this.productRepository=productRepository;
    }
    @Override
    public void invoke(String params) {
        try{
            Organization organization = JsonUtil.readFromString(params, new TypeReference<Organization>() {});
            TreeSet<Product> products=productRepository.getAll();
            for (Product product : products){
                if(product.getManufacturer().compareTo(organization)<0){
                    System.out.println(product);
                }
            }

        }catch (Exception e){
            System.out.println("Ошибка: "+e.getMessage());
        }

    }

    @Override
    public String commandName() {
        return "filter_less_than_manufacturer";
    }
}
