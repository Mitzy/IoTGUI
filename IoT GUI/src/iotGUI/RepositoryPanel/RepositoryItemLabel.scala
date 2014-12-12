/**
 * 
 */
package iotGUI.RepositoryPanel

import scala.swing._
import scala.swing.event._
import java.awt.datatransfer.{DataFlavor, Transferable}
import iotGUI.StagePanel._
import iotGUI.ExecutionEngine._

/**
 * @author Robert Abatecola
 *
 */

object eRLZoneType extends Enumeration
{
	type eRLZoneType = Value
	val	eRLZBody, eRLZInputHandle, eRLZOutputHandle, eRLZNone = Value
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
	var nextOutputChainLabel: RepositoryItemLabel = null

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

	def getZone(inPoint: Point): eRLZoneType =
	{
//		println("point: " + inPoint)
		if (inPoint.getX() > peer.bounds.getWidth() - 15)
		{
			return eRLZOutputHandle
		}

		if (inPoint.getX() < peer.location().x + 15)
		{
			return eRLZInputHandle
		}

		return eRLZBody
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
				        "What is your favorite pizza?",
				        "Favorite Pizza",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        Array("Cheese", "Pepperoni", "Sausage", "Veggie"), 
				        "Cheese");

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
