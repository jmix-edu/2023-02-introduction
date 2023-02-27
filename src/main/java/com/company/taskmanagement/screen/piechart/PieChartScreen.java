package com.company.taskmanagement.screen.piechart;

import com.company.taskmanagement.entity.TaskPriority;
import com.google.common.base.Strings;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.ValueLoadContext;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@UiController("tm_PieChartScreen")
@UiDescriptor("pie-chart-screen.xml")
public class PieChartScreen extends Screen {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Messages messages;

    @Install(to = "tasksDl", target = Target.DATA_LOADER)
    private List<KeyValueEntity> tasksDlLoadDelegate(ValueLoadContext valueLoadContext) {
        return dataManager.loadValues(valueLoadContext)
                .stream()
                .peek(entity-> {
                    String priorityId = entity.getValue("priority");
                    String localizedCaption;
                    if (Strings.isNullOrEmpty(priorityId)) {
                        localizedCaption = "No priority";
                    } else {
                        localizedCaption = messages.getMessage(TaskPriority.fromId(priorityId));
                    }
                    entity.setValue("priority", localizedCaption);
                }).collect(Collectors.toList());
    }
}