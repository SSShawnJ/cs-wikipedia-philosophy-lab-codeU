package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	private static int paraCount=0;
	private static List<String> links=new ArrayList<>();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		visit(url);

        // the following throws an exception so the test fails
        // until you update the code
        //String msg = "Complete this lab by adding your code and removing this statement.";
        //throw new UnsupportedOperationException(msg);
	}
	
	
	//recursively visit the first valid url
	public static void visit(String url) throws IOException{
		Elements paragraphs = wf.fetchWikipedia(url);
		links.add(url);
		//System.out.println(url);

		Element firstPara = paragraphs.get(0);
		
		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		for (Node node: iter) {
			
			if(node.baseUri().equals("https://en.wikipedia.org/wiki/Philosophy")){
				//System.out.println(links.size());
				System.out.println("success");
				return;
			}
			
			String link=node.absUrl("href");
			if(node instanceof TextNode) checkPara(node.toString());
			
			if(!link.equals("") ){
				if(isRevisit(link)){
					System.out.println("fail");
					return;
				}
				
				if(!paraIsValid()) continue;
				
				if(isItalic(node)) continue;
				
				
				visit(link);
				
				return;
			
			}
			else continue;
			
		}
	}
	
	public static void checkPara(String p){
		char[] para=p.toCharArray();
		for(char c:para){
			if(c=='(') paraCount++;
			else if(c==')') paraCount--;
		}
	}
	
	public static boolean paraIsValid(){
		return paraCount==0;
	}
	
	public static boolean isRevisit(String link){
		if(links.contains(link)) return true;
		return false;
	}
	
	public static boolean isItalic(Node node){
		Node n=node;
		while(n!=null){
			if(n.hasAttr("i")|| n.hasAttr("em")){
				return true;
			}
			n=n.parentNode();
		}
		return false;
	}
}
