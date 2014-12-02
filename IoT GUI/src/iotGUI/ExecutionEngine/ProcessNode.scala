package iotGUI.ExecutionEngine

import scala.collection.mutable.ListBuffer

object eRLType extends Enumeration
{
	type eRLType = Value
	val	eRLSection, eRLItem = Value
}

import eRLType._

class ProcessNode
{
	var	UID: Long = 0
	// Repository list information
	var rlType: eRLType = eRLItem
	var	rlSectionID: String = ""
	var rlToolTip: String = ""
	// Repository item details
	var	riName: String = ""
	var riType: Int = 0

	var	riInputConn: ListBuffer[ProcessNode] = new ListBuffer[ProcessNode]
	var riOutputConn: ProcessNode = null

	override def clone(): ProcessNode =
	{
		var newRLO = new ProcessNode

		newRLO.UID = UID
		newRLO.rlType = rlType
		newRLO.rlSectionID = rlSectionID
		newRLO.rlToolTip = rlToolTip
		newRLO.riName = riName
		newRLO.riType =riType

		return newRLO
	}

	def addInputConnection(inIC: ProcessNode)
	{
		riInputConn += inIC
	}

	def addOutputConnection(inOC: ProcessNode)
	{
		riOutputConn = inOC
	}


	// This is where the process runs
	def processNode(): ProcessNode =
	{
		var aNode: ProcessNode = riOutputConn
		var resultNode: ProcessNode = null

		if (aNode != null)
		{
			resultNode = aNode.processNode()
		}

		return resultNode
	}
}
