package iotGUI.StagePanel

//import swing._
import scala.swing._
import scala.swing.event._
import javax.swing.TransferHandler
import javax.swing.TransferHandler._
import scala.collection.mutable.ListBuffer
import java.awt.{Color, geom}
import iotGUI.RepositoryPanel._
import iotGUI.NULLPanel
import iotGUI.ExecutionEngine._
import eRLZoneType._
//import com.sun.jmx.mbeanserver.Repository
//import com.sun.org.apache.bcel.internal.util.Repository
//import java.lang.Boolean

class StagePanel extends NULLPanel
{
	var riDataFlavor = RepositoryItemLabel.riDataFlavor
	var mySPTG: mySPTransferHandler = new mySPTransferHandler(this)
	var selectedRL: RepositoryItemLabel = null
	var	startPoint: Point = null
	var dragstart: Point = null
	// records the dragging
	var path = new geom.GeneralPath
	var	connectionPath = new geom.GeneralPath
	// Connection creation vars
	var	connectDragging: Boolean = false
	var clickedItem: RepositoryItemLabel = null

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
//			println(e.toString)
//			startPoint = e.point
  			requestFocusInWindow()
//			println(e.asInstanceOf[scala.swing.event.MouseEvent].peer.isConsumed())

		case e:MouseDragged  =>
//			lineTo(e.point)
//			println(e.source)

//		case e:MouseReleased =>
//			lineTo(e.point)

		case KeyTyped(_,'c',_,_) => 
			path = new geom.GeneralPath
			repaint()

		case e:KeyPressed =>
			moveItem(selectedRL, e)

		case e:RILEvent =>
			var myRIL = e.source
			e.myEvent match
			{
				case e2:MouseClicked =>
					if (e2.clicks == 2)
						println("Got double-clicked")

				case e2:MouseDragged =>
					repaint
					if (dragstart != null)
					{
						var currentLocation: Point = myRIL.peer.getLocation()

//						println("e2.point = " + e2.point)
//						println("\"" + e.source.peer.getText() + "\"" + " peer location = " + e.source.peer.getLocation())
		
						myRIL.peer.setLocation(currentLocation.x - dragstart.x + e2.point.x, currentLocation.y - dragstart.y + e2.point.y)
					}
					if (connectDragging)
					{
						var endPoint = e2.point
						var	zoneLoc: Point = myRIL.getZoneLocation(myRIL.mouseClickedZone)
/*
			  			println("start loc: " + startPoint)
			  			println("drag loc: " + e2.point)
			  			println("clickedItem loc: " + clickedItem.location)
			  			println("clickedItem w, h: " + clickedItem.getWidth() + ", " + clickedItem.getHeight())
*/
						endPoint.setLocation(startPoint.getX() + endPoint.getX() - zoneLoc.getX(), startPoint.getY() + endPoint.getY() - zoneLoc.getY())
						lineTo(endPoint)
					}
	
				case e2:MousePressed =>
					requestFocusInWindow()
					if (myRIL.mouseClickedZone != eRLZBody)
					{
						var	zoneLoc: Point = myRIL.getZoneLocation(myRIL.mouseClickedZone)

						connectDragging = true;
						clickedItem = myRIL
						startPoint = e2.point
						startPoint.setLocation(clickedItem.location.getX() + zoneLoc.getX(), clickedItem.location.getY() + zoneLoc.getY())
			  			requestFocusInWindow()
//			  			println("click loc: " + e2.point)
//			  			println("startPoint: " + startPoint)
					}
					else
					{
						dragstart = e2.point
						selectRL(e.source)
					}
	
				case e2:MouseReleased =>
					if (connectDragging)
					{
						var endPoint = e2.point
						var releasedItem: RepositoryItemLabel = null
						var clickedZoneLoc: Point = clickedItem.getZoneLocation(clickedItem.mouseClickedZone)

						endPoint.setLocation(startPoint.getX() + endPoint.getX() - clickedZoneLoc.x, startPoint.getY() + endPoint.getY() - clickedZoneLoc.y)

						releasedItem = getComponentHere(endPoint)
						if ((releasedItem != null) && (releasedItem != clickedItem))
						{
							releasedItem.addInputConnection(clickedItem)
							clickedItem.addOutputConnection(releasedItem, clickedItem.mouseClickedZone)
						}
						path = new geom.GeneralPath
						repaint()
					}
					dragstart = null
					clickedItem = null
					connectDragging = false
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
//		g.clearRect(0, 0, this.bounds.getWidth.toInt, this.bounds.getHeight.toInt)
		super.paintComponent(g)
		g.setColor(new Color(100,100,100))
//		g.drawString("Press left mouse button and drag to paint." + 
//		(if (hasFocus) " Press 'c' to clear." else ""), 10, size.height-10)
		g.setColor(Color.black)
		g.draw(path)


		var rilItem: RepositoryItemLabel = null
		connectionPath = new geom.GeneralPath
		for (thing <- contents)
		{
			rilItem = thing match
			{
				case x:RepositoryItemLabel => x
				case _ => null
			}
			if (rilItem != null)
			{
				var outputRLList: ListBuffer[RepositoryItemLabel] = rilItem.getAllOutputs()
//				var outputRLItem: RepositoryItemLabel = rilItem.outputChainLabel
//				var outputRLItem: RepositoryItemLabel = null
				for (outputRLItem <- outputRLList)
				{
//					var outputRLItem: RepositoryItemLabel = _
//				}
					if (outputRLItem != null)
					{
						var startPt: Point = rilItem.peer.getLocation()
						var	endPt: Point = outputRLItem.peer.getLocation()
	//					var	startZoneLoc: Point = rilItem.getZoneLocation(eRLZOutputHandle)
						var	startZoneLoc: Point = rilItem.getZoneLocationByConnectedItem(outputRLItem)
	//					var	endZoneLoc: Point = outputRLItem.getZoneLocation(eRLZInputHandle)
						var	endZoneLoc: Point = outputRLItem.getZoneLocation(eRLZInputHandle)
	
						startPt.x += startZoneLoc.x
						startPt.y += startZoneLoc.y
	
						endPt.x += endZoneLoc.x
						endPt.y += endZoneLoc.y
	
						connectionPath.moveTo(startPt.x, startPt.y)
						connectionPath.lineTo(endPt.x, endPt.y)
					}
				}
			}
		}
		g.draw(connectionPath)
/*
		if (rlHead != null)
		{
			var rlItem = rlHead
			var outputRLItem: RepositoryItemLabel = null

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
*/
	}

	def getComponentHere(inPoint: Point): RepositoryItemLabel =
	{
//		var rItem: RepositoryItemLabel = rlHead
		var lastHitItem: RepositoryItemLabel = null
		var hitCount: Int = 0

//		println("getComponentHere: inPoint = ", inPoint)
		var rilItem: RepositoryItemLabel = null
		for (thing <- contents)
		{
			rilItem = thing match
			{
				case x:RepositoryItemLabel => x
				case _ => null
			}
			if (rilItem != null)
			{
				if (rilItem.peer.bounds.contains(inPoint))
				{
					hitCount += 1
					lastHitItem = rilItem
				}
			}
		}

		println("getComponentHere: point is in " + hitCount + " item" + (if (hitCount != 1) "s" else ""))

		return lastHitItem
	}

	// Test drawing line between the two most recently added items
//	var	lastAdded: RepositoryItemLabel = null

	def add(comp: RepositoryItemLabel, x: Int, y: Int): Unit =
	{
		super.add(comp, x, y, 3, 1)

		listenTo(comp)
/*
		if (lastAdded != null)
		{
//			lastAdded.getRLItem.addOutputConnection(comp.getRLItem)
//			comp.getRLItem.addInputConnection(lastAdded.getRLItem)

//			connectionPath = new geom.GeneralPath
//			connectionPath.moveTo(lastAdded.peer.getLocation().x, lastAdded.peer.getLocation().y)
//			connectionPath.lineTo(x, y)
			repaint()

			lastAdded.nextOutputChainLabel = comp
		}
		lastAdded = comp
*/

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
