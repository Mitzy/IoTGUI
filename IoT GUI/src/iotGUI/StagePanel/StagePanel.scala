package iotGUI.StagePanel

//import swing._
import scala.swing._
import scala.swing.event._
import javax.swing.TransferHandler
import javax.swing.TransferHandler._
import java.awt.{Color, geom}
import iotGUI.RepositoryPanel._
import iotGUI.NULLPanel
import iotGUI.cRIObject
import com.sun.jmx.mbeanserver.Repository
import com.sun.org.apache.bcel.internal.util.Repository

class StagePanel extends NULLPanel
{
	var riDataFlavor = RepositoryItemLabel.riDataFlavor
	var mySPTG: mySPTransferHandler = new mySPTransferHandler(this)
	var selectedRL: RepositoryItemLabel = null
	var	startPoint: Point = null
	var dragstart: Point = null
	/* records the dragging */
	var path = new geom.GeneralPath
	var	connectionPath = new geom.GeneralPath
	// Head of repository item output chain
	var riHead: cRIObject = null
	// Head of GUI labels corresponding to the RI output chain
	var rlHead: RepositoryLabel = null

	border = Swing.LineBorder(java.awt.Color.BLACK)
	peer.setTransferHandler(mySPTG)

	focusable = true
	listenTo(mouse.clicks, mouse.moves, keys)
	reactions +=
	{
//		case e:KeyTyped =>
//			println("KeyTyped")

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
			selectRL(null)
			println(e.toString)
			startPoint = e.point
  			requestFocusInWindow()
			println(e.asInstanceOf[scala.swing.event.MouseEvent].peer.isConsumed())

		case e:MouseDragged  =>
			lineTo(e.point)
			println(e.source)

		case e:MouseReleased =>
			lineTo(e.point)

		case KeyTyped(_,'c',_,_) => 
			path = new geom.GeneralPath
			repaint()

		case e:KeyPressed =>
			moveItem(selectedRL, e)

		case e:RILEvent =>
			e.myEvent match
			{
				case e2:MouseDragged =>
					repaint
					if (dragstart != null)
					{
						var currentLocation: Point = e.source.peer.getLocation()
		
//						println("e2.point = " + e2.point)
						println("\"" + e.source.peer.getText() + "\"" + " peer location = " + e.source.peer.getLocation())
		
						e.source.peer.setLocation(currentLocation.x - dragstart.x + e2.point.x, currentLocation.y - dragstart.y + e2.point.y)
					}
	
				case e2:MousePressed =>
					requestFocusInWindow()
					dragstart = e2.point
					selectRL(e.source)
	
				case e2:MouseReleased =>
					dragstart = null
			}

		case _: FocusLost => repaint()
	}

	def moveItem(inItem: RepositoryItemLabel, inEvent: KeyPressed)
	{
		var	currLoc: Point = null
		var	increment: Int = 1

		if (inItem == null)
			return

		if (inEvent.modifiers == Key.Modifier.Shift)
			increment = 10
		if (inEvent.modifiers == (Key.Modifier.Shift + Key.Modifier.Control))
			increment = 25

		currLoc = inItem.peer.getLocation
		inEvent.key match
		{
			case Key.Left =>
				currLoc.x -= increment

			case Key.Right =>
				currLoc.x += increment

			case Key.Up =>
				currLoc.y -= increment

			case Key.Down =>
				currLoc.y += increment

			case _ =>
				// Do nothing for other values
		}

		inItem.peer.setLocation(currLoc)
		repaint()
	}

	def selectRL(inRL: RepositoryItemLabel)
	{
		if (selectedRL != null)
			selectedRL.selectMe(false)

		if (inRL != null)
			inRL.selectMe(true)

		selectedRL = inRL
	}

	def lineTo(p: Point)
	{
		path = new geom.GeneralPath
		moveTo(startPoint)
		path.lineTo(p.x, p.y)
		repaint()
	}

	def moveTo(p: Point) { path.moveTo(p.x, p.y); repaint() }

	override def paintComponent(g: Graphics2D)
	{
		super.paintComponent(g)
		g.setColor(new Color(100,100,100))
		g.drawString("Press left mouse button and drag to paint." + 
		(if (hasFocus) " Press 'c' to clear." else ""), 10, size.height-10)
		g.setColor(Color.black)
		g.draw(path)

		if (rlHead != null)
		{
			var rlItem = rlHead
			var outputRLItem: RepositoryLabel = null

			connectionPath = new geom.GeneralPath

			while (rlItem != null)
			{
				outputRLItem = rlItem.nextOutputChainLabel
				if (outputRLItem != null)
				{
					var startPt: Point = rlItem.peer.getLocation()
					var	endPt: Point = outputRLItem.peer.getLocation()

					startPt.x += rlItem.peer.getWidth() - 1
					startPt.y += rlItem.peer.getHeight() / 2

					endPt.x += 1
					endPt.y += outputRLItem.peer.getHeight() / 2

					connectionPath.moveTo(startPt.x, startPt.y)
					connectionPath.lineTo(endPt.x, endPt.y)
				}
				rlItem = outputRLItem
			}
			g.draw(connectionPath)
		}
	}

	// Test drawing line between the two most recently added items
	var	lastAdded: RepositoryLabel = null

	def add(comp: RepositoryItemLabel, x: Int, y: Int): Unit =
	{
		super.add(comp, x, y, 3, 1)

		if (riHead == null)
			riHead = comp.getRLItem
		if (rlHead == null)
			rlHead = comp

		listenTo(comp)

		if (lastAdded != null)
		{
			lastAdded.getRLItem.addOutputConnection(comp.getRLItem)
			comp.getRLItem.addInputConnection(lastAdded.getRLItem)

//			connectionPath = new geom.GeneralPath
//			connectionPath.moveTo(lastAdded.peer.getLocation().x, lastAdded.peer.getLocation().y)
//			connectionPath.lineTo(x, y)
			repaint()

			lastAdded.nextOutputChainLabel = comp
		}
		lastAdded = comp

//		return super.add(comp, x, y)
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
