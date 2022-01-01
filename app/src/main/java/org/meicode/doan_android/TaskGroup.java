package org.meicode.doan_android;

public class TaskGroup {
    String GroupTask;
    String TaskTime;
    String IdParent;
    public TaskGroup(String GroupTask, String TaskTime, String IdParent)
    {
        this.GroupTask = GroupTask;
        this.TaskTime = TaskTime;
        this.IdParent = IdParent;
    }

    public String getIdParent() {
        return IdParent;
    }

    public String getGroupTask() {
        return GroupTask;
    }

    public String getTaskTime() {
        return TaskTime;
    }
}