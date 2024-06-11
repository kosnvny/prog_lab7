package utility;

import models.StudyGroup;

import java.io.Serializable;
import java.util.Objects;

public class Request implements Serializable {
    private String commandName;
    private String args = ""; // для команд без аргументов
    private StudyGroup studyGroup;

    public Request(String commandName) { // для команд без аргументов
        this.commandName = commandName;
    }
    public Request(String commandName, String args) { // для команд с аргументами, но не требующие объект StudyGroup
        this.commandName = commandName;
        this.args = args;
    }
    public Request(String commandName, StudyGroup studyGroup) { // для add и remove_greater
        this.commandName = commandName;
        this.studyGroup = studyGroup;
    }

    public Request(String commandName, String args, StudyGroup studyGroup) { // для update_id
        this.commandName = commandName;
        this.args = args;
        this.studyGroup = studyGroup;
    }

    public boolean isEmpty() {
        return commandName.isEmpty() && args.isEmpty() && studyGroup == null;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(commandName, request.commandName) && Objects.equals(args, request.args) && Objects.equals(studyGroup, request.studyGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandName, args, studyGroup);
    }

    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", args='" + args + '\'' +
                ", studyGroup=" + studyGroup +
                '}';
    }
}
