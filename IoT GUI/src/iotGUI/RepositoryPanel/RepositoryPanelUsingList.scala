/**
 *
 */
package iotGUI.RepositoryPanel

//import scala.swing.Swing
import scala.swing._
//import scala.swing.event._
import javax.swing.JComponent
import javax.swing.TransferHandler
import javax.swing.TransferHandler._
import java.awt.datatransfer.{DataFlavor, Transferable}

/**
 * @author Robert Abatecola
 *
 */

//class RepositoryPanel extends GridPanel(0, 1)
class RepositoryPanelUsingList[RepositoryLabel](items: Seq[RepositoryLabel]) extends ListView(items)
{
	def this() = this(null)

 	var myRPTG: myRPTransferHandler = new myRPTransferHandler(this.asInstanceOf[RepositoryPanelUsingList[iotGUI.RepositoryPanel.RepositoryLabel]])

	//	border = Swing.LineBorder(java.awt.Color.BLACK)
//	vGap = 3

	peer.setTransferHandler(myRPTG)

/*
	listenTo(mouse.clicks, mouse.moves)
	reactions +=
	{
		case e:MouseDragged =>
//			if (dragstart != null)
			{
				var currentLocation: Point = peer.getLocation()

//				println("e.point = " + e.point)
				println("Rep Panel got: " + e.peer)

//				peer.setLocation(currentLocation.x - dragstart.x + e.point.x, currentLocation.y - dragstart.y + e.point.y)
			}

		case e:MousePressed =>
//			dragstart = e.point

		case e:MouseReleased =>
//			dragstart = null    
	}
*/
}

class myRPTransferHandler extends TransferHandler
{
	var	myRP: RepositoryPanelUsingList[iotGUI.RepositoryPanel.RepositoryLabel] = null

	def this(inRP: RepositoryPanelUsingList[iotGUI.RepositoryPanel.RepositoryLabel])
	{
		this()
		myRP = inRP
	}

	override def getSourceActions(c: JComponent): Int =
	{
		return COPY
	}

	override def createTransferable(c: JComponent): Transferable =
	{
		var myObject = myRP.peer.getSelectedValue()
		var myRILabel: RepositoryItemLabel = myObject match
		{
		  case x:RepositoryItemLabel => x
		  case _ => null
		}

		return myRILabel
	}

	override def exportDone(c: JComponent, t: Transferable, action: Int) =
	{
		if (action == COPY)
		{
			myRP.peer.clearSelection()
		}
	}
}