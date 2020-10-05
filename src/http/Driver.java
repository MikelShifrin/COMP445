package http;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.commons.cli.*;

public class Driver {
	static String command = "";
	static String help = "";
	static String[] headers = null;
	static boolean verbose = false;
	static String url = "";
	static String body = "";

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		parse(args);
		
		Http http = new Http();
		
		switch(command) {
			case "get":
				System.out.println(http.get(verbose, headers, url));
				break;
			case "post":
				System.out.println(http.post(verbose, headers, url, body));
				break;
			case "help":
				System.out.println(http.help(help));
				break;
			default:
				System.out.println("incorrect format please enter help for more information");
				System.exit(0);
		}
		
	}
	
	public static void parse(String[] args) throws Exception {
		if(args.length < 1) {
			System.out.println("incorrect format please enter help for more information");
			System.exit(0);
		}
		else {
			if(args[0].equals("get") || args[0].equals("post") || args[0].equals("help")) {
				// create Options object
				Options options = new Options();

				// add option
				options.addOption("v", false, "verbose");
				options.addOption("h", true, "headers");
				options.addOption("d", true, "inline data");
				options.addOption("f", true, "file");
				
				CommandLineParser parser = new DefaultParser();
				CommandLine cmd = parser.parse( options, args);
				
				System.out.println("Command: "+args[0]);
				command = args[0];
				System.out.println("URL: "+args[args.length-1]);
				url = args[args.length-1];
				if(cmd.hasOption("v")) {
					verbose = true;
				}
				if(cmd.hasOption("h")) {
					headers = cmd.getOptionValues("h");
					for(int i=0;i<headers.length;i++) {
						System.out.println(headers[i]);
					}
				}
				if(cmd.hasOption("d") && cmd.hasOption("f")) {
					System.out.println("incorrect format please enter help for more information");
					System.exit(0);
				}
				if(cmd.hasOption("d")) {
					body = cmd.getOptionValue("d");
				}
				if(cmd.hasOption("f")) {
				    try {
				        File file = new File(cmd.getOptionValue("f"));
				        Scanner inputFile = new Scanner(file);
				        while (inputFile.hasNextLine()) {
				          String input = inputFile.nextLine();
				          System.out.println(input);
				          body += input;
				          if(inputFile.hasNextLine()) {
				        	  body += "\n";
				          }
				        }
				        inputFile.close();
				      } catch (FileNotFoundException e) {
				        System.out.println("An error occurred.");
				        e.printStackTrace();
				      }
				    
				}
				
				if(args[0].equals("help")) {
					if(args.length == 1) {
						help = "general";
					}
					else if(args.length ==2) {
						if(args[1].equals("get") || args[1].equals("post")) {
							help = args[1];
						}
						else {
							System.out.println("incorrect format please enter help for more information");
							System.exit(0);
						}
					}
					else {
						System.out.println("incorrect format please enter help for more information");
						System.exit(0);
					}
				}
				
			}
			else {
				System.out.println("incorrect format please enter help for more information");
				System.exit(0);
			}
		}
	}

}
