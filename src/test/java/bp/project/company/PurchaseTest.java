package bp.project.company;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.RequiredHistoryLevel;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Ignore;

import java.util.List;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.junit.Assert.assertEquals;

@RequiredHistoryLevel(ProcessEngineConfiguration.HISTORY_AUDIT)
public class PurchaseTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @Deployment(resources = {"Purchase.bpmn"})
    public void shouldFailToOrderForKowalski() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        HistoryService historyService = processEngineRule.getHistoryService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("order_contractorName", "Kowalski")
                .putValue("order_order_bedAmount", 2)
                .putValue("order_wardrobeAmount", 0);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Purchase", variablesIn);

        assertThat(processInstance).isEnded();
    }

    @Ignore
    @Test
    @Deployment(resources = {"Transaction.bpmn"})
    public void shouldAddTransactionId() throws InterruptedException {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        HistoryService historyService = processEngineRule.getHistoryService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("parentBusinessKey", "123456789");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Transaction", variablesIn);

        // TODO fix
        assertThat(processInstance).isEnded();

        List<HistoricVariableInstance> transactionIds = historyService.createHistoricVariableInstanceQuery().variableName("").list();

        assertEquals(1, transactionIds.size());

        HistoricVariableInstance lastTransactionId = transactionIds.get(0);

        assertEquals("GHX-12345", lastTransactionId.getValue().toString());

    }
}
