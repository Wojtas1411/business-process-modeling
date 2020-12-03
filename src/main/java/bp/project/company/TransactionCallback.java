package bp.project.company;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class TransactionCallback implements JavaDelegate {

    public void execute(DelegateExecution delegateExecution) throws Exception {
        RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
        runtimeService.createMessageCorrelation("Transaction Result")
                .processInstanceId(delegateExecution.getVariable("parentBusinessKey").toString())
                .setVariable("transaction_number", delegateExecution.getVariable("transaction_number"))
                .correlateWithResult();
    }
}
