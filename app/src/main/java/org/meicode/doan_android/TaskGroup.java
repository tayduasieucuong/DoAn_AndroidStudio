package org.meicode.doan_android;

public class TaskGroup {
    String GroupName;
    String GroupId;
    public TaskGroup(String GroupId, String GroupName)
    {
        this.GroupName = GroupName;
        this.GroupId = GroupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public String getGroupId() {
        return GroupId;
    }
}