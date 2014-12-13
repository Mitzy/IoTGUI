package iotGUI.RepositoryPanel

import scala.swing._
import scala.collection.mutable.ListBuffer
import java.awt._
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JButton
import javax.swing.JTextArea
import javax.swing.JTextField
import scala.swing.Dialog
import scala.swing.Dialog.Message

import eRLZoneType._

class RIL_IfThenElse(inText: String, inBorderWeight: Int = 1) extends RepositoryItemLabel(inText, inBorderWeight)
{
	private var elseOutputChainLabel: RepositoryItemLabel = null

	override def clone(): RepositoryItemLabel =
	{
		var newRIL = new RIL_IfThenElse(text, borderWeight)

		newRIL.setProcessNode(myProcessNode.clone())

		return newRIL
	}

	override def getZone(inPoint: Point): eRLZoneType =
	{
		var	elseClickRect: Rectangle = new Rectangle()

		// Set bounds for the else output handle
		elseClickRect.setBounds((peer.bounds.getWidth() / 2 - (zoneHandleWidth / 2)).toInt, (peer.bounds.getHeight() - zoneHandleHeight).toInt, zoneHandleWidth, zoneHandleHeight)
		if (elseClickRect.contains(inPoint))
			return eRLZElseHandle

		return super.getZone(inPoint)
	}

	override def addOutputConnection(inRIL: RepositoryItemLabel, inZone: eRLZoneType = eRLZNone)
	{
		if (inZone == eRLZElseHandle)
		{
			elseOutputChainLabel = inRIL
//			getProcessNode.addOutputConnection(inRIL.getProcessNode)
		}
		else
		{
			outputChainLabel = inRIL
			getProcessNode.addOutputConnection(inRIL.getProcessNode)
		}
	}

	override def getZoneByConnectedItem(inRIL: RepositoryItemLabel): eRLZoneType =
	{
		if (inRIL == elseOutputChainLabel)
			return eRLZElseHandle

		return super.getZoneByConnectedItem(inRIL)
	}

	override def getAllOutputs(): ListBuffer[RepositoryItemLabel] =
	{
		var	myOutputs: ListBuffer[RepositoryItemLabel] = super.getAllOutputs

		myOutputs += elseOutputChainLabel

		return myOutputs
	}

	override def getZoneLocation(inZone: eRLZoneType): Point =
	{
		var	zoneLoc: Point = new Point

		inZone match
		{
			case `eRLZElseHandle` =>
				zoneLoc.setLocation(getWidth() /2, getHeight() - 1)

			case _ =>
				return super.getZoneLocation(inZone)
		}

		return zoneLoc
	}

	override def promptForParameters()
	{
		var myJPanel = new JPanel()
		var textArea: JTextArea = new JTextArea(6, 25)
		var	tempSensorList = Array[Object]("Temp01", "Temp02", "Gas01", "Gas02")
//		var longMessage = "This is a long text message that I would like to display inside a dialog.  Let's see if it works.  It seems to be OK as long as Java is used for the dialog and components; however, in Scala it does not work."

//        textArea.setText(longMessage)

		var myDialog: JDialog = new JDialog
		myDialog.setVisible(true)
/*
		var answer = Dialog.showMessage(this,
			myJPanel,
	        "Select sensor",
	        Message.Plain,
	        null/*,
	        tempSensorList,
			tempSensorList(2)*/);
*/
	}
}
