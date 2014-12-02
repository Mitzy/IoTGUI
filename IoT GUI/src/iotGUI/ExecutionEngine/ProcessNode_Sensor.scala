package iotGUI.ExecutionEngine

class Sensor
{
	def Connect(): Int =
	{
		return 0
	}

	def Disconnect(): Int =
	{
		return 0
	}

	def Read(): String =
	{
		return ""
	}

	def Write(): Int =
	{
		println("Writing to sensor <(show the value here>")
		return 0
	}
}

class ProcessNode_Sensor extends ProcessNode
{
	var	action: Int = 0
	var mySensor: Sensor = null

	// This is where the process runs
	override def processNode(): ProcessNode =
	{
		// Read or write sensor data, or connect/disconnect sensor
		action match
		{
			case 1 =>
				ConnectSensor()
			case 2 =>
				DisconnectSensor()
			case 3 =>
				ReadSensor()
			case 4 =>
				WriteSensor()
		}
		
		return super.processNode()
	}


	def ConnectSensor(): Int =
	{
		// Do whatever it means to connect to the sensor
		return mySensor.Connect()
	}

	def DisconnectSensor(): Int =
	{
		// Do whatever it means to disconnect from the sensor
		return mySensor.Disconnect()
	}

	def ReadSensor(): String =
	{
		// Do whatever it means to read from the sensor
		return mySensor.Read()
	}

	def WriteSensor(): Int =
	{
		// Do whatever it means to write to the sensor
		return mySensor.Write()
	}
}
