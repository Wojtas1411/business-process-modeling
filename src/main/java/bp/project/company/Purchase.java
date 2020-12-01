package bp.project.company;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

@ProcessApplication("Purchase")
public class Purchase extends ServletProcessApplication {

    @PostDeploy
    public void evaluateDecisionTable(ProcessEngine processEngine) {
        DecisionService decisionService = processEngine.getDecisionService();

        VariableMap variableMap = Variables.createVariables()
                .putValue("order_bedAmount", 2)
                .putValue("order_wardrobeAmount", 0);

        DmnDecisionTableResult tableResult = decisionService.evaluateDecisionTableByKey("order_assessment", variableMap);
        DmnDecisionRuleResult sr = tableResult.getSingleResult();

        // TODO


    }
}
