package org.meicode.doan_android;

public class TaskGroup {
    String GroupTask;
    String TaskTime;
    String IdParent;
    String Done;
    public TaskGroup(String GroupTask, String TaskTime, String IdParent,String Done)
    {
        this.GroupTask = GroupTask;
        this.TaskTime = TaskTime;
        this.IdParent = IdParent;
        this.Done = Done;
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

    public String getDone() {
            return Done;
    }
}