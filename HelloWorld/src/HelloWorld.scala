object HelloWorld
{
	def main(args: Array[String])
	{
		println("Hello, world!")
		args.foreach(arg => print(arg + " "))
	}
}
