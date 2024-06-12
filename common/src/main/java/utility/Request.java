package utility;

import models.StudyGroup;

import java.io.Serializable;
import java.util.Objects;

public class Request implements Serializable {
    private String commandName;
    private String args = ""; // для команд без аргументов
    private StudyGroup studyGroup;
    private User user;

    public Request(String commandName, User user) { // для команд без аргументов
        this.commandName = commandName;
        this.user = user;
    }
    public Request(String commandName, String args, User user) { // для команд с аргументами, но не требующие объект StudyGroup
        this.commandName = commandName;
        this.args = args;
        this.user = user;
    }
    public Request(String commandName, StudyGroup studyGroup, User user) { // для add и remove_greater
        this.commandName = commandName;
        this.studyGroup = studyGroup;
        this.user = user;
    }

    public Request(String commandName, String args, StudyGroup studyGroup, User user) { // для update_id
        this.commandName = commandName;
        this.args = args;
        this.studyGroup = studyGroup;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
