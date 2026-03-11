package org.example.command;

public interface Command {

     void invoke(String params);
     String commandName();
}
