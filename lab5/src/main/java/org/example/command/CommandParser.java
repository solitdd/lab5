package org.example.command;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    private final Map<String,Command> commands=new HashMap<>();


    public void register(Command command){
        commands.put(command.commandName(),command);
    }
    public void execute(String line){

        if(line==null||line.trim().isEmpty()){
            return;
        }

        String trimmed=line.trim();
        String[] commandParts=trimmed.split(" ",2);
        String commandName=commandParts[0];
        String params=commandParts.length>1?commandParts[1]:"";
        Command command=commands.get(commandName);
        if(command==null){
            System.out.println("Unknown command");
            return;
        }

        command.invoke(params);
    }
}
