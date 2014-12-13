package iotGUI.ExecutionEngine

import scala.collection.mutable.ListBuffer

import iotGUI.Output._

object eRLType extends Enumeration
{
	type eRLType = Value
	val	eRLSection, eRLItem, eRLSensorConn, eRLSensorReadWrite, eRLGroupConn, eRLGroupReadWrite, eRLIfThenElse, eRLFor, eRLWhile, eRLEnd, eRLWait, eRLTweet = Value
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
		
		this.riType match {
			case 1 =>
				println("Starting execution")
			case 2 =>
				println("Connecting to sensor")
			case 3 =>
				println("Reading from sensor")
			case 11 =>
				println("Sending tweet")
				OutputNode_Twitter.SendTweet("Alert!")
			case 12 =>
				println("Debug output")
			case _ =>
				println("Executing process node")
		}
		
		if (aNode != null)
		{
			resultNode = aNode.processNode()
		}

		return resultNode
	}
}
