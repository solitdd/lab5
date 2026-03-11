package org.example.command;

import java.io.File;
import java.util.Scanner;

public class ExecuteScriptCommand implements Command{
    private final CommandParser commandParser;

    public ExecuteScriptCommand(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    @Override
    public void invoke(String params) {
        File file = new File(params);
        if(!file.exists()){
            System.out.println("Скрипт не найден.");
            return;
        }
        try(Scanner scanner=new Scanner(file)) {
            while (scanner.hasNextLine()){
                String line = scanner.nextLine().trim();
                if(line.isEmpty()){
                    continue;
                }
                System.out.println("> "+line);
                commandParser.execute(line);
            }
        }catch (Exception e){
            System.out.println("Ошибка при исполнении скрипта");
        }

    }

    @Override
    public String commandName() {
        return "execute_script";
    }
}
