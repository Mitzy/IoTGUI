/**
 *
 */
package iotGUI.RepositoryPanel

//import scala.swing.Swing
import scala.swing._
import scala.swing.event._
import scala.swing.Insets
import scala.swing.GridBagPanel
import scala.swing.GridBagPanel._
import scala.swing.Label
//import scala.swing.GridPanel
//import scala.swing.Orientation._

/**
 * @author Robert Abatecola
 *
 */

//class RepositoryPanel extends GridPanel(0, 1)
class RepositoryPanel extends GridBagPanel
{
//	border = Swing.LineBorder(java.awt.Color.BLACK)
//	vGap = 3

	var	lastY: Int = 0
	var	emptyLabel = new Label

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

	def resetEmptyLabel()
	{
		val c = new Constraints

		c.weighty = 1
		c.gridx = 0;
		c.gridy = lastY;

		layout -= emptyLabel
		layout(emptyLabel) = c
	}

	def	addSectionLabel(inSL: RepositorySectionLabel)
	{
		val c = new Constraints

		c.weightx = 0.5
//		c.ipadx = 1
		c.insets = new Insets(1, 1, 1, 1)

		c.fill = Fill.Horizontal
		c.gridx = 0;
		c.gridy = lastY;
		layout(inSL) = c
		lastY += 1

		resetEmptyLabel()
	}

	def addItemLabel(inIL: RepositoryItemLabel)
	{
		val c = new Constraints

		c.weightx = 0.5
//		c.ipadx = 1
		c.insets = new Insets(1, 1, 1, 1)

		c.fill = Fill.Horizontal
		c.gridx = 0;
		c.gridy = lastY;
		layout(inIL) = c
		lastY += 1

		resetEmptyLabel()

		listenTo(inIL)
	}
}
