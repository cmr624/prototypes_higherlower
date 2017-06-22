import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/*
	main class.
		Game - a game about deciding whether or not the given statistic 
		is higher or lower than the number given. Very simple, includes basic scoring system.
	//including a commit change

*/
public class higher_lower
{
	//main
	public static void main(String[] args) 
	{
		menuLoop();
	}

	public static void menuLoop()
	{
		boolean run = true;
		while(run)
		{
			Scanner input = new Scanner(System.in);

			//TODO: figure out how to do this better
			String[] options = new String[4];
			options[0] = "(1) NEW GAME";
			options[1] = "(2) LOAD GAME";
			options[2] = "(3) SCOREBOARD";
			options[3] = "(4) EXIT";

			//initialize a menu
			String menuName = "WELCOME TO HIGHER LOWER";
			Menu menu = new Menu(options, menuName);
			menu.printMenu();
			int userCommand = input.nextInt();
			switch(userCommand)
			{
				case 1: newGame();
						break;
				case 2: loadGame();
						break;
				case 3: showScoreboard();
						break;
				case 4: run = leaveGame();
						break;
				default: System.out.println("Choose (1) (2) (3) (4).");
						 userCommand = input.nextInt();
						 break;
			}
		}
		System.exit(0);
	}

	public static void newGame()
	{
		Scanner input = new Scanner(System.in);
		clear();
		System.out.println("**********************");
		System.out.println("****** NEW GAME ******");
		System.out.println("**********************");

		//get user input for # of questions
		System.out.println("How many questions?");
		int questionNum = input.nextInt();
		String fileName = "content.csv";
		//initialize the questions w the given question #
		Question qarray [] = initialize(questionNum, fileName);

		//welcome screen
		System.out.println("Type in your name:");
		Player player = new Player(input.next());
		System.out.println("Welcome, " + player.name + "!");
		player.printScore();

		//game loop, loops for the number of questions currently
		questionLoop(questionNum, qarray, player, input);

	}

	public static void loadGame()
	{

	}

	public static void showScoreboard()
	{

	}

	public static boolean leaveGame()
	{
		System.out.println("Game exited.");
		return false;
	}

	public static void clear()
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void questionLoop(int questionNum, Question[] qarray, Player player, Scanner input)
	{
		//currently progresses linearly through qarray
		for(int i = 0; i < questionNum; i++)
		{
			//get the current question, print it, get the user's option, check it, then print the result.
			Question current = qarray[i];
			System.out.println(current);
			String userCommand = input.next();
			check(userCommand, current, player);
			player.printScore();
			System.out.println("Choose: (n)ext or check (s)core");
			userCommand = input.next();
			if (userCommand.equals("n"))
			{
				clear();
			}
			else if (userCommand.equals("s"))
			{
				clear();
				player.printScore();
			}
		}
	}

	//initialize function - deals with file i/o
	public static Question[] initialize(int size, String fileName)
	{
		//creates an array the size of hte initializition
		Question[] qarray = new Question[size];
		Scanner inputFile;
		try
		{
			//TODO: allow for a bunch of different csv's according to category
			File f = new File(fileName);
			inputFile = new Scanner(f);
			for (int i = 0; i < size; i++)
				{
					String line = inputFile.nextLine();
					if(line.contains("#"))
					{
						i -= 1;
						continue;
					}
					else
					{
						String [] splitted = new String[4];
						splitted = line.split(",");
						qarray[i] = new Question(splitted[0], Integer.parseInt(splitted[1]), 
							Integer.parseInt(splitted[2]),Integer.parseInt(splitted[3]));
					}

				}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return qarray;
	}
	//check function. pretty basic, test the user command and do the appropriate action.
	public static void check(String userCommand, Question current, Player player)
	{
		if (userCommand.equals("b"))
			{
				if (current.number > current.answer)
				{
					current.correct();
					current.answer();
					player.addScore(current.points);
				}
				else
				{
					current.answer();
					player.subtractScore(current.points);
				}
			}
			else if (userCommand.equals("s"))
			{
				if (current.number < current.answer)
				{
					current.correct();
					current.answer();
					player.addScore(current.points);
				}
				else
				{
					current.answer();
					player.subtractScore(current.points);
				}
			}
			else
			{
				//TODO: actual handling of invalid input
				System.out.println("INVALID INPUT. IDK WHAT TO DO!");
			}
	}
}
//TODO: unfinished  protoype of class. unsure how to fit in category concept.
class Category
{
	String category;
	int size;
	Question qarray[];

	Category(String category, int size, Question qarray[])
	{
		this.category = category;
		this.size = size;
		this.qarray = new Question[size];
	}
}

//question class. basic set up.
//TODO: Make everything private. 
class Question
{
	String question;
	int number;
	boolean correct = false;
	int answer;
	int points;

	//constructor, pretty borin'
	Question(String question, int number, int answer, int points)
	{
		this.question = question;
		this.number = number;
		this.answer = answer;
		this.points = points;
	}

	//sets the boolean to correct. there's prob a better way to do this but rn this is what we got
	void correct()
	{
		this.correct = true;
	}
	public String toString()
	{	
		return ("QUESTION" + "\n" + this.question + "\n" + this.number + 
		"\n" + "-------------------------" + "\n" + "Is this number too (s)mall or too (b)ig 
		compared to the FACT?\n");
	}

	//the answer function. this is just a series of print statements, I shuold consolidate 
	//the scorekeeping maybe within this, or put this into scorekeeping
	//TODO: this seems ineffecient. will go back.
	void answer()
	{
		if(this.correct)
		{
			System.out.println("CORRECT");
			System.out.println("ANSWER:" + this.answer);
			System.out.println("-------------------------\n");

		}
		else
		{
			System.out.println("INCORRECT");
			System.out.println("ANSWER:" + this.answer);
		}
	}
}

//player class, only a name and a score (just for keeping score)
class Player
{
	String name;
	int score;

	//initailze the score at 0
	Player(String name)
	{
		this.name = name;
		this.score = 0;
	}

	//prints the score, maybe could make it toString but i am adapting for the future here.
	void printScore()
	{
		System.out.println("SCORE: " + this.name + ": " + this.score + "\n");
	}


	//is there a better way to do this? think about it - 
	//right now it's just two functions that add or subtract the score. 
	//I do like the idea of the player doing all the score 
	//controlling. i don't like the main function dealing w that at all. 
	void addScore(int add)
	{
		this.score += add;
	}

	void subtractScore(int sub)
	{
		this.score -= sub;
	}
}

class Menu
{
	String[] options;
	Scanner input = new Scanner(System.in);
	String menuName;

	Menu(String[] options, String menuName)
	{
		this.options = options;
		this.menuName = menuName;
	}

	void printMenu()
	{
		System.out.println(menuName);
		System.out.println("***********************");
		for (int i = 0; i < options.length; i++)
		{
			System.out.println("***** " + options[i] + " *****");
		}
		System.out.println("***********************");
	}
}