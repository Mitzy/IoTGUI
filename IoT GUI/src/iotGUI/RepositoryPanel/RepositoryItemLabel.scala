/**
 * 
 */
package iotGUI.RepositoryPanel

import scala.swing._
import scala.swing.event._
import java.awt.datatransfer.{DataFlavor, Transferable}

/**
 * @author Robert Abatecola
 *
 */

object RepositoryItemLabel
{
	var riDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=iotGUI.RepositoryPanel.RepositoryItemLabel", "Repository Item Flavor");
	var myDataFlavors: Array[DataFlavor] = Array(riDataFlavor)
}

class RepositoryItemLabel(inText: String, inBorderWeight: Int = 1) extends RepositoryLabel(inText, inBorderWeight) with Transferable
{
	var dragstart: Point = null

	override def clone(): RepositoryItemLabel =
	{
		var newRIL = new RepositoryItemLabel(text, borderWeight)

		newRIL.setRLItem(myRLItem.clone())

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

	listenTo(mouse.clicks, mouse.moves)
	reactions +=
	{
		case e:MouseDragged =>
			if (dragstart != null)
			{
				var currentLocation: Point = peer.getLocation()

//				println("e.point = " + e.point)
				println("\"" + this.text + "\"" + " peer location = " + peer.getLocation())

				peer.setLocation(currentLocation.x - dragstart.x + e.point.x, currentLocation.y - dragstart.y + e.point.y)
			}

		case e:MousePressed =>
			dragstart = e.point

		case e:MouseReleased =>
			dragstart = null    
	}
}
