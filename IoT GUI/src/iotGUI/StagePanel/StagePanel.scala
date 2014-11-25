package iotGUI.StagePanel

//import swing._
import scala.swing._
import scala.swing.event._
import javax.swing.TransferHandler
import javax.swing.TransferHandler._
import java.awt.datatransfer.{Transferable, DataFlavor}
import iotGUI.RepositoryPanel._
import iotGUI.NULLPanel

class StagePanel extends NULLPanel
{
	var riDataFlavor = RepositoryItemLabel.riDataFlavor
	var mySPTG: mySPTransferHandler = new mySPTransferHandler(this)
	var selectedRL: RepositoryLabel = null

	border = Swing.LineBorder(java.awt.Color.BLACK)
	peer.setTransferHandler(mySPTG)

	listenTo(mouse.clicks, keys)
	reactions +=
	{
		case e:KeyTyped =>
			println("KeyTyped")

//		case e:MouseDragged =>
//			if (dragstart != null)
//			{
//				var currentLocation: Point = peer.getLocation()
//
////				println("e.point = " + e.point)
//				println("\"" + this.text + "\"" + " peer location = " + peer.getLocation())
//
//				peer.setLocation(currentLocation.x - dragstart.x + e.point.x, currentLocation.y - dragstart.y + e.point.y)
//			}

		case e:MousePressed =>
//			selectedRL = ??
			println(e.toString)
//			dragstart = e.point

		case e:MouseReleased =>
//			dragstart = null    
	}
}

class mySPTransferHandler extends TransferHandler
{
	var	mySP: StagePanel = null
	var riDataFlavor = RepositoryItemLabel.riDataFlavor

	def this(inSP: StagePanel)
	{
		this()
		mySP = inSP
	}

	override def importData(support: TransferSupport): Boolean =
	{
		if (!canImport(support))
		{
			return false
		}

		var myTransferable = support.getTransferable()
		myTransferable.getTransferData(riDataFlavor)
		var dropLoc = support.getDropLocation()

		var myRILabel: RepositoryItemLabel = myTransferable.getTransferData(riDataFlavor) match
		{
			case x:RepositoryItemLabel => x
			case _ => null
		}

		mySP.add(myRILabel, dropLoc.getDropPoint().x - myRILabel.getWidth() / 2, dropLoc.getDropPoint().y - myRILabel.getHeight() / 2)
		mySP.repaint
//		println("After cast: " + myRILabel.text)
		return true
	}

	override def canImport(support: TransferSupport): Boolean =
    {
        support.setShowDropLocation(false)
        if (!support.isDrop())
        {
            return false
        }
//            System.err.println("comp = " + support.getComponent().getClass)
//            System.err.println("tran = " + support.getTransferable().getClass)
//            System.err.println("supp = " + support.getClass)

        if (!support.isDataFlavorSupported(riDataFlavor))
        {
            System.err.println("only riDataFlavor is supported")
            var junk = support.getDataFlavors()
            System.err.println("got " + support.getDataFlavors())

            return false
        }

        var dropLoc = support.getDropLocation()
        var dropPt: Point = dropLoc.getDropPoint()

        if (dropPt == null)
        {
        	return false
        }

        support.setShowDropLocation(true)
        return true;
    }
}
