package cn.doodlister;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * 发现 这网站做了 反爬 访问太快 会报 500 错误 所以 加延迟*/
		String url="http://ijs.mbr.pub2web.ingenta.com/search?sortDescending=true&%3Bvalue1=%22List+Of+Changes+In+Taxonomic+Opinion%22&%3Bvalue2=journal%2Fijsem&%3Bcollectiontitle=International+Journal+of+Systematic+and+Evolutionary+Microbiology+-+List+Of+Changes+In+Taxonomic+Opinion+Collection&%3BisJournalCollection=true&value2=journal/ijsem&%3Boption2=pub_serialIdent&facetNames=pub_serialIdent_facet dcterms_subject_facet&operator2=AND&sortField=prism_publicationDate&option1=dcterms_subject_facet&facetOptions=2 3&option2=pub_serialIdent_facet&operator3=AND&option3=dcterms_subject_facet&value3='New Taxa'";
		BlockingQueue<String> queue=new LinkedBlockingQueue<>();
		EntranceUrlArchieveTask EUATask=new EntranceUrlArchieveTask(queue, url);
		
		Thread a=new Thread(EUATask);
		a.start();
		for(int i=0;i<30;++i){
			new Thread(new ContentUrlAnalyzTask(queue, a, i)).start();;
			
		}
		System.out.println("结束任务");
	}

}
