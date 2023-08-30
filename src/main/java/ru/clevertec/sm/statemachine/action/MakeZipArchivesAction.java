package ru.clevertec.sm.statemachine.action;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import ru.clevertec.sm.aspect.TimeProfilingAspect;
import ru.clevertec.sm.service.ZipService;
import ru.clevertec.sm.statemachine.Event;
import ru.clevertec.sm.statemachine.State;
import ru.clevertec.sm.util.ServiceConstants;
import ru.clevertec.sm.util.StateMachineUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MakeZipArchivesAction implements Action<State, Event> {

    private final ZipService zipService;
    private final TimeProfilingAspect profilingAspect;

    @Override
    public void execute(StateContext<State, Event> context) {
        File sourceFolder = new File(ServiceConstants.OUTPUT_PATH);
        Map<String, List<String>> archivesAndFilesNames = new HashMap<>();
        List<File> folders = extractDirectories(sourceFolder);
        for (File folder : folders) {
            zipService.createArchive(folder, ServiceConstants.CSV_EXTENSION)
                    .ifPresent(e -> archivesAndFilesNames.put(e.getKey(), e.getValue()));
        }
        var stateMachine = context.getStateMachine();
        StateMachineUtil.putVariable(
                stateMachine,
                ServiceConstants.GENERATED_ARCHIVES_AND_FILES,
                archivesAndFilesNames
        );
        StateMachineUtil.putVariable(stateMachine, ServiceConstants.CREATION_REPORT, profilingAspect.getReportBuilder());
        StateMachineUtil.sendEvent(stateMachine, Event.FINISH_ZIP_ARCHIVES);
    }

    private List<File> extractDirectories(File folder) {
        return Optional.ofNullable(folder.listFiles())
                .stream()
                .flatMap(Stream::of)
                .filter(File::isDirectory)
                .toList();
    }
}
