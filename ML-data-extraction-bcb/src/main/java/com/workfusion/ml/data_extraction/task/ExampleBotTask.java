package com.workfusion.ml.data_extraction.task;

import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.task.AdHocTask;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;

@BotTask
public class ExampleBotTask implements AdHocTask {

    @Override
    public TaskRunnerOutput run(TaskInput taskInput) {
        return taskInput.asResult()
                .withColumn("example_bot_task_output", "completed_successfully");
    }

}
