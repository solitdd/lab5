package org.example;

import org.example.command.*;
import org.example.repositories.OrganizationRepository;
import org.example.repositories.ProductRepository;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String productRepositoryFileName=args.length>0?args[0]:"products.json";
        String organizationRepositoryFileName=args.length>1?args[1]:"organizations.json";
        Scanner scanner =new Scanner(System.in);
        ProductRepository productRepository=new ProductRepository(productRepositoryFileName);
        OrganizationRepository organizationRepository= new OrganizationRepository(organizationRepositoryFileName);
        CommandParser parser=new CommandParser();

        parser.register(new HelpCommand());
        parser.register(new AddCommand(productRepository, organizationRepository));
        parser.register(new UpdateCommand(scanner, productRepository, organizationRepository));
        parser.register(new RemoveByIdCommand(scanner, productRepository));
        parser.register(new ShowCommand(productRepository));
        parser.register(new InfoCommand(productRepository));
        parser.register(new ClearCommand(productRepository,organizationRepository));
        parser.register(new ExitCommand());
        parser.register(new SaveCommand(productRepository,organizationRepository));
        parser.register(new RemoveLowerCommand(productRepository,organizationRepository));
        parser.register(new FilterLessThanManufacturerCommand(productRepository));
        parser.register(new AddIfMinCommand(productRepository,organizationRepository));
        parser.register(new RemoveGreaterCommand(productRepository,organizationRepository));
        parser.register(new RemoveAllByPartNumber(productRepository));
        parser.register(new CountGreaterThanUnitOfMeasureCommand(productRepository));
        parser.register(new ExecuteScriptCommand(parser));


        System.out.println("Введите команду");
        while(true){
            parser.execute(scanner.nextLine());

        }
    }
}