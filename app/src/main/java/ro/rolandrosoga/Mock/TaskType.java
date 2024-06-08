package ro.rolandrosoga.Mock;

import java.io.Serializable;


public class TaskType implements Serializable {

    private int taskType_id;
    private int task_id_value;

    public TaskType() {

    }

    public TaskType(int taskType_id, int task_id_value) {
        this.taskType_id = taskType_id;
        this.task_id_value = task_id_value;
    }

    public int getTaskType_id() {
        return taskType_id;
    }

    public void setTaskType_id(int taskType_id) {
        this.taskType_id = taskType_id;
    }

    public int getTask_id_value() {
        return task_id_value;
    }

    public void setTask_id_value(int task_id_value) {
        this.task_id_value = task_id_value;
    }

}