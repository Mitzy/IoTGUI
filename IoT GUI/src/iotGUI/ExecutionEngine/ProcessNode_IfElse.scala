package iotGUI.ExecutionEngine
	
class IfElse
{
    def If(): Int =
    {
        return 0
    }

    def Then(): Int =
    {
        return 0
    }

    def Else(): Int =
    {
        return 0
    }

}

class ProcessNode_IfElse extends ProcessNode
{
    var    action: Int = 0
    var myIfElse: IfElse = null

    // This is where the process runs
    override def processNode(): ProcessNode =
    {
    	var x = 20
        // check for IfConditon and ElseCondition
        action match
        {
            case 1 =>
                //IfCondition()
            	if(x < 20)
            		super.processNode()
            case 2 =>
                //ElseCondition()
         }
        
        return super.processNode()
    }


    def IfCondition(): Int =
    {
        // check If Condition
        return myIfElse.If()
    }

    def ThenAction(): Int =
    {
        // Perform ThenAction
        return myIfElse.Then()
    }

    def ElseCondition(): Int =
    {
        // check else Condition
        return myIfElse.Else()
    }

}
