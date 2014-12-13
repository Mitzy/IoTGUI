package iotGUI.ExecutionEngine

class ProcessNode_Debug extends ProcessNode
{
 // This is where the process runs
	override def processNode(): ProcessNode =
	{
		println("Debug line")
		
		return super.processNode()
	}
}