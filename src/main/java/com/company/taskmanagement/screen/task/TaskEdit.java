package com.company.taskmanagement.screen.task;

import com.company.taskmanagement.entity.Project;
import com.company.taskmanagement.entity.TaskPriority;
import com.company.taskmanagement.entity.User;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.company.taskmanagement.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("tm_Task.edit")
@UiDescriptor("task-edit.xml")
@EditedEntityContainer("taskDc")
public class TaskEdit extends StandardEditor<Task> {

    @Autowired
    private CurrentUserSubstitution currentAuthentication;
    @Autowired
    private ComboBox<TaskPriority> priorityField;

    @Autowired
    private CollectionContainer<Project> projectsDc;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Task> event) {
        User user = (User) currentAuthentication.getSubstitutedUser();
        Task task = event.getEntity();
        task.setAssignee(user);
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        
    }
    
    

    @Subscribe(id = "taskDc", target = Target.DATA_CONTAINER)
    public void onTaskDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<Task> event) {
        if ("project".equals(event.getProperty())) {
            Project project = (Project) event.getValue();
            if (project != null) {
                Task item = event.getItem();
                TaskPriority defaultPriorty = project.getDefaultPriorty();
                if (defaultPriorty != null) {
                    item.setPriority(defaultPriorty);
                }
            }
        }
    }

   /* @Subscribe("projectField")
    public void onProjectFieldValueChange(HasValue.ValueChangeEvent<Project> event) {
        Project project = event.getValue();
        if (project != null) {
            TaskPriority defaultPriorty = project.getDefaultPriorty();
            priorityField.setValue(defaultPriorty);
        }
    }*/
}