package mnj.ont.view.backing;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class CopyPost {
    private RichTable queryTable;

    public CopyPost() {
    }

    public void setQueryTable(RichTable queryTable) {
        this.queryTable = queryTable;
    }

    public RichTable getQueryTable() {
        return queryTable;
    }

    public String Copy() {
        // Add event code here...
        
        OperationBinding operationBinding = executeOperation("HeaderActions");         
        operationBinding.getParamsMap().put("type", "C"); //bomLineId
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(queryTable);
        return null;
    }

    public String Post() {
        OperationBinding operationBinding = executeOperation("HeaderActions");         
        operationBinding.getParamsMap().put("type", "V"); //bomLineId   
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(queryTable);
        return null;
    }
    
    /*****Generic Method to Get BindingContainer**/
        public BindingContainer getBindingsCont() {
            return BindingContext.getCurrent().getCurrentBindingsEntry();
        }

        /**
         * Generic Method to execute operation
         * */
        public OperationBinding executeOperation(String operation) {
            OperationBinding createParam = getBindingsCont().getOperationBinding(operation);
            return createParam;

        }
}
