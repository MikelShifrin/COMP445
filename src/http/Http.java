package http;

import java.util.*;
import java.net.*;
import java.io.*;



public class Http {
	private static Socket s;
	private String host = "";
	private String path = "";
	private String query = "";
	private String body = "";
	
	/*
	public static void main(String[] args) throws Exception{
		s = new Socket(InetAddress.getByName("httpbin.org"), 80);
		
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("GET /get HTTP/1.0");
		
		String [] a = {"Host: httpbin.org", "Accept: *//*"};
		//System.out.print(headerList(a));
		pw.print(headerList(a));
		//pw.println("Host: httpbin.org");
		//pw.println("Accept: *//*");
		pw.println("");
		pw.flush();
		
		System.out.println(verbose(true));
		//System.out.println(help("p"));
		

	}
	*/
	public String get(boolean verbose, String[] headers, String url) throws Exception{
		
		URL a = new URL(url);
		host = a.getHost();
		path = a.getPath();
		query = a.getQuery();
		s = new Socket(InetAddress.getByName(host), 80);
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		//System.out.println(path);
		//System.out.println(query);
		if(query == null) {
			pw.println("GET " + path  + " HTTP/1.0");
		}
		else {
			pw.println("GET " + path + "?" + query + " HTTP/1.0");
		}
		System.out.println("got until headers inside get");
		if(headers != null) {
			pw.print(headerList(headers));
		}
		pw.println("");
		pw.flush();
		return verbose(verbose);
	}
	public String post(boolean verbose, String[] headers, String url, String body) throws Exception{
		URL a = new URL(url);
		host = a.getHost();
		path = a.getPath();
		query = a.getQuery();
		this.body = body;
		s = new Socket(InetAddress.getByName(host), 80);
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		//System.out.println(path);
		//System.out.println(query);
		pw.println("POST " + path + " HTTP/1.0");
		pw.print(headerList(headers));
		pw.println("Content-Length: " + body.length());
		pw.println("");
		pw.println(this.body);
		pw.flush();
		return verbose(verbose);
	}
	public String verbose(boolean verbose) throws Exception{
		Scanner response = new Scanner(s.getInputStream());
		String line = "";
		if(verbose) {
			while(response.hasNext()) {
				line += response.nextLine() + "\n"; 
			}	
		}
		else {
			boolean body = false;
			String currentLine = "random"; //should not be null or ""
			while(response.hasNext()) {
				currentLine = response.nextLine();
				if(body) {
					line += currentLine + "\n";
				}
				else{
					if(currentLine.trim().isEmpty()){
						body = true;
					}
				}
			}
		}
		response.close();
		return line;
	}
	
	public String help(String type) {
		switch(type) {
		  case "get":
			  return "usage: httpc get [-v] [-h key:value] URL\n\n" +
		    		"Get executes a HTTP GET request for a given URL.\n" +
		    		"-v\t" +
		    		"Prints the detail of the response such as protocol, status, and headers.\n" +
		    		"-h key:value\t" +
		    		"Associates headers to HTTP Request with the format \'key:value\'.\n";
		    
		  case "post":
			  return  "usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\n\n" +
					  "Post executes a HTTP POST request for a given URL with inline data or from file.\n\t" +
					  "-v\t" +
					  "Prints the detail of the response such as protocol, status, and headers.\n\t" +
					  "-h key:value\t" +
					  "Associates headers to HTTP Request with the format \'key:value\'.\n\t" +
					  "-d string\t" +
					  "Associates an inline data to the body HTTP POST request.\n\t" +
					  "-f file\t" +
					  "Associates the content of a file to the body HTTP POST request.\n\n" +
					  "Either [-d] or [-f] can be used but not both.";
		   
		  default:
			  return "Usage:\n\t" +
	    		"httpc command [arguments]\n" +
	    		"The commands are:\n\t" +
	    		"get\t" +
	    		"executes a HTTP GET request and prints the response.\n\t" +
	    		"post\t" +
	    		"executes a HTTP POST request and prints the response.\n\t" +
	    		"help\t" +
	    		"prints this screen.\n" +
	    		"Use \"httpc help [command]\" for more information about a command.";  
		}
	}
	
	public String headerList(String[] keyValue) {
		String headers = "";
		for(int i=0; i < keyValue.length;i++) {
			headers += keyValue[i] + "\n";
		}
		return headers;
	}

}

