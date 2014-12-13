/**
 * 
 */
package iotGUI.RepositoryPanel

import scala.swing._
import scala.swing.event._
import scala.collection.mutable.ListBuffer
import java.awt.datatransfer.{DataFlavor, Transferable}
//import iotGUI.StagePanel._
//import iotGUI.ExecutionEngine._

/**
 * @author Robert Abatecola
 *
 */

object eRLZoneType extends Enumeration
{
	type eRLZoneType = Value
	val	eRLZBody, eRLZInputHandle, eRLZOutputHandle, eRLZElseHandle, eRLZNone = Value
}

import eRLZoneType._

object RepositoryItemLabel
{
	var riDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=iotGUI.RepositoryPanel.RepositoryItemLabel", "Repository Item Flavor");
	var myDataFlavors: Array[DataFlavor] = Array(riDataFlavor)
}

class RILEvent(inEvent: InputEvent, inSource: RepositoryItemLabel) extends scala.swing.event.Event
{
	var	myEvent: InputEvent = inEvent
	var source: RepositoryItemLabel = inSource
}

class RepositoryItemLabel(inText: String, inBorderWeight: Int = 1) extends RepositoryLabel(inText, inBorderWeight) with Transferable
{
	var dragstart: Point = null
	var	mouseClickedZone: eRLZoneType = eRLZNone
	var	mouseReleasedZone: eRLZoneType = eRLZNone
	var outputChainLabel: RepositoryItemLabel = null
	var inputChainLabel: RepositoryItemLabel = null

	override def clone(): RepositoryItemLabel =
	{
		var newRIL = new RepositoryItemLabel(text, borderWeight)

		newRIL.setProcessNode(myProcessNode.clone())

		return newRIL
	}

	override def getTransferData(flavor: DataFlavor): Object =
	{
		var	newRILabel: RepositoryItemLabel = this.clone()

		return newRILabel
	}

	override def getTransferDataFlavors(): Array[java.awt.datatransfer.DataFlavor] =
	{
		return RepositoryItemLabel.myDataFlavors
	}

	override def isDataFlavorSupported(flavor: DataFlavor): Boolean =
	{
		return flavor.equals(RepositoryItemLabel.riDataFlavor)
	}

	def addInputConnection(inRIL: RepositoryItemLabel)
	{
		inputChainLabel = inRIL
		getProcessNode.addInputConnection(inRIL.getProcessNode)
	}

	def addOutputConnection(inRIL: RepositoryItemLabel, inZone: eRLZoneType = eRLZNone)
	{
		if (inZone == eRLZOutputHandle)
		{
			outputChainLabel = inRIL
			getProcessNode.addOutputConnection(inRIL.getProcessNode)
		}
	}

	def getZone(inPoint: Point): eRLZoneType =
	{
		var peerBounds: Rectangle = peer.bounds()
		var peerLoc: Point = peer.location()
//		println("point: " + inPoint)
		if (inPoint.getX() > peer.bounds.getWidth() - 15)
		{
			return eRLZOutputHandle
		}

		if (inPoint.getX() < + 15)
		{
			return eRLZInputHandle
		}

		return eRLZBody
	}

	def getZoneByConnectedItem(inRIL: RepositoryItemLabel): eRLZoneType =
	{
		if (inRIL == outputChainLabel)
			return eRLZOutputHandle

		if (inRIL == inputChainLabel)
			return eRLZInputHandle

		return eRLZNone
	}

	def getAllOutputs(): ListBuffer[RepositoryItemLabel] =
	{
		var	myOutputs: ListBuffer[RepositoryItemLabel] = new ListBuffer[RepositoryItemLabel]

		myOutputs += outputChainLabel
	}

	def getZoneLocation(inZone: eRLZoneType): Point =
	{
		var	zoneLoc: Point = new Point

		inZone match
		{
			case `eRLZInputHandle` =>
				zoneLoc.setLocation(1, getHeight() / 2)

			case `eRLZOutputHandle` =>
				zoneLoc.setLocation(getWidth(), getHeight() / 2)

			case _ =>
				zoneLoc.setLocation(this.location)
		}

		return zoneLoc
	}

	def getZoneLocationByConnectedItem(inRIL: RepositoryItemLabel): Point =
	{
		var	theZone: eRLZoneType = getZoneByConnectedItem(inRIL)

		return getZoneLocation(theZone)
	}

	listenTo(mouse.clicks, mouse.moves)
	reactions +=
	{
		case e:MouseClicked =>
//			println("Got clicked!")
			if (e.clicks == 2)
			{
//				Dialog.showMessage(this, "Thank you!", title="You double-clicked me")
//				Dialog.showOptions(this, "Thank you!", title="You double-clicked me", optionType = Dialog.Options.YesNo, messageType = Dialog.Message.Question, icon = scala.swing.Swing.EmptyIcon, List("Yes", "No", "Hi!"), initial = 1)

				import javax.swing.JOptionPane
				var answer = JOptionPane.showInputDialog(this.peer, 
				        "Select sensor",
				        "Select sensor",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        Array("Temp01", "Temp02", "Gas01", "Gas02"), 
				        "Gas02");

				println("Answer is '" + answer + "'")
				publish(new RILEvent(e, this))
			}

		case e:MouseDragged =>
			publish(new RILEvent(e, this))

		case e:MousePressed =>
			this.mouseClickedZone = getZone(e.point)
			publish(new RILEvent(e, this))

		case e:MouseReleased =>
			this.mouseReleasedZone = getZone(e.point)
			publish(new RILEvent(e, this))
	}
}
