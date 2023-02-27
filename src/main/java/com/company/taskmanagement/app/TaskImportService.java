package com.company.taskmanagement.app;

import com.company.taskmanagement.entity.Project;
import com.company.taskmanagement.entity.Task;
import io.jmix.core.DataManager;
import io.jmix.core.EntitySet;
import io.jmix.core.SaveContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("tm_TaskImportService")
public class TaskImportService {
    private final DataManager dataManager;

    public TaskImportService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public int importTasks() {
        List<String> names = generateTaskNames();
        List<Task> tasks = names.stream()
                .map(name -> {
                    Task task = dataManager.create(Task.class);
                    task.setName(name);
                    task.setProject(loadDefaultProject());
                    return task;
                }).collect(Collectors.toList());

        EntitySet entitySet = dataManager.save(new SaveContext().saving(tasks));

        return entitySet.size();
    }

    private List<String> generateTaskNames() {
        List<String> result = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            result.add(RandomStringUtils.randomAlphabetic(5));
        }
        return result;
    }

    @Nullable
    protected Project loadDefaultProject() {
        return dataManager.load(Project.class)
                .query("select p from tm_Project p where p.defaultProject = :defaultProject")
                .parameter("defaultProject", true)
                .optional().orElse(null);
    }
}