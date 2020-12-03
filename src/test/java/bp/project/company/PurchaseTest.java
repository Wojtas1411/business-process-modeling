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
import static org.junit.Assert.assertNotNull;

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
                .putValue("order_bedAmount", 2)
                .putValue("order_wardrobeAmount", 0);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Purchase", variablesIn);

        assertThat(processInstance).isEnded();
    }

    @Test
    @Deployment(resources = {"Magazine.bpmn"})
    public void shouldThrowErrorDueToMissingProducts() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        HistoryService historyService = processEngineRule.getHistoryService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("order_bedAmount", 7)
                .putValue("order_wardrobeAmount", 5);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Magazine", variablesIn);

        assertThat(processInstance).isEnded();

        List<HistoricVariableInstance> totalCountHistory = historyService.createHistoricVariableInstanceQuery().variableName("total_count").list();
        assertEquals(1, totalCountHistory.size());

        assertEquals(12L, totalCountHistory.get(0).getValue());

    }

    @Test
    @Deployment(resources = {"Purchase.bpmn", "OrderAssessment.dmn", "Transaction.bpmn", "Magazine.bpmn"})
    public void shouldFailToOrderWhenOrderTooBig() {
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        HistoryService historyService = processEngineRule.getHistoryService();
        VariableMap variablesIn = Variables.createVariables()
                .putValue("order_contractorName", "IKEA")
                .putValue("order_bedAmount", 7)
                .putValue("order_wardrobeAmount", 5);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Purchase", variablesIn);

//        assertThat(processInstance).isEnded();
//        assertThat(processInstance).variables().containsEntry("needs_advance_payment", Boolean.TRUE);
//        TODO
    }

}
