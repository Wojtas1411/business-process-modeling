package bp.project.company;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.Map;

public class TransactionRequest implements JavaDelegate {

    public void execute(DelegateExecution delegateExecution) throws Exception {
        RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        Map<String, Object> processVariables = new HashMap<String, Object>();
        processVariables.put("parentBusinessKey", delegateExecution.getProcessInstanceId());
//        processVariables.put("decyzja_czyPozytywna", delegateExecution.getVariable("decyzja_czyPozytywna"));
//        processVariables.put("decyzja_uzasadnienie", delegateExecution.getVariable("decyzja_uzasadnienie"));

        runtimeService.startProcessInstanceByMessage("Transaction Start", processVariables);
    }
}
